package com.github.phillima.asniffer.metric;

import com.github.phillima.asniffer.annotations.CodeElementMetric;
import com.github.phillima.asniffer.interfaces.ICodeElementMetricCollector;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.utils.AnnotationUtils;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

@CodeElementMetric
public class AED implements ICodeElementMetricCollector {

	@Override
	public void execute(CompilationUnit cu, CodeElementModel codeElementMetricModel, BodyDeclaration codeElementNode) {
		
		int aed = AnnotationUtils.checkForAnnotations(codeElementNode).size();
		
		codeElementMetricModel.setAed(aed);
		
	}
}
