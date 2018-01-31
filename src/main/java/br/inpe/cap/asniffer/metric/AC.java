package br.inpe.cap.asniffer.metric;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.Metric;

public class AC extends ASTVisitor implements MetricCollector{

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
	public void execute(CompilationUnit cu, Metric result, AMReport report) {
		cu.accept(this);
		
	}

	@Override
	public void setResult(Metric result) {
		result.addClassMetric("AC",annotations);
		
	}

}
