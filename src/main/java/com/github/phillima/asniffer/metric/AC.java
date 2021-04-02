package com.github.phillima.asniffer.metric;

import com.github.phillima.asniffer.annotations.ClassMetric;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

@ClassMetric
public class AC extends ASTVisitor implements IClassMetricCollector {

	private int annotations = 0;
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		annotations++;
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		annotations++;
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		annotations++;
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, ClassModel result, AMReport report) {
		cu.accept(this);
		
	}

	@Override
	public void setResult(ClassModel result) {
		result.addClassMetric("AC",annotations);
	}

}
