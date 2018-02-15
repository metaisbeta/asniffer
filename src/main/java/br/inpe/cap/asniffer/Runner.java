package br.inpe.cap.asniffer;

import java.nio.file.Path;
import java.nio.file.Paths;

import br.inpe.cap.asniffer.utils.FileUtils;
import br.inpe.cap.asniffer.utils.XMLUtils;

public class Runner {
	
	String projectsPath = "";
	String xmlPath = "";
	
	public Runner(String projectPath, String xmlPath) {
		this.projectsPath = projectPath;
		this.xmlPath = xmlPath;
	}
	
	//project path is a root directory to multiple project directories
	public void collectMultiple(){
		for (Path projectPath : FileUtils.getProjectsPath(projectsPath)) 
			collect(projectPath);
	}
	
	//project path is a directory to a single
	public void collectSingle() {
		collect(Paths.get(projectsPath));
	}
	
	private void collect(Path projectPath) {
		String projectName = FileUtils.getProjectName(projectPath);
		System.out.println("Initializing extraction for project " + projectName);
		AMReport report = new AM().calculate(projectPath.toString(), projectName);
		XMLUtils.createXMLFile(report, xmlPath);
	}
}
