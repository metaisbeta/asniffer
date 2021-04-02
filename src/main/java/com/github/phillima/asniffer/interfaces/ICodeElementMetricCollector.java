package com.github.phillima.asniffer.interfaces;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.phillima.asniffer.model.CodeElementModel;

public interface ICodeElementMetricCollector {

	public void execute(CompilationUnit cu, CodeElementModel codeElementMetricModel, BodyDeclaration codeElementNode);
	
}
