package br.inpe.cap.asniffer.metric;

import org.eclipse.jdt.core.dom.CompilationUnit;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.MetricResult;

public interface MetricCollector {
	
	void execute(CompilationUnit cu, MetricResult result, AMReport report);
	void setResult(MetricResult result);
	
}
