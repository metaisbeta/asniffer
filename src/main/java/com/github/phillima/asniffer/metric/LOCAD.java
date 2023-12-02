package com.github.phillima.asniffer.metric;

import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.Node.Parsedness;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector;
import com.github.phillima.asniffer.model.AnnotationMetricModel;

public class LOCAD implements IAnnotationMetricCollector {

	private CompilationUnit compUnit;
	
	@Override
	public void execute(CompilationUnit compUnit, AnnotationMetricModel annotationMetricModel,
						AnnotationExpr annotation) {
		this.compUnit = compUnit;
		compUnit.getParsed();
		int locad = getNumLines(annotation);
		annotationMetricModel.addAnnotationMetric("LOCAD", locad);
	}
	
	private int getNumLines(AnnotationExpr annotation) {

		int locad = 0;
		Range range = annotation.getTokenRange().get().toRange().get();
		int startLineNumber = range.begin.line;
		int nodeLength = range.getLineCount();
		int endLineNumber = range.end.line;
		locad = endLineNumber - startLineNumber + 1;
		return locad;
	}
	
	public Parsedness getParsedCompUnit() {
		return this.compUnit.getParsed();
	}
}
