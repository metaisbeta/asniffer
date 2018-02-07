package br.inpe.cap.asniffer;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import br.inpe.cap.asniffer.utils.FileUtils;
import br.inpe.cap.asniffer.utils.XMLUtils;

public class Runner {
	
	String projectsPath = "";
	String xmlPath = "";
	
	public Runner(String projectPath, String xmlPath) {
		this.projectsPath = projectPath;
		this.xmlPath = xmlPath;
	}
	
	public void collect() throws FileNotFoundException {
		
		for (Path projectPath : FileUtils.getProjectsPath(projectsPath)) {
			String projectName = FileUtils.getProjectName(projectPath);
			AMReport report = new AM().calculate(projectPath.toString(), projectName);
			XMLUtils.createXMLFile(report, xmlPath);
		}
	}
}
