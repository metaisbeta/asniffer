package br.inpe.cap.asniffer.metric;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.ElementMetric;
import br.inpe.cap.asniffer.MetricResult;

public class AA extends ASTVisitor implements MetricCollector {

	private Map<String, Integer> aa = new HashMap<>();
	private CompilationUnit cu;
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		aa.put(node.getTypeName().getFullyQualifiedName(), 0);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		aa.put(node.getTypeName().getFullyQualifiedName(), 1);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		aa.put(node.getTypeName().getFullyQualifiedName() + "_" + cu.getLineNumber(node.getStartPosition()) , node.values().size());
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, MetricResult result, AMReport report) {
		this.cu= cu;
		cu.accept(this);

	}

	@Override
	public void setResult(MetricResult result) {
		result.addElementMetric("AA", aa);
	}

}