package br.inpe.cap.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.Annotation;

import br.inpe.cap.asniffer.annotations.SimpMetric;

@SimpMetric
public class UniqueAnnotationProcessor implements SimpleMetricProcessor {

	@Override
	public int execute(List<Annotation> annotation) {
		Set<String> anotSet = new HashSet<>();
		for (Annotation anot : annotation) 
			anotSet.add(anot.toString());
		return anotSet.size();
	}

}
