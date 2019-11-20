package br.inpe.cap.asniffer.interfaces;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import br.inpe.cap.asniffer.model.CodeElementModel;

public interface ICodeElementMetricCollector {

	public void execute(CompilationUnit cu, CodeElementModel codeElementMetricModel, BodyDeclaration codeElementNode);
	
}
