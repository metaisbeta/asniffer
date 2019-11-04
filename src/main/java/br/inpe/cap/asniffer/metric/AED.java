package br.inpe.cap.asniffer.metric;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam.Mode;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.ElementMetric;
import br.inpe.cap.asniffer.MetricResult;
import br.inpe.cap.asniffer.annotations.AnnotationMetric;

@AnnotationMetric
public class AED extends ASTVisitor implements MetricCollector{

	private List<ElementMetric> aed = new ArrayList<>();
	private CompilationUnit cu;
	private MetricResult result;
	
	@Override
	public boolean visit(EnumDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"enum", node, 
				cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"compiltation-unit", node, 
				cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"annotation-definition", node, 
				cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"method", node, 
				cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		Object o = node.fragments().get(0);
		if(o instanceof VariableDeclarationFragment){
			String name = ((VariableDeclarationFragment) o).getName().toString();
			addElement(name,"field", node, cu.getLineNumber(node.getStartPosition()));
		}
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnumConstantDeclaration node) {
		addElement(node.getName().getFullyQualifiedName(),"enum-constant", node, 
				cu.getLineNumber(node.getStartPosition()));
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, MetricResult result, AMReport report) {
		this.cu = cu;
		this.result = result;
		cu.accept(this);
	}

	@Override
	public void setResult(MetricResult result) {
		result.addElementMetric("AED", aed);
		result.setNec(aed.size());
	}

	
	
	private void addElement(String elementName, String type, BodyDeclaration node, int line) {
		int numAnnotations = checkForAnnotations(node);
		ElementMetric element = new ElementMetric(numAnnotations,type,line,elementName);
		aed.add(element);
		
		addElementReport(node, elementName);
	}
	
	private void addElementReport(BodyDeclaration node, String elementName) {
		
		//Reports for the element
		List<ElementMetric> elementReports = new ArrayList<>();
		for (Object modifier : node.modifiers()) {
			if(modifier instanceof Annotation)
				treateAnnotation((Annotation) modifier, elementReports);
		}
		result.addElementReport(elementName, elementReports);
	}

	private void treateAnnotation(Annotation annotation, List<ElementMetric> elementReports) {
			if(annotation instanceof SingleMemberAnnotation) {
				ElementMetric elementReport = new ElementMetric(
									((SingleMemberAnnotation)annotation).getTypeName().toString(),
									cu.getLineNumber(((ASTNode) annotation).getStartPosition()),1);
				elementReports.add(elementReport);
			}
			if(annotation instanceof MarkerAnnotation) {
				ElementMetric elementReport = new ElementMetric(
									((MarkerAnnotation)annotation).getTypeName().toString(),
									cu.getLineNumber(((ASTNode) annotation).getStartPosition()),0);
				elementReports.add(elementReport);
			}
			if(annotation instanceof NormalAnnotation) {
				List<MemberValuePair> arguments  = ((NormalAnnotation) annotation).values();
				ElementMetric elementReport = new ElementMetric(
						((NormalAnnotation)annotation).getTypeName().toString(),
						cu.getLineNumber(((ASTNode) annotation).getStartPosition()),arguments.size());
				elementReports.add(elementReport);
				
				//Inspecting arguments for inner annotations
				for(MemberValuePair value : arguments) {
					Expression  argArray = value.getValue();
					if(argArray instanceof ArrayInitializer) {
						for (Object memberValuePair : ((ArrayInitializer)argArray).expressions()) {
							if(memberValuePair instanceof Annotation)
								treateAnnotation((Annotation) memberValuePair, elementReports);
						}
					}else if (argArray instanceof Annotation) {
						treateAnnotation((Annotation) argArray, elementReports);
					}
				}
			}
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
