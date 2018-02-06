package br.inpe.cap.asniffer.metric;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.ElementMetric;
import br.inpe.cap.asniffer.MetricResult;

public class ANL extends ASTVisitor implements MetricCollector {

	private List<ElementMetric> anl = new ArrayList<>();
	private CompilationUnit cu;
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		anl.add(new ElementMetric(getNestingLevel(node), null, cu.getLineNumber(node.getStartPosition()), 
					node.getTypeName().getFullyQualifiedName()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		anl.add(new ElementMetric(getNestingLevel(node), null, cu.getLineNumber(node.getStartPosition()), 
					node.getTypeName().getFullyQualifiedName()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		anl.add(new ElementMetric(getNestingLevel(node), null, cu.getLineNumber(node.getStartPosition()), 
				node.getTypeName().getFullyQualifiedName()));
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, MetricResult result, AMReport report) {
		this.cu= cu;
		cu.accept(this);
	}

	@Override
	public void setResult(MetricResult result) {
		result.addElementMetric("ANL", anl);
	}
	
	private int getNestingLevel(Annotation annotation) {
		ASTNode parentNode;
		int anlCount = 0;
		parentNode = annotation.getParent();
		while(parentNode.getNodeType() != ASTNode.COMPILATION_UNIT) {
			if(parentNode.getNodeType() == ASTNode.MARKER_ANNOTATION ||
					parentNode.getNodeType() == ASTNode.NORMAL_ANNOTATION ||
					parentNode.getNodeType() == ASTNode.SINGLE_MEMBER_ANNOTATION)
				anlCount++;
			parentNode = parentNode.getParent();		
		}
		return anlCount;
	}

}
