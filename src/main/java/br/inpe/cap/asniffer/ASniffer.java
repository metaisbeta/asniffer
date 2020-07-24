package br.inpe.cap.asniffer;

import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.inpe.cap.asniffer.parameters.ParamMapper;
import br.inpe.cap.asniffer.parameters.Parameters;


public class ASniffer {
            
	private static final Logger logger = 
			LogManager.getLogger(ASniffer.class);
	
	//Called as an executable jar                                                 	                                          
	public static void main(String[] args) throws FileNotFoundException {
		    
		if(args==null || args.length < 2) {
			System.out.println("To use ASniffer please run the "
					+ "command as following, providing four parameters:");
			System.out.println("java -jar asniffer.jar ");
			System.out.println("-p <path to project> (A complete path to where your project(s) is located)");
			System.out.println("-r <path to report> (Path where you would like to store your report. "
					+ "If no path is provided, ASniffer will place the report in your project folder.");
			System.out.println("-m <single/multi> (you have to specify single or multi. Single is the default value. Multi specifies that the directory contains multiple projects");
					System.out.println("report type can be xml or json. Please type one of them");
			System.out.println("-t <report type> (as of version 2.3.0, the report type can be xml or json. If no value is specified, an XML file will be generated");
			System.exit(1);
		}
		
		//Read the parameters
		Parameters param = new ParamMapper().map(args, Parameters.class);
		
		run(param.getProjectPath(), param.getReportPath(), 
				param.isAMultiProject(), param.getReportType());
			
	}
	
	//Called from other applications
	public static void run(String projectPath, String reportPath, boolean multiProject,
			String reportType) throws FileNotFoundException {
		Runner runner = new Runner(projectPath, reportPath,reportType);
		if(!multiProject) {
			logger.info("Initializing extraction for single project.");
			runner.collectSingle();
		}
		else {
			logger.info("Initializing extraction for multiple projects.");
			runner.collectMultiple();
		}
	}
}
