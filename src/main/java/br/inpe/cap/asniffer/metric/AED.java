package br.inpe.cap.asniffer.metric;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

import br.inpe.cap.asniffer.annotations.CodeElementMetric;
import br.inpe.cap.asniffer.interfaces.ICodeElementMetricCollector;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.utils.AnnotationUtils;

@CodeElementMetric
public class AED implements ICodeElementMetricCollector{

	@Override
	public void execute(CompilationUnit cu, CodeElementModel codeElementMetricModel, BodyDeclaration codeElementNode) {
		
		int aed = AnnotationUtils.checkForAnnotations(codeElementNode).size();
		
		codeElementMetricModel.setAed(aed);
		
	}
}
