package com.github.phillima.asniffer.output.json.d3hierarchy.systemview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.output.IReport;
import com.github.phillima.asniffer.output.json.d3hierarchy.FetchSystemViewIMP;
import com.github.phillima.asniffer.utils.ReportTypeUtils;

public class JSONReportSV implements IReport{

	ProjectReportSystemView projectReportJson;
	
	@Override
	public void generateReport(AMReport report, String path) {
		
		projectReportJson = prepareJson(report);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Path jsonFilePath = Paths.get(path + File.separator + report.getProjectName() + "-SV.json").normalize();
		
		String json = gson.toJson(projectReportJson);
		
		try {
			FileWriter writer = new FileWriter(jsonFilePath.toString());
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private ProjectReportSystemView prepareJson(AMReport report) {
		
		ProjectReportSystemView projectReportJson =
					new ProjectReportSystemView(report.getProjectName());
		
		projectReportJson.addPackages(ReportTypeUtils.fetchPackages(report.getPackages(), new FetchSystemViewIMP()));
		
		return projectReportJson;
		
	}
	
}