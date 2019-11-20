package br.inpe.cap.asniffer;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;

import br.inpe.cap.asniffer.interfaces.IAnnotationMetricCollector;
import br.inpe.cap.asniffer.interfaces.IClassMetricCollector;
import br.inpe.cap.asniffer.interfaces.ICodeElementMetricCollector;
import br.inpe.cap.asniffer.metric.LOCCalculator;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.MetricResult;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.utils.AnnotationUtils;


public class MetricsExecutor extends FileASTRequestor{

	private AMReport report;
	private Map<String, PackageModel> packagesModel;
	MetricResult result = null;
	private Callable<List<IClassMetricCollector>> classMetrics;
	private Callable<List<IAnnotationMetricCollector>> annotationMetrics;
	private Callable<List<ICodeElementMetricCollector>> codeElementMetrics;
	
	public MetricsExecutor(Callable<List<IClassMetricCollector>> classMetrics, Callable<List<IAnnotationMetricCollector>> annotationMetrics, 
						   Callable<List<ICodeElementMetricCollector>> codeElementMetrics, String projectName) {
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
			
			result = new MetricResult(sourceFilePath, info.getClassName(), info.getType(),loc);
			
			//Obtain class metrics
			for(IClassMetricCollector visitor : classMetrics.call()) {
				visitor.execute(cu, result, report);
				visitor.setResult(result);
			}
			
			
			
			
			info.getCodeElementsInfo().forEach((node,codeElement)->{
				//Obtain annotations
				System.out.println(codeElement.getElementName());
				
				//Obtain code element metrics
				try {
					for(ICodeElementMetricCollector visitor : codeElementMetrics.call()) {
						visitor.execute(cu, codeElement, node);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				//Annotation Metrics
				List<Annotation> annotations = AnnotationUtils.checkForAnnotations(node);
				for (Annotation annotation : annotations) {
					AnnotationMetricModel annotationMetricModel = new AnnotationMetricModel(annotation.getTypeName().toString(), 
																		   cu.getLineNumber(annotation.getStartPosition()));
					try {
						for (IAnnotationMetricCollector annotationCollector : annotationMetrics.call()) {
							annotationCollector.execute(cu, annotationMetricModel, annotation);
						}
						codeElement.addAnnotationMetric(annotationMetricModel);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				result.addElementReport(codeElement);
			});
			
			packageModel.add(result);
			report.addPackageModel(packageModel);
		} catch(Exception e) {
			//if(result!=null) result.error();
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
