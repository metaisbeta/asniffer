package br.inpe.cap.asniffer.metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import br.inpe.cap.asniffer.annotations.AnnotationMetric;
import br.inpe.cap.asniffer.interfaces.IAnnotationMetricCollector;
import br.inpe.cap.asniffer.interfaces.IClassMetricCollector;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.MetricResult;

@AnnotationMetric
public class AA implements IAnnotationMetricCollector {

	@Override
	public void execute(CompilationUnit cu, AnnotationMetricModel annotationMetricModel, Annotation annotation) {
		int aa = 0;
		
		if(annotation instanceof NormalAnnotation)
			aa = ((NormalAnnotation)annotation).values().size();
		if(annotation instanceof SingleMemberAnnotation)
			aa = 1;
		
		//No need to check for marker annotation
		
		annotationMetricModel.addAnnotationMetric("AA", aa);
		
	}

}
