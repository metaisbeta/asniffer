package com.github.phillima.asniffer.output.json.d3hierarchy.classview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.output.IReport;
import com.github.phillima.asniffer.output.json.d3hierarchy.FetchClassViewIMP;
import com.github.phillima.asniffer.utils.ReportTypeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONReportCV implements IReport {
	
	private ProjectReportCV projectReport;

	@Override
	public void generateReport(AMReport report, String path) {
		
		projectReport = prepareJson(report);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		Path jsonFilePath = Paths.get(path + File.separator + report.getProjectName() + "-CV.json").normalize();
		
		String json = gson.toJson(projectReport);
		
		try {
			FileWriter writer = new FileWriter(jsonFilePath.toString());
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ProjectReportCV prepareJson(AMReport report) {
		
		ProjectReportCV projectReport = new ProjectReportCV(report.getProjectName());
		
		projectReport.addPackages(ReportTypeUtils.
				fetchPackages(report.getPackages(), new FetchClassViewIMP()));
		
		return projectReport;
	}

}