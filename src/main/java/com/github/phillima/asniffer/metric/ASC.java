package com.github.phillima.asniffer.metric;

import java.util.*;

import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.google.common.collect.ImmutableSet;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;


public class ASC extends ASTVisitor implements IClassMetricCollector {

	List<String> imports = new ArrayList<>();
	HashMap<String, String> schemasMapper = new HashMap<>();
	CompilationUnit cu;

	//predefined java annotations
	private static Set<String> javaLangPredefined = ImmutableSet.of("Override","Deprecated","SuppressWarnings","SafeVarargs","FunctionalInterface");

	@Override
	public boolean visit(MarkerAnnotation node) {
		findSchema(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		findSchema(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		findSchema(node);
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, ClassModel result, AMReport report) {
		findImports(cu);
		this.cu = cu;

		cu.accept(this);
	}

	@Override
	public void setResult(ClassModel result) {

		result.setSchemas(schemasMapper);
		result.addClassMetric("ASC", result.getAnnotationSchemas().size());
		
	}
	
	private void findSchema(Annotation annotation) {
		
		//check if annotations was imported
		for (String import_ : imports) {
			String annotationName = annotation.getTypeName().getFullyQualifiedName();
			String schema = "";
			if(annotationName.contains(".")){//was not imported during usage. has fully qualified name
				schema = annotationName.substring(0,annotationName.lastIndexOf("."));
				schemasMapper.put(annotationName.substring(annotationName.lastIndexOf(".")+1) + "-" +
						cu.getLineNumber(annotation.getStartPosition()),schema);
				return;
			}
			if(import_.contains(annotation.getTypeName().toString())) {
				int lastIndex = import_.lastIndexOf(".");
				if(annotationName.equals(import_.substring(lastIndex+1))){//was imported
					schema = import_.substring(0,lastIndex);
					schemasMapper.put(annotationName + "-" +
									cu.getLineNumber(annotation.getStartPosition()),schema);
					return;
				}
			}
		}
		String schema = "";
		//check if it is a java lang annotation
		if(javaLangPredefined.contains(annotation.getTypeName().getFullyQualifiedName()))
			schema = "java.lang";
		else //if not, the annotation was declared on the package being used
			schema = cu.getPackage().getName().toString();

		schemasMapper.put(annotation.getTypeName() + "-" +
						cu.getLineNumber(annotation.getStartPosition()),schema);
	}
	
	private void findImports(CompilationUnit cu) {
		for (Object import_ : cu.imports()) {
			if(import_ instanceof ImportDeclaration && !((ImportDeclaration) import_).isStatic()) {
				imports.add(((ImportDeclaration) import_).getName().getFullyQualifiedName());
			}
		}
	}
}	
