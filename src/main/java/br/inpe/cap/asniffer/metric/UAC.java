package br.inpe.cap.asniffer.metric;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.Metric;

public class UAC extends ASTVisitor implements MetricCollector {

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
	public void execute(CompilationUnit cu, Metric result, AMReport report) {
		cu.accept(this);
		
	}

	@Override
	public void setResult(Metric result) {
		result.addClassMetric("UAC", uniqueAnnotations.size());
		
	}

}
