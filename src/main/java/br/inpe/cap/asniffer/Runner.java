package br.inpe.cap.asniffer;

import java.nio.file.Path;
import java.nio.file.Paths;

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
	public void collectMultiple(){
		for (Path projectPath : FileUtils.getProjectsPath(projectsPath)) 
			collect(projectPath);
	}
	
	//project path is a directory to a single
	public void collectSingle() {
		collect(Paths.get(projectsPath));
	}
	
	private void collect(Path projectPath) {
		String projectName = FileUtils.getProjectName(projectPath);
		logger.info("Initializing extraction for project " + projectName);
		AMReport report = new AM().calculate(projectPath.toString(), projectName);
		logger.info("Extraction concluded for project " + projectName);
		IReport xmlReport = new XMLReport();
		xmlReport.generateReport(report, xmlPath);
	}
}
