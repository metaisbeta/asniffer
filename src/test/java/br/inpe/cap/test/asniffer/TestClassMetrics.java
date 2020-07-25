package br.inpe.cap.test.asniffer;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.AM;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.ClassModel;

public class TestClassMetrics {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testClassMetrics() {
		
		ClassModel a = report.getPackages().get(0).getByClassName("annotationtest.AnnotationTest");
		int ac = a.getClassMetric("AC");
		int uac = a.getClassMetric("UAC");
		int asc = a.getClassMetric("ASC");
		int naec = a.getClassMetric("NAEC");
		int nec = a.getClassMetric("NEC"); 
		
		Assert.assertEquals(27, ac);
		Assert.assertEquals(17, uac);
		Assert.assertEquals(3, asc);
		Assert.assertEquals(16, naec);
		Assert.assertEquals(32, nec);
	
	}

}
