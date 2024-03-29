package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector;
import com.github.phillima.asniffer.model.AnnotationMetricModel;


public class AA implements IAnnotationMetricCollector {

	@Override
	public void execute(CompilationUnit cu, AnnotationMetricModel annotationMetricModel, AnnotationExpr annotation) {
		int aa = 0;
		
		if(annotation instanceof NormalAnnotationExpr)
			aa = ((NormalAnnotationExpr)annotation).getPairs().size();
		if(annotation instanceof SingleMemberAnnotationExpr)
			aa = 1;
		
		//No need to check for marker annotation
		
		annotationMetricModel.addAnnotationMetric("AA", aa);
		
	}

}
