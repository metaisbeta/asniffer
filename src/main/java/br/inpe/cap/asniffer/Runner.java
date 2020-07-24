package br.inpe.cap.asniffer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.utils.FileUtils;

public class Runner {
	
	private String projectsPath = "";
	private String reportPath = "";
	private String reportType = ""; 
	
	private static final Logger logger = 
		      LogManager.getLogger(Runner.class);
	
	public Runner(String projectPath, String reportPath, String reportType) {
		this.projectsPath = projectPath;
		this.reportPath = reportPath;
		this.reportType = reportType;
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
		
		Object reportInstance = null;
		try {
			String classReport = FileUtils.getReportType(reportType);
			Class<?> reportClazz = Class.forName(classReport);
			reportInstance = reportClazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if(reportInstance instanceof IReport)
			((IReport) reportInstance).generateReport(report, reportPath);
		else
			System.out.println("Unable to generate report!");
		return report;
	}
}
