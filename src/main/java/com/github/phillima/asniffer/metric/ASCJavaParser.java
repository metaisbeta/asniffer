package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.annotations.ClassMetric;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector_;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@ClassMetric
public class ASCJavaParser extends VoidVisitorAdapter<Object> implements IClassMetricCollector_ {

	List<String> imports = new ArrayList<>();
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
		
		//check if annotations was imported
		for (String import_ : imports) {
			String annotationName = annotation.getNameAsString();
			String schema = "";
			if(annotationName.contains(".")){//was not imported during usage. has fully qualified name
				schema = annotationName.substring(0,annotationName.lastIndexOf("."));
				schemasMapper.put(annotationName.substring(annotationName.lastIndexOf(".")+1) + "-" +
						annotation.getTokenRange().get().toRange().get().begin.line, schema);
				return;
			}
			if(import_.contains(annotation.getNameAsString())) {
				int lastIndex = import_.lastIndexOf(".");
				if(annotationName.equals(import_.substring(lastIndex+1))){//was imported
					schema = import_.substring(0,lastIndex);
					schemasMapper.put(annotationName + "-" +
							annotation.getTokenRange().get().toRange().get().begin.line,schema);
					return;
				}
			}
		}
		String schema = "";
		//check if it is a java lang annotation
		if(javaLangPredefined.contains(annotation.getNameAsString()))
			schema = "java.lang";
		else //if not, the annotation was declared on the package being used
			schema = cu.getPackageDeclaration().get().getNameAsString();

		schemasMapper.put(annotation.getNameAsString() + "-" +
				annotation.getTokenRange().get().toRange().get().begin.line,schema);
	}
	
	private void findImports(CompilationUnit cu) {
		for (Object import_ : cu.getImports()) {
			if(import_ instanceof ImportDeclaration && !((ImportDeclaration) import_).isStatic()) {
				imports.add(((ImportDeclaration) import_).getNameAsString());
			}
		}
	}
}	
