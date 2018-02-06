package br.inpe.cap.asniffer.metric;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.ElementMetric;
import br.inpe.cap.asniffer.MetricResult;

public class AED extends ASTVisitor implements MetricCollector{

	private List<ElementMetric> aed = new ArrayList<>();
	private CompilationUnit cu;
	
	@Override
	public boolean visit(EnumDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"enum", checkForAnnotations(node), cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"compiltation-unit", checkForAnnotations(node), cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"annotation-definition", checkForAnnotations(node), cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"method", checkForAnnotations(node), cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		Object o = node.fragments().get(0);
		if(o instanceof VariableDeclarationFragment){
			String name = ((VariableDeclarationFragment) o).getName().toString();
			addElement(name,"field", checkForAnnotations(node), cu.getLineNumber(node.getStartPosition()));
		}
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnumConstantDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"enum-constant", checkForAnnotations(node), cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, MetricResult result, AMReport report) {
		this.cu = cu;
		cu.accept(this);
	}

	@Override
	public void setResult(MetricResult result) {
		result.addElementMetric("AED", aed);
	}

	private int checkForAnnotations(BodyDeclaration node) {
		
		int aedCount = 0;
		for (Object modifier : node.modifiers()) {
			if(modifier instanceof Annotation) {
				aedCount++;
				if(modifier instanceof NormalAnnotation)
					aedCount += checkNestedAnnotation((NormalAnnotation)modifier);
			}
		}
		return aedCount;
	}
	
	private void addElement(String elementName, String type, int value, int line) {
		ElementMetric _element = new ElementMetric(value,type,line,elementName);
		aed.add(_element);
	}
	
	private int checkNestedAnnotation(NormalAnnotation annotation) {
		
		int aedCounter = 0;
		List<MemberValuePair> values  = annotation.values();
		for(MemberValuePair value : values) 
			aedCounter += StringUtils.countMatches(value.getValue().toString(),"@");
		return aedCounter;
		
	}

}
