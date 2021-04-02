package com.github.phillima.asniffer.interfaces;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.phillima.asniffer.model.AnnotationMetricModel;

public interface IAnnotationMetricCollector {

	public void execute(CompilationUnit cu, AnnotationMetricModel annotationMetricModel, Annotation annotation);
}
