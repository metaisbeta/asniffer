package br.inpe.cap.asniffer;

import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.inpe.cap.asniffer.model.AMReport;


public class ASniffer {
            
	private static final Logger logger = 
			LogManager.getLogger(ASniffer.class);
	
	//Called as an executable jar                                                 	                                          
	public static void main(String[] args) throws FileNotFoundException {
		                                                    
		
		String pathProject = null, pathXML = null;
		boolean single = true;
		
		if(args==null || args.length < 2) {
			System.out.println("To use ASniffer please run the "
					+ "command as following, providing three parameters:");
			System.out.println("Usage java -jar asniffer.jar <path to project> <path to xml report> <single/multi>");
			System.out.println("multi specifies that the directory contains multiple projects");
			System.out.println("If no <path to xml> is provided, the ASniffer will place the report on the <path to project>");
			System.exit(1);
		}
		
		if(args.length >= 2) {
			pathProject = args[0];
			if(args.length > 2) {
				pathXML = args[1];
				single = args[2].toLowerCase().contains("single");
			}else {
				pathXML = args[0];
				single = args[1].toLowerCase().contains("single");
			}
			
		}
		
		run(pathProject, pathXML, single);
			
	}
	
	//Called from other applications
	public static void run(String projectPath, String xmlPath, boolean singleProject) throws FileNotFoundException {
		Runner runner = new Runner(projectPath, xmlPath);
		if(singleProject) {
			logger.info("Initializing extraction for single project.");
			runner.collectSingle();
		}
		else {
			logger.info("Initializing extraction for multiple projects.");
			runner.collectMultiple();
		}
	}
}
