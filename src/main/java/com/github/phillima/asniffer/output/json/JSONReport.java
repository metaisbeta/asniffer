package com.github.phillima.asniffer.output.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.output.IReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONReport implements IReport {

	private static final Logger logger = 
		      LogManager.getLogger(JSONReport.class);
	@Override
	public void generateReport(AMReport report, String path) {
		
		//projectReportJson = prepareJson(report);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		Path jsonFilePath = Paths.get(path + File.separator + report.getProjectName() + ".json").normalize();
		
		String json = gson.toJson(report);
		
		try {
			FileWriter writer = new FileWriter(jsonFilePath.toString());
			writer.write(json);
			writer.close();
			logger.info("JSON file for project " + report.getProjectName() + " created on " + jsonFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
