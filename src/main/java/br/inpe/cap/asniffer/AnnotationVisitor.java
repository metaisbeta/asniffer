package br.inpe.cap.asniffer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class AnnotationVisitor extends ASTVisitor {
	
	private List<NormalAnnotation> normalAnnotations = new ArrayList<NormalAnnotation>();
	private List<MarkerAnnotation> markerAnnotations = new ArrayList<MarkerAnnotation>();
	private List<SingleMemberAnnotation> singleAnnotations = new ArrayList<SingleMemberAnnotation>();
	private List<Annotation> allAnnotations = new ArrayList<Annotation>();
	
	@Override
	public boolean visit(NormalAnnotation node) {
		allAnnotations.add(node);
		normalAnnotations.add(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		allAnnotations.add(node);
		markerAnnotations.add(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		allAnnotations.add(node);
		singleAnnotations.add(node);
		return super.visit(node);
	}
	
	
	//GETTERS
	public List<NormalAnnotation> getNormalAnnotations() {
		return normalAnnotations;
	}
	
	public List<MarkerAnnotation> getMarkerAnnotations() {
		return markerAnnotations;
	}
	
	public List<SingleMemberAnnotation> getSingleAnnotations() {
		return singleAnnotations;
	}
	
	public List<Annotation> getAllAnnotations() {
		return allAnnotations;
	}
	
}
