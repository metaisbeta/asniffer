package br.inpe.cap.asniffer.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GenerateCSV {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	public boolean writeCSV(String projectName, String fileName, String path, List<String> metricValues, int numberElementsLine){
		FileWriter fileWriter = null;
		Path projectDir = Paths.get(path + projectName);
		if (!Files.exists(projectDir)) {
            try {
                Files.createDirectories(projectDir);
            } catch (IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
        }
		File file = new File(projectDir.toFile(),fileName);
		//Create a folder for project
		new File(path).mkdir();
		try {
			fileWriter = new FileWriter(file);

			for(int i = 0; i < metricValues.size(); i++){
				fileWriter.append(metricValues.get(i));
				if(((i+1)%numberElementsLine == 0) && i != 0){//Add new line at every numberElemetsLine
					fileWriter.append(NEW_LINE_SEPARATOR);
					continue;
				}if(i < metricValues.size() - 1)
					fileWriter.append(COMMA_DELIMITER);
			}
			//Properly end the csv line
			fileWriter.append(NEW_LINE_SEPARATOR);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
				return true;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		return false;

	}
}
