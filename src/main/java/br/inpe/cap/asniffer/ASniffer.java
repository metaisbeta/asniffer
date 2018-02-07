package br.inpe.cap.asniffer;

import java.io.File;
import java.io.FileNotFoundException;

public class ASniffer {
                                                               
	//Called as an executable jar                                                 	                                          
	public static void main(String[] args) {
		                                                    
		String userConfigXml = null;
		if(args==null || args.length < 2) {
			System.out.println("Usage java -jar asniffer.jar <path to project> <path to xml report> <path to xml configuration file>");
			System.out.println("<path to xml configuration file> is optional");
			System.exit(1);
		}
		
		if(args.length < 3) 
			System.out.println("No user xml configuration file supplied. Only default metrics will be collected. To supply user configuration run the program as: "
					+ "java -jar asniffer.jar <path to project> "
					+ "<path to xml report> "
					+ "<path to xml configuration file>");
		else {
			userConfigXml = args[2];
			testFile(userConfigXml);
			
		}
		Runner runner = new Runner(args[0], args[1], userConfigXml);
		try {
			runner.collect();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Called from other applications
	public void run(String projectPath, String xmlPath, String userConfigXML) {
		testFile(userConfigXML);
		Runner runner = new Runner(projectPath, xmlPath, userConfigXML);
		try {
			runner.collect();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void run(String projectPath, String xmlPath) {
		run(projectPath, xmlPath, null);
	}
	
	private static void testFile(String userConfigXml) {
		if(!new File(userConfigXml).exists()) {
			System.out.println("XML configuration file not found");
			System.out.println("Supplied path: " + userConfigXml);
			System.exit(1);
		}
	}

}
