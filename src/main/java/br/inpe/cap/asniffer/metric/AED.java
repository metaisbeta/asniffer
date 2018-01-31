package br.inpe.cap.asniffer.metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.inpe.cap.asniffer.Metric;

public class AED extends ASTVisitor implements MetricCollector{

	private Map<String, Integer> aed = new HashMap<>();
	private CompilationUnit cu;
	
	@Override
	public boolean visit(EnumDeclaration node) {
		aed.put(node.getName().getFullyQualifiedName(), checkForAnnotations(node));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		aed.put(node.getName().getFullyQualifiedName(), checkForAnnotations(node));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		aed.put(node.getName().getFullyQualifiedName(), checkForAnnotations(node));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		aed.put(node.getName().getFullyQualifiedName() +"_"+ cu.getLineNumber(node.getStartPosition()) , checkForAnnotations(node));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		Object o = node.fragments().get(0);
		if(o instanceof VariableDeclarationFragment){
			String name = ((VariableDeclarationFragment) o).getName().toString();
			aed.put(name, checkForAnnotations(node));
		}
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnumConstantDeclaration node) {
		aed.put(node.getName().getFullyQualifiedName(), checkForAnnotations(node));
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, Metric result, AMReport report) {
		this.cu = cu;
		cu.accept(this);
	}

	@Override
	public void setResult(Metric result) {
		Map<String, Integer> aedValue = new HashMap<>();
		aed.forEach((k,v)->{
			aedValue.put(k, v);
		});
		
		result.addElementMetric("AED", aedValue);
		
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
	
	private int checkNestedAnnotation(NormalAnnotation annotation) {
		
		int aedCounter = 0;
		List<MemberValuePair> values  = annotation.values();
		for(MemberValuePair value : values) 
			aedCounter += StringUtils.countMatches(value.getValue().toString(),"@");
		return aedCounter;
		
	}

}
