package com.github.phillima.asniffer;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.interfaces.ICodeElementMetricCollector;
import com.github.phillima.asniffer.metric.LOCCalculator;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.AnnotationMetricModel;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.PackageModel;
import com.github.phillima.asniffer.utils.AnnotationUtils;

public class MetricsExecutor extends FileASTRequestor{

	private AMReport report;
	private Map<String, PackageModel> packagesModel;
	ClassModel result = null;
	private Callable<List<IClassMetricCollector>> classMetrics;
	private List<IAnnotationMetricCollector> annotationMetrics;
	private List<ICodeElementMetricCollector> codeElementMetrics;
	
	private static final Logger logger = 
		      LogManager.getLogger(MetricsExecutor.class);
	
	public MetricsExecutor(Callable<List<IClassMetricCollector>> classMetrics, List<IAnnotationMetricCollector> annotationMetrics, 
						   List<ICodeElementMetricCollector> codeElementMetrics, String projectName) {
		this.classMetrics = classMetrics;
		this.annotationMetrics = annotationMetrics;
		this.codeElementMetrics = codeElementMetrics;
		this.report = new AMReport(projectName);
		this.packagesModel = new HashMap<String, PackageModel>();
	}

	@Override
	public void acceptAST(String sourceFilePath, 
			CompilationUnit cu) {
		
		try {
			ClassInfo info = new ClassInfo(cu);
			cu.accept(info);
			
			if(info.getClassName()==null) return;
		
			String packageName = info.getPackageName();
			
			PackageModel packageModel = getPackageModel(packageName);
			
			int loc = new LOCCalculator().calculate(new FileInputStream(sourceFilePath));
			int nec = info.getCodeElementsInfo().size();
			
			result = new ClassModel(sourceFilePath, info.getClassName(), info.getType(),loc, nec);
			logger.info("Initializing extraction of class metrics for class: " + info.getClassName());
			//Obtain class metrics
			for(IClassMetricCollector visitor : classMetrics.call()) {
				visitor.execute(cu, result, report);
				visitor.setResult(result);
			}
			logger.info("Finished extracting class metrics.");
			info.getCodeElementsInfo().entrySet().forEach(entry ->{
				BodyDeclaration codeElementBody = entry.getKey();
				CodeElementModel codeElementModel = entry.getValue();
				logger.info("Initializing extraction of code element metrics for element: " + codeElementModel.getElementName());
				//Obtain code element metrics
				for(ICodeElementMetricCollector visitor : codeElementMetrics) {
					visitor.execute(cu, codeElementModel, codeElementBody);
				}
				logger.info("Finished extraction of code element metrics for element: " + codeElementModel.getElementName());
				//Annotation Metrics
				List<Annotation> annotations = AnnotationUtils.checkForAnnotations(codeElementBody);
				
				logger.info("Initializing extraction of annotation metrics for code element: " + codeElementModel.getElementName());
				
				annotations.forEach(annotation -> {
					AnnotationMetricModel annotationMetricModel = new AnnotationMetricModel(
								annotation.getTypeName().toString(), 
							    cu.getLineNumber(annotation.getStartPosition()), 
						   		result.getAnnotationSchema(annotation.getTypeName().getFullyQualifiedName()
								   +"-"+cu.getLineNumber(annotation.getStartPosition())));
					for (IAnnotationMetricCollector annotationCollector : annotationMetrics) {
						annotationCollector.execute(cu, annotationMetricModel, annotation);
					}
					codeElementModel.addAnnotationMetric(annotationMetricModel);
				});
				logger.info("Finished extraction of annotation metrics for code element: " + codeElementModel.getElementName());
				result.addElementReport(codeElementModel);
			});
			
			packageModel.addClassModel(result);
			report.addPackageModel(packageModel);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public AMReport getReport() {
		return report;
	}
	
	private PackageModel getPackageModel(String packageName) {
		if(packagesModel.containsKey(packageName))
			return packagesModel.get(packageName);
		PackageModel packageModel = new PackageModel(packageName);
		packagesModel.put(packageName, packageModel);
		return packageModel;
	}
	
}