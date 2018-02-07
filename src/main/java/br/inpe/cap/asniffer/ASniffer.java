package br.inpe.cap.asniffer;

import java.io.File;
import java.io.FileNotFoundException;

public class ASniffer {
                                                               
	//Called as an executable jar                                                 	                                          
	public static void main(String[] args) {
		                                                    
		if(args==null || args.length < 2) {
			System.out.println("Usage java -jar asniffer.jar <path to project> <path to xml report> <path to xml configuration file>");
			System.out.println("<path to xml configuration file> is optional");
			System.exit(1);
		}
		
		Runner runner = new Runner(args[0], args[1]);
		try {
			runner.collect();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Called from other applications
	public void run(String projectPath, String xmlPath) {
		Runner runner = new Runner(projectPath, xmlPath);
		try {
			runner.collect();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
