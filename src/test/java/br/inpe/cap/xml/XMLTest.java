package br.inpe.cap.xml;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.AM;
import br.inpe.cap.asniffer.AMReport;
import br.inpe.cap.asniffer.utils.XMLUtils;

public class XMLTest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
		
	@Test	
	public void firstTest() {
		XMLUtils.createXMLFile(report, "annotationtest");
		assertTrue(true);
	}
	
}
