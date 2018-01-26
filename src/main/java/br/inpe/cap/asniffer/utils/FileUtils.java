package br.inpe.cap.asniffer.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

	
	public static String readFileAsString (String filePath)
	{
	    StringBuilder contentBuilder = new StringBuilder();
	    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
	    {
	        stream.forEach(s -> contentBuilder.append(s).append("\n"));
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}
	
	public static String readFileAsString (Path filePath)
	{
	    return readFileAsString(filePath.toString());
	}
	
	
	public static List<Path> findFiles(String path) {
		
		List<Path> files = new ArrayList<Path>();
		try {
			Files.list(Paths.get(path))
	             .forEach(files::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return files;
	}
	
	public static List<Path> findFiles(Path path) {
			
		return findFiles(path.toString());
	}

	public static List<Path> listProjects(String path) {
		List<Path> subfolder = new ArrayList<>();
		try {
			Files.list(Paths.get(path))
			        .filter(Files::isDirectory)
			        .forEach(subfolder::add);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return subfolder;
	}
	
}
