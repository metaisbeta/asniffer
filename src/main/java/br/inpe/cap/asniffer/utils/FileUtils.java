package br.inpe.cap.asniffer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;

import br.inpe.cap.asniffer.parameters.ParameterReadingException;

public class FileUtils {
	
	public static String[] getAllDirs(String path) {
		ArrayList<String> dirs = new ArrayList<String>();
		getAllDirs(path, dirs);
		String[] ar = new String[dirs.size()];
		ar = dirs.toArray(ar);
		return ar;
	}
	
	private static void getAllDirs(String path, ArrayList<String> dirs) {
		
		File f = new File(path);
		if(f.getName().equals(".git")) return;
		
		for(File inside : f.listFiles()) {
			if(inside.isDirectory()) {
				String newDir = inside.getAbsolutePath();
				dirs.add(newDir);
				getAllDirs(newDir, dirs);
			}
		}
	}

	public static String[] getAllJavaFiles(String path) {
		ArrayList<String> files = new ArrayList<String>();
		getAllJavaFiles(path, files);
		
		String[] ar = new String[files.size()];
		ar = files.toArray(ar);
		return ar;
	}
	
	private static void getAllJavaFiles(String path, ArrayList<String> files) {
		
		File f = new File(path);
		if(f.getName().equals(".git")) return;
		
		for(File inside : f.listFiles()) {
			if(inside.isDirectory()) {
				String newDir = inside.getAbsolutePath();
				getAllJavaFiles(newDir, files);
			} else if(inside.getAbsolutePath().toLowerCase().endsWith(".java")) {
				files.add(inside.getAbsolutePath());
			}
		}
	}
	
	public static List<Path> getProjectsPath(String projectPath){
		
		List<Path> projectsPaths = new ArrayList<>();
		
		try {
			Files.list(Paths.get(projectPath))
					.filter(Files::isDirectory)
					.forEach(projectsPaths::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return projectsPaths;
		
	}

	public static String getProjectName(Path projectPath) {
		
		
		String path = projectPath.toAbsolutePath().normalize().toString();
		
		int index = path.lastIndexOf("/");
		if(index != -1)
			return path.substring(index+1);
		else
		 return path;
	}
	
	public static String[] getJarDependencies() {
		
		
		String[] jarFileNamesArray = {"-jar:org.eclipse.core.expressions-3.6.0.jar",
				"-jar:org.eclipse.equinox.app-1.3.400.jar",
				"-jar:fast-classpath-scanner-2.18.0.jar",
				"-jar:org.eclipse.core.filesystem-1.7.0.jar",
				"-jar:junit-4.12.jar",
				"-jar:guava-18.0.jar",
				"-jar:org.eclipse.core.contenttype-3.6.0.jar",
				"-jar:org.eclipse.equinox.common-3.9.0.jar",
				"-jar:org.eclipse.equinox.preferences-3.7.0.jar",
				"-jar:org.eclipse.jdt.core-3.12.2.jar",
				"-jar:org.eclipse.core.resources-3.12.0.jar",
				"-jar:org.eclipse.text-3.6.100.jar",
				"-jar:hamcrest-core-1.3.jar",
				"-jar:org.eclipse.equinox.registry-3.7.0.jar",
				"-jar:org.eclipse.core.runtime-3.13.0.jar",
				"-jar:commons-lang3-3.0.jar",
				"-jar:org.eclipse.osgi-3.12.50.jar",
				"-jar:org.eclipse.core.commands-3.9.0.jar",
				"-jar:org.eclipse.core.jobs-3.9.2.jar"};
		
		return jarFileNamesArray;
	}
	
	public static String getFileAsString(String path) {
		
	   	StringBuilder contentBuilder = new StringBuilder();
	    try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) 
	    {
	        stream.forEach(s -> contentBuilder.append(s).append("\n"));
	    }
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}
	
	
	
	
}
