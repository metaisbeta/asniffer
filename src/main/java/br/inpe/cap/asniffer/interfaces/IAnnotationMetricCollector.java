package br.inpe.cap.asniffer.interfaces;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

import br.inpe.cap.asniffer.model.AnnotationMetricModel;

public interface IAnnotationMetricCollector {

	public void execute(CompilationUnit cu, AnnotationMetricModel annotationMetricModel, Annotation annotation);
}
