package br.inpe.cap.asniffer;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import br.inpe.cap.asniffer.utils.FileUtils;
import br.inpe.cap.asniffer.utils.XMLUtils;

public class Runner {

public static void main(String[] args) throws FileNotFoundException {
		
		if(args==null || args.length < 2) {
			System.out.println("Usage java -jar asniffer.jar <path to project> <path to xml>");
			System.exit(1);
		}
		String projectsPath = args[0];
		String xmlPath = args[1];
		
		//String testPath = "/Users/phillima/Documents/teste_back";
		for (Path projectPath : FileUtils.getProjectsPath(projectsPath)) {
			String projectName = FileUtils.getProjectName(projectPath);
			AMReport report = new AM().calculate(projectPath.toString(), projectName);
			XMLUtils.createXMLFile(report, xmlPath);
		}
	}
}
