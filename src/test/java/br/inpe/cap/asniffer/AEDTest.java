package br.inpe.cap.asniffer;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AEDTest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report =  new AM().calculate(testFilePath);
	}
	
	@Test
	public void testAED() {
		
		Metric a = report.getByClassName("annotationtest.AnnotationTest");
		Assert.assertEquals(2, a.getClassMetric("ASC"));
	
	}
	
}
