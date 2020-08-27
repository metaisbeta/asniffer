package br.inpe.cap.test.asniffer;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.AM;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.ClassModel;

public class TestClassMetrics {

	private static AMReport report;
	private static ClassModel a;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
		a = report.getPackages().get(0).getByClassName("annotationtest.AnnotationTest");
	}
	
	@Test
	public void testAC() {
		int ac = a.getClassMetric("AC");
		Assert.assertEquals(27, ac);
	}
	
	@Test
	public void testUac() {
		int uac = a.getClassMetric("UAC");
		Assert.assertEquals(17, uac);
	}
	
	@Test
	public void testASC() {
		int asc = a.getClassMetric("ASC");
		Assert.assertEquals(3, asc);
	}
	
	@Test
	public void testNAEC() {
		int naec = a.getClassMetric("NAEC");
		Assert.assertEquals(16, naec);
	}
	
	@Test
	public void testNEC() {
		int nec = a.getClassMetric("NEC"); 
		Assert.assertEquals(32, nec);
	}

}
