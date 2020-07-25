package br.inpe.cap.asniffer.interfaces;

import org.eclipse.jdt.core.dom.CompilationUnit;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.ClassModel;

public interface IClassMetricCollector  {
	
	void execute(CompilationUnit cu, ClassModel result, AMReport report);
	void setResult(ClassModel result);
	
}
