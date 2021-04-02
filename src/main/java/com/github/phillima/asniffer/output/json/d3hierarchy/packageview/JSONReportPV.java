package com.github.phillima.asniffer.output.json.d3hierarchy.packageview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.output.IReport;
import com.github.phillima.asniffer.output.json.d3hierarchy.FetchPackageViewIMP;
import com.github.phillima.asniffer.utils.ReportTypeUtils;

public class JSONReportPV implements IReport {

	
	ProjectReportPV projectReportJson;
	
	@Override
	public void generateReport(AMReport report, String path) {
		
		
		projectReportJson = prepareJson(report);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Path jsonFilePath = Paths.get(path + File.separator + report.getProjectName() + "-PV.json").normalize();
		
		String json = gson.toJson(projectReportJson);
		
		try {
			FileWriter writer = new FileWriter(jsonFilePath.toString());
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private ProjectReportPV prepareJson(AMReport report) {
	
		ProjectReportPV projectReport = new ProjectReportPV(report.getProjectName());
		
		projectReport.addPackages(ReportTypeUtils.fetchPackages(report.getPackages(), new FetchPackageViewIMP()));
		
		return projectReport;
		
	}
	

}
