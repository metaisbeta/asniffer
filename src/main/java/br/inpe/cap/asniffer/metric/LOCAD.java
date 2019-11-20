package br.inpe.cap.asniffer.metric;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;

import br.inpe.cap.asniffer.annotations.AnnotationMetric;
import br.inpe.cap.asniffer.interfaces.IAnnotationMetricCollector;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;

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
