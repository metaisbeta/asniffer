package com.github.phillima.asniffer.interfaces;

import com.github.phillima.asniffer.model.AMReport;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.phillima.asniffer.model.ClassModel;

public interface IClassMetricCollector  {
	
	void execute(CompilationUnit cu, ClassModel result, AMReport report);
	void setResult(ClassModel result);
	
}
