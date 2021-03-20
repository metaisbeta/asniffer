package br.inpe.cap.asniffer.output.json.d3hierarchy.classview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
import br.inpe.cap.asniffer.output.json.d3hierarchy.FetchClassViewIMP;
import br.inpe.cap.asniffer.output.json.d3hierarchy.packageview.ProjectReportPV;
import br.inpe.cap.asniffer.utils.ReportTypeUtils;

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