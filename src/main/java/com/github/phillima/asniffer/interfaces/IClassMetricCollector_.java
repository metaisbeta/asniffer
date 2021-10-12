package com.github.phillima.asniffer.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;


public interface IClassMetricCollector_ {
	
	void execute(CompilationUnit cu, ClassModel result, AMReport report);
	void setResult(ClassModel result);
	
}
