package br.inpe.cap.processor;

import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;

public interface SimpleMetricProcessor {
	
	public int execute(List<Annotation> annotation);

}
