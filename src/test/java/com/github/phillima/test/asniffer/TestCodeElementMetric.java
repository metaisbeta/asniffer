package com.github.phillima.test.asniffer;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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
		String testFilePath = Paths.get(System.getProperty("user.dir") + "/annotationtest").toString();
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAED() {
		
		ClassModel result = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.AnnotationTest");

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
		ClassModel result = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.AnnotationTest");

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
