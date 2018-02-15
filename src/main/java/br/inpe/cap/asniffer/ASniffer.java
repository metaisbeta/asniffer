package br.inpe.cap.asniffer;

import java.io.FileNotFoundException;

public class ASniffer {
                                                               
	//Called as an executable jar                                                 	                                          
	public static void main(String[] args) throws FileNotFoundException {
		                                                    
		if(args==null || args.length < 3) {
			System.out.println("Usage java -jar asniffer.jar <path to project> <path to xml report> <single/multi>");
			System.out.println("multi specifies that the directory contains multiple projects");
			System.exit(1);
		}
		
		Runner runner = new Runner(args[0], args[1]);
		if(args[2].toLowerCase().contains("single"))
			runner.collectSingle();
		else
			runner.collectMultiple();
	}
	
	//Called from other applications
	public void run(String projectPath, String xmlPath, boolean singleProject) throws FileNotFoundException {
		Runner runner = new Runner(projectPath, xmlPath);
		if(singleProject)
			runner.collectSingle();
		else
			runner.collectMultiple();
	}
}
