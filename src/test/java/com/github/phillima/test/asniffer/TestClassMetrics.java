package com.github.phillima.test.asniffer;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.phillima.asniffer.AMJavaParser;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;

import java.nio.file.Paths;

public class TestClassMetrics {

	private static AMReport report;
	private static ClassModel classModel;
	
	//Fixture
	@BeforeClass
	public static void setUp() {
		String testFilePath = Paths.get(System.getProperty("user.dir") + "/annotationtest").toString();

		report = new AMJavaParser().calculate(testFilePath, "project");
		classModel = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.AnnotationTest");
	}
	
	@Test
	public void testAC() {
		int ac = classModel.getClassMetric("AC");
		Assert.assertEquals(39, ac);
	}
	
	@Test
	public void testUac() {
		int uac = classModel.getClassMetric("UAC");
		Assert.assertEquals(25, uac);
	}
	
	@Test
	public void testASC() {
		int asc = classModel.getClassMetric("ASC");
		Assert.assertEquals(7, asc);
	}
	
	@Test
	public void testNAEC() {
		int naec = classModel.getClassMetric("NAEC");
		Assert.assertEquals(21, naec);
	}
	
	@Test
	public void testNEC() {
		int nec = classModel.getClassMetric("NEC");
		Assert.assertEquals(44, nec);
	}

}
