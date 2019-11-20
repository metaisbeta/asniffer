package br.inpe.cap.asniffer.interfaces;

import org.eclipse.jdt.core.dom.CompilationUnit;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.MetricResult;

public interface IClassMetricCollector  {
	
	void execute(CompilationUnit cu, MetricResult result, AMReport report);
	void setResult(MetricResult result);
	
}
