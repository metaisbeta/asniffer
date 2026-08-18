package com.github.phillima.asniffer;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.phillima.asniffer.filter.AnnotationFilter;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.output.IReport;
import com.github.phillima.asniffer.parameters.Parameters;
import com.github.phillima.asniffer.utils.FileUtils;
import com.github.phillima.asniffer.utils.ReportTypeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ASniffer {

	private String projectsPath = "";
	private String reportPath = "";
	private IReport reportType;
	private String filterPath = "";

	private static final Logger logger =
			LogManager.getLogger(ASniffer.class);

	public ASniffer(String projectPath, String reportPath, IReport reportType) {
		this.projectsPath = projectPath;
		this.reportPath = reportPath;
		this.reportType = reportType;
	}

	public ASniffer(String projectPath, String reportPath) {
		this.projectsPath = projectPath;
		this.reportPath = reportPath;
		this.reportType = ReportTypeUtils.getReportInstance(Parameters.DEFAULT_PROJECT_REPORT);
	}

	public ASniffer(String projectsPath, String reportPath, IReport reportType, String filterPath) {
		this(projectsPath, reportPath, reportType);
		this.filterPath = filterPath;
	}

	//project path is a root directory to multiple project directories
	public List<AMReport> collectMultiple(boolean writeOutput) {
		List<AMReport> reports = new ArrayList<AMReport>();
		for (Path projectPath : FileUtils.getProjectsPath(projectsPath)) {
			reports.add(collect(projectPath, writeOutput));
		}
		return reports;
	}

	//project path is a directory to a single
	public AMReport collectSingle(boolean writeOutput) {
        return collect(Paths.get(projectsPath), writeOutput);
	}

	private AMReport collect(Path projectPath, boolean writeOutput) {

		StaticJavaParser.setConfiguration(
				StaticJavaParser.getConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17_PREVIEW)
		);

		AnnotationFilter filter = AnnotationFilter.fromFile(Path.of(filterPath));

		String projectName = FileUtils.getProjectName(projectPath);
		logger.info("Initializing extraction for project " + projectName);
		AMReport report = AmFactory.createAm(projectPath.toString(), projectName, filter).calculate();
		logger.info("Extraction concluded for project " + projectName);

		if(writeOutput) generateOutput(report);
		return report;
	}

	private void generateOutput(AMReport report) {
		reportType.generateReport(report, reportPath);
	}
}