package br.inpe.cap.asniffer;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ASCTest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM(null).calculate(testFilePath, "project");
	}
	
	@Test
	public void testASC() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		Assert.assertEquals(2, a.getClassMetric("ASC"));
	
	}

}

