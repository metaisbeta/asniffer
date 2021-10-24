package com.github.phillima.asniffer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.interfaces.ICodeElementMetricCollector;
import com.github.phillima.asniffer.metric.LOCCalculator;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.AnnotationMetricModel;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.model.PackageModel;
import com.github.phillima.asniffer.utils.AnnotationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class MetricsExecutor {

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


    public void accept(List<CompilationUnit> compilationUnitList) {
        compilationUnitList.forEach(cu -> {
            try {

                ClassInfo info = new ClassInfo(cu);
                cu.accept(info, null);

                if (info.getClassName() == null) return;

                String packageName = info.getPackageName();

                PackageModel packageModel = getPackageModel(packageName);

                String sourceFilePath = cu.getStorage().get().getPath().toString();
                int loc = new LOCCalculator().calculate(new FileInputStream(sourceFilePath));
                int nec = info.getCodeElementsInfo().size();

                result = new ClassModel(sourceFilePath, info.getClassName(), info.getType(), loc, nec);
                logger.info("Initializing extraction of class metrics for class: " + info.getClassName());
                //Obtain class metrics
                for (IClassMetricCollector visitor : classMetrics.call()) {
                    visitor.execute(cu, result, report);
                    visitor.setResult(result);
                }
                logger.info("Finished extracting class metrics.");
                info.getCodeElementsInfo().entrySet().forEach(entry -> {
                    Node codeElementBody = entry.getKey();
                    CodeElementModel codeElementModel = entry.getValue();
                    logger.info("Initializing extraction of code element metrics for element: " + codeElementModel.getElementName());
                    //Obtain code element metrics
                    for (ICodeElementMetricCollector visitor : codeElementMetrics) {
                        visitor.execute(cu, codeElementModel, codeElementBody);
                    }
                    logger.info("Finished extraction of code element metrics for element: " + codeElementModel.getElementName());
                    //Annotation Metrics
                    List<AnnotationExpr> annotations = AnnotationUtils.checkForAnnotations(codeElementBody);

                    logger.info("Initializing extraction of annotation metrics for code element: " + codeElementModel.getElementName());

                    annotations.forEach(annotation -> {
                        AnnotationMetricModel annotationMetricModel = new AnnotationMetricModel(
                                annotation.getName().getIdentifier(),
                                annotation.getTokenRange().get().toRange().get().begin.line,
                                result.getAnnotationSchema(annotation.getName().getIdentifier()
                                        + "-" +   annotation.getTokenRange().get().toRange().get().begin.line));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public AMReport getReport() {
        return report;
    }

    private PackageModel getPackageModel(String packageName) {
        if (packagesModel.containsKey(packageName))
            return packagesModel.get(packageName);
        PackageModel packageModel = new PackageModel(packageName);
        packagesModel.put(packageName, packageModel);
        return packageModel;
    }

}