package br.inpe.cap.asniffer.metric;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.annotations.ClassMetric;
import br.inpe.cap.asniffer.interfaces.IClassMetricCollector;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.MetricResult;

@ClassMetric
public class ASC extends ASTVisitor implements IClassMetricCollector {

	List<String> imports = new ArrayList<>();
	Set<String> schemas = new HashSet<>();
	
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
	public void execute(CompilationUnit cu, MetricResult result, AMReport report) {
		findImports(cu);
		cu.accept(this);
		
		
	}

	@Override
	public void setResult(MetricResult result) {

		result.setSchemas(schemas);
		result.addClassMetric("ASC", schemas.size());
		
	}
	
	private void findSchema(Annotation annotation) {
		for (String import_ : imports) {
			if(import_.contains(annotation.getTypeName().getFullyQualifiedName())) {
				int lastIndex = import_.lastIndexOf(".");
				schemas.add(import_.substring(0,lastIndex));
			}
			else
				schemas.add("java.lang");
		}
	}
	
	private void findImports(CompilationUnit cu) {
		for (Object import_ : cu.imports()) {
			if(import_ instanceof ImportDeclaration) {
				imports.add(((ImportDeclaration) import_).getName().getFullyQualifiedName());
			}
		}
	}
}	
