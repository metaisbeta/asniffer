package br.inpe.cap.xml;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


import br.inpe.cap.asniffer.AM;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.output.IReport;
import br.inpe.cap.asniffer.output.xml.XMLReport;

public class XMLTest {

	private static AMReport report;
	private static IReport outputProcessor;
	static String testFilePath;
	
	//@BeforeClass
	public static void setUp() {
		testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
		outputProcessor = new XMLReport();
	}
		
	//@Test	
	public void firstTest() {
		outputProcessor.generateReport(report, "annotationtest");
		
		String expected = null, actual = null;
		try {
			expected = new String(Files.readAllBytes(Paths.get(testFilePath + 
													File.separatorChar + "expected.xml")), StandardCharsets.UTF_8);
			actual = new String(Files.readAllBytes(Paths.get(testFilePath + 
													File.separatorChar +  "project.xml")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(expected,actual);
	}
	
}
