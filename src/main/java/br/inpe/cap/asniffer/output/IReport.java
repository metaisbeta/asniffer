package br.inpe.cap.asniffer.output;

import br.inpe.cap.asniffer.model.AMReport;

public interface IReport {

	public void generateReport(AMReport report, String path);
	
}
