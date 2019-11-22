package br.inpe.cap.asniffer;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.MetricResult;

public class TestClassMetrics {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testClassMetrics() {
		
		MetricResult a = report.getPackages().get(0).getByClassName("annotationtest.AnnotationTest");
		int ac = a.getClassMetric("AC");
		int uac = a.getClassMetric("UAC");
		int asc = a.getClassMetric("ASC");
		int naec = a.getClassMetric("NAEC");
		int nec = a.getClassMetric("NEC"); 
		
		Assert.assertEquals(28, ac);
		Assert.assertEquals(18, uac);
		Assert.assertEquals(5, asc);
		Assert.assertEquals(16, naec);
		Assert.assertEquals(32, nec);
	
	}

}
