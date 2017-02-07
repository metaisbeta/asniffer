package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.inpe.cap.asniffer.output.ClassRepresentation;
import br.inpe.cap.asniffer.output.MetricRepresentation;
import br.inpe.cap.asniffer.output.OutputRepresentation;
import br.inpe.cap.asniffer.output.PackageRepresentation;
import br.inpe.cap.asniffer.util.CSVUtils;

public class CSVTest {
	
	final int NUM_PACKAGE = 3;
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	@Test
	public void testCSV_AC(){
		
		char acValues[] = {'0','0','0','3','8','5','0'} ;
		
		
		
		String cwd = new File("").getAbsolutePath();
		
		FileWriter fileWriter = null;
		Path path = Paths.get(cwd + File.separator + "CSV");
		if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);//Creates CSV dir
            } catch (IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
        }
		
		File expected = new File(path.toFile(),"expectedAC.csv");
		//File returned = new File(path.toFile(), "returnedAC.csv");
		
		try {
			fileWriter = new FileWriter(expected);
			for(int i = 0; i < acValues.length; i++){
				fileWriter.append(acValues[i]);
				fileWriter.append(COMMA_DELIMITER);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}

		
		
		
		//List<MetricRepresentation> metricRep = fetchMetricRepresentation(acValues, "AC", "Annotations in Class");
		//CSVUtils.writeCSV_DATA(metricRep);
		
	}

	@Test
	public void testCSV_LOCAD(){
		
	}
	
	//Inner helper methods
	private List<MetricRepresentation> fetchMetricRepresentation(int[] metricValues, String alias, 
											String name) {
		
		List<MetricRepresentation> metricRep = new ArrayList<>();
		
		for(int i = 0; i < metricValues.length; i++)
			metricRep.add(new MetricRepresentation(alias, name, metricValues[i]));
		
		return metricRep;
	}
}
