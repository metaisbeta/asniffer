package com.github.phillima.asniffer;

import java.io.FileNotFoundException;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.phillima.asniffer.output.IReport;
import com.github.phillima.asniffer.parameters.ParamMapper;
import com.github.phillima.asniffer.parameters.Parameters;
import com.github.phillima.asniffer.utils.ReportTypeUtils;

@SuppressWarnings("PMD")
public class Main {

	private static final Logger logger =
			LogManager.getLogger(Main.class);

	//Called as an executable jar                                                 	                                          
	public static void main(String[] args) throws FileNotFoundException {
		LocalTime start = LocalTime.now();

		ifInvalidArgsPrintHowToUseAndExit(args);

		//Read the parameters
		Parameters param = new ParamMapper().map(args, Parameters.class);

		run(
				param.getProjectPath(),
				param.getReportPath(),
				param.isAMultiProject(),
				ReportTypeUtils.getReportInstance(param.getReportType())
		);

		LocalTime finish = LocalTime.now();
		LocalTime diff = finish.minusNanos(start.toNanoOfDay());

		System.out.println("Execution time: " + diff);
	}

	public static void run(String projectPath, String reportPath, boolean multiProject,
						   IReport reportType) throws FileNotFoundException {

		ASniffer runner = new ASniffer(projectPath, reportPath, reportType);
		if(!multiProject) {
			logger.info("Initializing extraction for single project.");
			runner.collectSingle();
		}
		else {
			logger.info("Initializing extraction for multiple projects.");
			runner.collectMultiple();
		}

	}

	public static void run(String projectPath, String reportPath) throws FileNotFoundException {
		run(projectPath,reportPath,false,ReportTypeUtils.getReportInstance(Parameters.DEFAULT_PROJECT_REPORT));
	}

	public static void run(String projectPath) throws FileNotFoundException {
		run(projectPath,projectPath);
	}


	private static void ifInvalidArgsPrintHowToUseAndExit(String[] args) {
		if(args ==null || args.length < 2) {
			System.out.println("To use ASniffer please run the "
					+ "command as following, providing four parameters:");
			System.out.println("java -jar asniffer.jar ");
			System.out.println("-p <path to project> (A complete path to where your project(s) is located)");
			System.out.println("-r <path to report> (Path where you would like to store your report. "
					+ "If no path is provided, ASniffer will place the report in your project folder.");
			System.out.println("-m <single/multi> (you have to specify single or multi. Single is the default value. Multi specifies that the directory contains multiple projects");
			System.out.println("-t <report type> (the report type can be variations of json. If no value is specified, a default json file will be generated. README file contains more details about the different json reports.");
			System.exit(1);
		}
	}


}
