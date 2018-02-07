package br.inpe.cap.asniffer.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		
		String path = projectPath.toString();
		int index = path.lastIndexOf("/");
		if(index != -1)
			return path.substring(index+1);
		return null;
	}
	
	public static String[] getJarDependencies(String dependeciesListPath) {
		
		File f = new File(dependeciesListPath);
		List<String> jarFileNames = new ArrayList<>();
		Scanner inputLines = null;
		try {
			inputLines = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(inputLines.hasNextLine())
			jarFileNames.add(inputLines.nextLine());
		
		String[] jarFileNamesArray = new String[jarFileNames.size()];
		jarFileNamesArray = jarFileNames.toArray(jarFileNamesArray);
		
		return jarFileNamesArray;
	}
	
}
