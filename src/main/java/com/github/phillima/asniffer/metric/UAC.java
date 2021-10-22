package com.github.phillima.asniffer.metric;

import java.util.HashSet;
import java.util.Set;

import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

public class UAC extends ASTVisitor implements IClassMetricCollector {

	Set<String> uniqueAnnotations = new HashSet<>();
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		uniqueAnnotations.add(node.getTypeName().getFullyQualifiedName());
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		uniqueAnnotations.add(node.toString());
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		uniqueAnnotations.add(node.toString());
		return super.visit(node);
	}
	
	
	@Override
	public void execute(CompilationUnit cu, ClassModel result, AMReport report) {
		cu.accept(this);
		
	}

	@Override
	public void setResult(ClassModel result) {
		result.addClassMetric("UAC", uniqueAnnotations.size());
		
	}

}
