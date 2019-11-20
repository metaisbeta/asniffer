package br.inpe.cap.asniffer;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.MetricResult;

public class UACTest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testUAC() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		Assert.assertEquals(18, a.getClassMetric("UAC"));
	
	}

}
