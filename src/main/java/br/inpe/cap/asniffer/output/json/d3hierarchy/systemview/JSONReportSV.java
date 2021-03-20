package br.inpe.cap.asniffer.output.json.d3hierarchy.systemview;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.collections4.map.HashedMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.ClassModel;
import br.inpe.cap.asniffer.model.PackageModel;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.json.adapter.ExcludeFieldsJSON;
import br.inpe.cap.asniffer.output.json.d3hierarchy.Children;
import br.inpe.cap.asniffer.output.json.d3hierarchy.IFetchChildren;
import br.inpe.cap.asniffer.output.json.d3hierarchy.FetchSystemViewIMP;
import br.inpe.cap.asniffer.utils.ReportTypeUtils;

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