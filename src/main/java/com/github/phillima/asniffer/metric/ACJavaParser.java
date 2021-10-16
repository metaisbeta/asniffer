package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.annotations.ClassMetric;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector_;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;

@ClassMetric
public class ACJavaParser extends VoidVisitorAdapter<Object> implements IClassMetricCollector_ {

	private int annotations = 0;
	
	@Override
	public void visit(MarkerAnnotationExpr node, Object obj) {
		annotations++;
		super.visit(node, obj);
	}
	
	@Override
	public void visit(NormalAnnotationExpr node, Object obj) {
		annotations++;
		super.visit(node, obj);
	}
	
	@Override
	public void visit(SingleMemberAnnotationExpr node, Object obj) {
		annotations++;
		super.visit(node, obj);
	}
	
	@Override
	public void execute(CompilationUnit cu, ClassModel result, AMReport report) {
		this.visit(cu, null);
	}

	@Override
	public void setResult(ClassModel result) {
		result.addClassMetric("AC",annotations);
	}

}
