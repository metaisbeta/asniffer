package com.github.phillima.asniffer.metric;

import java.util.*;

import com.github.phillima.asniffer.annotations.ClassMetric;
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

@ClassMetric
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
			if(import_.contains(annotation.getTypeName().getFullyQualifiedName())) {
				int lastIndex = import_.lastIndexOf(".");
				String annotationName = annotation.getTypeName().getFullyQualifiedName();
				if(annotationName.equals(import_.substring(lastIndex+1))) {
					schemasMapper.put(annotationName + "-" +
							cu.getLineNumber(annotation.getStartPosition())
							,import_.substring(0,lastIndex));
					return;
				}	
			}
		}
		String schema = "";
		//check if it is a java lang annotation
		if(javaLangPredefined.contains(annotation.getTypeName().getFullyQualifiedName()))
			schema = "java.lang";
		else
			schema = cu.getPackage().getName().getFullyQualifiedName().toString();
		//if not, the annotation was declared on the package being used
		schemasMapper.put(annotation.getTypeName().getFullyQualifiedName() + "-" +
						cu.getLineNumber(annotation.getStartPosition())
				,schema);
	}
	
	private void findImports(CompilationUnit cu) {
		for (Object import_ : cu.imports()) {
			if(import_ instanceof ImportDeclaration && !((ImportDeclaration) import_).isStatic()) {
				imports.add(((ImportDeclaration) import_).getName().getFullyQualifiedName());
			}
		}
	}
}	
