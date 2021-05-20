package com.github.phillima.asniffer.output;

import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.output.json.d3hierarchy.ProjectReport;

public interface IReport {

	public void generateReport(AMReport report, String path);

}