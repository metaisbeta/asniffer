package com.github.phillima.asniffer.output;

import com.github.phillima.asniffer.model.AMReport;

public interface IReport {

	public void generateReport(AMReport report, String path);
	
}
