package br.inpe.cap.asniffer.metric;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.ElementMetric;
import br.inpe.cap.asniffer.MetricResult;

public class LOCAD extends ASTVisitor implements MetricCollector {

	private Map<String, Integer> locad = new HashMap<>();
	private CompilationUnit cu;
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		locad.put(node.getTypeName().getFullyQualifiedName(), 1);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		locad.put(node.getTypeName().getFullyQualifiedName(), 1);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		locad.put(node.getTypeName().getFullyQualifiedName() + "_" + cu.getLineNumber(node.getStartPosition()),
				getNumLines(node));
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, MetricResult result, AMReport report) {
		this.cu= cu;
		cu.accept(this);

	}

	@Override
	public void setResult(MetricResult result) {
		result.addElementMetric("LOCAD", locad);
	}
	
	private int getNumLines(Annotation annotation) {

		int locad = 0;
		int startLineNumber = cu.getLineNumber(annotation.getStartPosition());
		int nodeLength = annotation.getLength();
		int endLineNumber = cu.getLineNumber(annotation.getStartPosition() + nodeLength);
		locad = endLineNumber - startLineNumber + 1;
		return locad;
	}

}