package br.inpe.cap.asniffer.output.json.d3hierarchy.packageview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;
import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.json.d3hierarchy.Children;
import br.inpe.cap.asniffer.output.json.d3hierarchy.FetchPackageViewIMP;
import br.inpe.cap.asniffer.utils.ReportTypeUtils;

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
