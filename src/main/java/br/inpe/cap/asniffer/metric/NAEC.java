package br.inpe.cap.asniffer.metric;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;


import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.Metric;

public class NAEC extends ASTVisitor implements MetricCollector{

	private int annotatedElements = 0;
	
	@Override
	public boolean visit(EnumDeclaration node) {
		checkForAnnotations(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		checkForAnnotations(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		checkForAnnotations(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		checkForAnnotations(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		checkForAnnotations(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnumConstantDeclaration node) {
		checkForAnnotations(node);
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, Metric result, AMReport report) {
		cu.accept(this);

	}

	@Override
	public void setResult(Metric result) {
		result.addClassMetric("NAEC", annotatedElements);
	}
	
	private void checkForAnnotations(BodyDeclaration node) {
		
		for (Object modifier : node.modifiers()) {
			if(modifier instanceof Annotation) {
				annotatedElements++;
				return;
			}
		}
	}
}
