package com.github.phillima.test.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.phillima.asniffer.AM;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.model.ClassModel;

public class TestCodeElementMetric {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAED() {
		
		ClassModel result = report.getPackages().get(0).getClassModel("annotationtest.AnnotationTest");
		List<CodeElementModel> codeElements = result.getElementsReport();
		int aedValue = 0;
		
		for (CodeElementModel codeElement : codeElements) {
			if(codeElement.getLine()==161) {
				aedValue = codeElement.getAed();
			}
		}
		
		assertEquals(11, aedValue);
		
	}

	@Test
	public void testeAEDMethodWithAnnotatedParam() {
		ClassModel result = report.getPackages().get(0).getClassModel("annotationtest.AnnotationTest");
		List<CodeElementModel> codeElements = result.getElementsReport();
		int aedValue = 0;

		for (CodeElementModel codeElement : codeElements) {
			if(codeElement.getLine()==56) {
				aedValue = codeElement.getAed();
			}
		}

		assertEquals(8, aedValue);
	}

}
