package br.inpe.cap.asniffer;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NAECTest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath);
	}
	
	@Test
	public void testNAEC() {
		Metric a = report.getByClassName("annotationtest.AnnotationTest");
		Assert.assertEquals(16, a.getClassMetric("NAEC"));
	}

}
