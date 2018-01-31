package br.inpe.cap.asniffer;

import java.io.FileNotFoundException;

public class Runner {

public static void main(String[] args) throws FileNotFoundException {
		
		if(args==null || args.length < 2) {
			System.out.println("Usage java -jar asniffer.jar <path to project> <path to xml>");
			System.exit(1);
		}
		
		String path = args[0];
		String xmlPath = args[1];
	
		AMReport report = new AM().calculate(path);
		
		//TODO XML file creation
		//TODO MAP metrics to be XML file created
	}
}
