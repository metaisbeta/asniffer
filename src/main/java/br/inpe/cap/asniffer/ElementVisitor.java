package br.inpe.cap.asniffer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ElementVisitor extends ASTVisitor {
	
	List<BodyDeclaration> elementsAnnotation = new ArrayList<>();
	
	public List<BodyDeclaration> getElementsAnnotation() {
		return this.elementsAnnotation;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		checkForAnnotations(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		checkForAnnotations(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		checkForAnnotations(node);
		return super.visit(node);
	}
	
	/*
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		System.out.println(node.getName());
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		System.out.println(node.getName());
		return super.visit(node);
	}*/
	
	//Inner helper methods
		private void checkForAnnotations(BodyDeclaration node) {
			for (Object obj : node.modifiers()) {
				if(obj instanceof Annotation) { 
					elementsAnnotation.add(node);//There is at least one annotation, no need to check further
					return;
				}
			}
		}

}
