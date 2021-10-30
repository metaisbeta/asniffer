package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.google.common.collect.ImmutableSet;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

		String qualifier = getQualifier(annotation);
		int annotationLineNumber = annotation.getTokenRange().get().toRange().get().begin.line;
		String annotationName = annotation.getName().getIdentifier();
		String annotationNameAndLine = annotationName + "-" + annotationLineNumber;

		if (!qualifier.isEmpty()) {
			//String schema = annotation.getName().getQualifier().get().asString().substring(0,isInlineQualified);
			schemasMapper.put(annotationNameAndLine, qualifier);
			return;
		}
		
		//check if annotations was imported
		for (ImportDeclaration import_ : imports) {
			String schema = "";
			if (import_.getName().getIdentifier().equals(annotationName)){
				import_.getName().getQualifier().ifPresent(s -> {
					schemasMapper.put(annotationNameAndLine,s.toString());
				});
				return;
			}
		}

		String schema = "";
		//check if it is a java lang annotation
		if(javaLangPredefined.contains(annotation.getNameAsString()))
			schema = "java.lang";
		else //if not, the annotation was declared on the package being used
			schema = cu.getPackageDeclaration().get().getNameAsString();

		schemasMapper.put(annotationNameAndLine, schema);
	}

	private String getQualifier(AnnotationExpr annotation) {

		String qualifier = "";

		if(annotation.getName().getQualifier().isPresent()){
			Name tempLastWordOfQualifier = annotation.getName().getQualifier().get();
			while(Character.isUpperCase(tempLastWordOfQualifier.getIdentifier().charAt(0))){
				tempLastWordOfQualifier = tempLastWordOfQualifier.getQualifier().orElseThrow();
			}
			qualifier = tempLastWordOfQualifier.toString();
		}
		return qualifier;

	}


	private void findImports(CompilationUnit cu) {
		for (ImportDeclaration import_ : cu.getImports()) {
			if(!import_.isStatic())
				imports.add(import_);
		}
	}
}