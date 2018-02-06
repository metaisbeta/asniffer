package br.inpe.cap.asniffer.metric;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.ElementMetric;
import br.inpe.cap.asniffer.MetricResult;

public class AA extends ASTVisitor implements MetricCollector {

	private List<ElementMetric> aa = new ArrayList<>();
	private CompilationUnit cu;
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		aa.add(new ElementMetric(0,null,cu.getLineNumber(node.getStartPosition()),node.getTypeName().getFullyQualifiedName()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		aa.add(new ElementMetric(1,null,cu.getLineNumber(node.getStartPosition()),node.getTypeName().getFullyQualifiedName()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		aa.add(new ElementMetric(node.values().size(),null,cu.getLineNumber(node.getStartPosition()),node.getTypeName().getFullyQualifiedName()));
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
