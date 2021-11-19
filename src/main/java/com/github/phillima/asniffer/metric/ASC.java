package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.utils.Glossary;
import com.google.common.collect.ImmutableSet;

import java.util.*;


public class ASC extends VoidVisitorAdapter<Object> implements IClassMetricCollector {


	List<ImportDeclaration> imports = new ArrayList<>();
	HashMap<String, String> schemasMapper = new HashMap<>();
	CompilationUnit cu;

	//predefined java annotations
	private static Set<String> javaLangPredefined = ImmutableSet.of("Override","Deprecated","SuppressWarnings","SafeVarargs","FunctionalInterface");

	@Override
	public void visit(MarkerAnnotationExpr node, Object obj) {
		findSchema(node);
		super.visit(node, obj);
	}
	
	@Override
	public void visit(NormalAnnotationExpr node, Object obj) {
		findSchema(node);
		super.visit(node, obj);
	}
	
	@Override
	public void visit(SingleMemberAnnotationExpr node, Object obj) {
		findSchema(node);
		super.visit(node, obj);
	}

	@Override
	public void execute(CompilationUnit cu, ClassModel result, AMReport report) {
		findImports(cu);
		this.cu = cu;
		this.visit(cu, null);
	}

	@Override
	public void setResult(ClassModel result) {

		result.setSchemas(schemasMapper);
		result.addClassMetric("ASC", result.getAnnotationSchemas().size());
		
	}


	
	private void findSchema(AnnotationExpr annotation) {

		String qualifier = annotation.getName().getQualifier().isPresent() ? getQualifier(annotation) : "";
		int annotationLineNumber = annotation.getTokenRange().get().toRange().get().begin.line;
		String annotationName = qualifier.isEmpty() ? annotation.getNameAsString() : annotation.getName().asString().replaceFirst(qualifier.concat("\\."),"");
		String annotationNameAndLine = annotationName + "-" + annotationLineNumber;

		if (!qualifier.isEmpty()) {
			schemasMapper.put(annotationNameAndLine, qualifier);
			return;
		}
		
		//check if annotations was imported
		for (ImportDeclaration import_ : imports) {
			String annotationNameTemp = annotationName;
			if(annotationName.contains("."))//it is inner annotations declaration. THe first name should be mapped to the import
				annotationNameTemp = annotationName.substring(0,annotationName.indexOf("."));
			if (import_.getName().getIdentifier().equals(annotationNameTemp)){
				import_.getName().getQualifier().ifPresent(s -> {
					schemasMapper.put(annotationNameAndLine,s.toString());
				});
				return;
			}
		}

		// Old hard coded glossary
		//String glossarySchema = Glossary.ANNOTATION_NAME_TO_SCHEMA.get(annotationName);
		// Json glossary
		String glossarySchema = Glossary.get(annotationName);
		String schema = "";

		if (glossarySchema != null) {			
			for (ImportDeclaration importDeclaration : imports) {
				if(importDeclaration.getName().toString().equals(glossarySchema) && importDeclaration.isAsterisk()) {
					schema = glossarySchema;					
				}
			}
		} else if(javaLangPredefined.contains(annotation.getNameAsString()))
			schema = "java.lang";
		else //if not, the annotation was declared on the package being used
			schema = cu.getPackageDeclaration().get().getNameAsString();

		schemasMapper.put(annotationNameAndLine, schema);
	}

	private String getQualifier(AnnotationExpr annotation) {

		Name qualifier = annotation.getName().getQualifier().get();

		while(Character.isUpperCase(qualifier.getId().charAt(0))){
			if(qualifier.getQualifier().isPresent())
				qualifier = qualifier.getQualifier().get();
			else
				return "";
		}
		return qualifier.asString();
	}


	private void findImports(CompilationUnit cu) {
		for (ImportDeclaration import_ : cu.getImports()) {
			if(!import_.isStatic())
				imports.add(import_);
		}
	}
}