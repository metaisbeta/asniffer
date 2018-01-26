package br.inpe.cap.processor;

import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;

import br.inpe.cap.asniffer.annotations.SimpMetric;

@SimpMetric
public class AnnotationClassProcessor implements SimpleMetricProcessor {

	@Override
	public int execute(List<Annotation> annotation) {
		return annotation.size();
	}

	

}
