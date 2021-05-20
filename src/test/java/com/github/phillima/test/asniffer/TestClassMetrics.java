package com.github.phillima.test.asniffer;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.phillima.asniffer.AM;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;

public class TestClassMetrics {

	private static AMReport report;
	private static ClassModel a;
	
	//Fixture
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";

		report = new AM().calculate(testFilePath, "project");
		a = report.getPackages().get(0).getClassModel("annotationtest.AnnotationTest");
		System.out.println(a.getFullyQualifiedName());
		
	}
	
	@Test
	public void testAC() {
		int ac = a.getClassMetric("AC");
		Assert.assertEquals(35, ac);
	}
	
	@Test
	public void testUac() {
		int uac = a.getClassMetric("UAC");
		Assert.assertEquals(25, uac);
	}
	
	@Test
	public void testASC() {
		int asc = a.getClassMetric("ASC");
		Assert.assertEquals(6, asc);
	}
	
	@Test
	public void testNAEC() {
		int naec = a.getClassMetric("NAEC");
		Assert.assertEquals(17, naec);
	}
	
	@Test
	public void testNEC() {
		int nec = a.getClassMetric("NEC"); 
		Assert.assertEquals(33, nec);
	}

}
