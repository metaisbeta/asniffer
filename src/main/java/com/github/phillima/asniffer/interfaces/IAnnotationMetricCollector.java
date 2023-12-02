package com.github.phillima.asniffer.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.phillima.asniffer.model.AnnotationMetricModel;

public interface IAnnotationMetricCollector {

	public void execute(CompilationUnit compUnit, AnnotationMetricModel annotationMetricModel, AnnotationExpr annotation);
}
