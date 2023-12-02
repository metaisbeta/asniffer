package com.github.phillima.asniffer.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class FileUtils {
	
	private FileUtils() { }
	
	
	public static String[] getAllDirs(String path) {
		List<String> dirs = new ArrayList<String>();
		getAllDirs(path, dirs);
		String[] ar = new String[dirs.size()];
		ar = dirs.toArray(ar);
		return ar;
	}
	
	public static void getAllDirs(String path, List<String> dirs) {
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
		List<String> files = new ArrayList<String>();
		getAllJavaFiles(path, files);
		
		String[] ar = new String[files.size()];
		ar = files.toArray(ar);
		return ar;
	}
	
	public static void getAllJavaFiles(String path, List<String> files) {
		File f = new File(path);
		if(f.isHidden()) return;

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
		return String.valueOf(projectPath.getFileName());
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
