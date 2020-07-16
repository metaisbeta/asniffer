package br.inpe.cap.asniffer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.xml.XMLReport;
import br.inpe.cap.asniffer.utils.FileUtils;

public class Runner {
	
	String projectsPath = "";
	String xmlPath = "";
	
	private static final Logger logger = 
		      LogManager.getLogger(Runner.class);
	
	public Runner(String projectPath, String xmlPath) {
		this.projectsPath = projectPath;
		this.xmlPath = xmlPath;
	}
	
	//project path is a root directory to multiple project directories
	public List<AMReport> collectMultiple(){
		List<AMReport> reports = new ArrayList<AMReport>();
		for (Path projectPath : FileUtils.getProjectsPath(projectsPath)) 
			reports.add(collect(projectPath));
		return reports;
	}
	
	//project path is a directory to a single
	public AMReport collectSingle() {
		return collect(Paths.get(projectsPath));
	}
	
	private AMReport collect(Path projectPath) {
		String projectName = FileUtils.getProjectName(projectPath);
		logger.info("Initializing extraction for project " + projectName);
		AMReport report = new AM().calculate(projectPath.toString(), projectName);
		logger.info("Extraction concluded for project " + projectName);
		//IReport jsonReport = new JSONReport();
		//jsonReport.generateReport(report, xmlPath);
		IReport xmlReport = new XMLReport();
		xmlReport.generateReport(report, xmlPath);
		return report;
	}
}
