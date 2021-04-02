package com.github.phillima.asniffer.metric;

import com.github.phillima.asniffer.annotations.AnnotationMetric;
import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector;
import com.github.phillima.asniffer.model.AnnotationMetricModel;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;

@AnnotationMetric
public class LOCAD implements IAnnotationMetricCollector {

	private CompilationUnit cu;
	
	@Override
	public void execute(CompilationUnit cu, AnnotationMetricModel annotationMetricModel,
			Annotation annotation) {
		this.cu= cu;
		int locad = getNumLines(annotation);
		annotationMetricModel.addAnnotationMetric("LOCAD", locad);
	}
	
	private int getNumLines(Annotation annotation) {

		int locad = 0;
		int startLineNumber = cu.getLineNumber(annotation.getStartPosition());
		int nodeLength = annotation.getLength();
		int endLineNumber = cu.getLineNumber(annotation.getStartPosition() + nodeLength);
		locad = endLineNumber - startLineNumber + 1;
		return locad;
	}
}
