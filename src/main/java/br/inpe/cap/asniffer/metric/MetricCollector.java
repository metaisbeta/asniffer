package br.inpe.cap.asniffer.metric;

import org.eclipse.jdt.core.dom.CompilationUnit;

import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.Metric;

public interface MetricCollector {
	
	void execute(CompilationUnit cu, Metric result, AMReport report);
	void setResult(Metric result);
	
}
