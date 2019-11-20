package br.inpe.cap.asniffer.metric;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.annotations.AnnotationMetric;
import br.inpe.cap.asniffer.interfaces.IAnnotationMetricCollector;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;

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
