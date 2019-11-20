package br.inpe.cap.asniffer.metric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.annotations.AnnotationMetric;
import br.inpe.cap.asniffer.annotations.ClassMetric;
import br.inpe.cap.asniffer.interfaces.IClassMetricCollector;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.MetricResult;

@ClassMetric
public class ASC extends ASTVisitor implements IClassMetricCollector {

	Map<String,String> annotations = new HashMap<>();
	Set<String> aSchemas = new HashSet<>();
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		annotations.put(node.getTypeName().getFullyQualifiedName(), null);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		annotations.put(node.getTypeName().getFullyQualifiedName(), null);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		annotations.put(node.getTypeName().getFullyQualifiedName(), null);
		return super.visit(node);
	}
	
	@Override
	public void execute(CompilationUnit cu, MetricResult result, AMReport report) {
		cu.accept(this);
		addSchemas(cu);
		
	}

	@Override
	public void setResult(MetricResult result) {
		result.addClassMetric("ASC", aSchemas.size());
		
	}
	
	private void addSchemas(CompilationUnit cu) {
		for (Object importName : cu.imports()) {
			int endIndex = importName.toString().lastIndexOf(";");
			int beginIndex = importName.toString().lastIndexOf(".");
			String lastNameInImport = importName.toString().substring(beginIndex+1,endIndex);
			
			if(annotations.containsKey(lastNameInImport))
				aSchemas.add(importName.toString().replaceAll("import ", "").replaceFirst("."+lastNameInImport+";", ""));
		}
	}

	
}	
