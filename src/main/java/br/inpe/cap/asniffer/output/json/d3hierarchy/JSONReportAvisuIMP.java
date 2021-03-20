package br.inpe.cap.asniffer.output.json.d3hierarchy;

import java.io.File;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.json.d3hierarchy.classview.JSONReportCV;
import br.inpe.cap.asniffer.output.json.d3hierarchy.packageview.JSONReportPV;
import br.inpe.cap.asniffer.output.json.d3hierarchy.systemview.JSONReportSV;

public class JSONReportAvisuIMP implements IReport {

	@Override
	public void generateReport(AMReport report, String path) {
		
		String dirPathResults = path + File.separator + "asniffer_results";
		
		new File(dirPathResults).mkdir();
		
		new JSONReportCV().generateReport(report, dirPathResults);
		new JSONReportSV().generateReport(report, dirPathResults);
		new JSONReportPV().generateReport(report, dirPathResults);
		
	}

}