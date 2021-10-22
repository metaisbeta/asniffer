package com.github.phillima.asniffer.metric;

import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector;
import com.github.phillima.asniffer.model.AnnotationMetricModel;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;


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
