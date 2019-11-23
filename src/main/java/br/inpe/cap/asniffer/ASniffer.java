package br.inpe.cap.asniffer;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

public class ASniffer {
            
	private static final Logger logger = 
		      Logger.getLogger(ASniffer.class);
	
	//Called as an executable jar                                                 	                                          
	public static void main(String[] args) throws FileNotFoundException {
		                                                    
		if(args==null || args.length < 3) {
			System.out.println("Usage java -jar asniffer.jar <path to project> <path to xml report> <single/multi>");
			System.out.println("multi specifies that the directory contains multiple projects");
			System.exit(1);
		}
		
		run(args[0], args[1], args[2].toLowerCase().contains("single"));
			
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
