package com.github.phillima.asniffer.metric;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.phillima.asniffer.annotations.AnnotationMetric;
import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector;
import com.github.phillima.asniffer.model.AnnotationMetricModel;

import java.util.Optional;


@AnnotationMetric
public class ANL implements IAnnotationMetricCollector {
	
	@Override
	public void execute(CompilationUnit cu, AnnotationMetricModel annotationMetricModel, AnnotationExpr annotation) {

		int anl = getNestingLevel(annotation);
		annotationMetricModel.addAnnotationMetric("ANL", anl);
		
	}
	
	private int getNestingLevel(AnnotationExpr annotation) {
		Optional<Node> parentNodeOp;
		int anlCount = 0;
		parentNodeOp = annotation.getParentNode();

		while(parentNodeOp.isPresent() && !(parentNodeOp.get() instanceof CompilationUnit)) {
			Node parentNode = parentNodeOp.get();
			if(parentNode instanceof AnnotationExpr)
				anlCount++;
			parentNodeOp = parentNode.getParentNode();
		}


		return anlCount;
	}

	

}
