package com.github.phillima.test.asniffer;

import com.github.phillima.asniffer.AM;
import com.github.phillima.asniffer.AMJavaParser;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.CodeElementModel;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

public class TestCodeElementMetricJavaParser {

	private static AMReport report;

	private ClassModel result;

	
	@BeforeClass
	public static void setUpAll() {
		String testFilePath = Paths.get(System.getProperty("user.dir") + "/annotationtest").toString();
		report = new AMJavaParser().calculate(testFilePath, "project");
	}

	@Before
	public void setUp(){
		result = report.getPackages()
				.stream()
				.filter(pk -> pk.getPackageName().equals("annotationtest"))
				.findFirst()
				.get()
				.getClassModel("annotationtest.AnnotationTest");
	}

	@Test
	public void testAED() {
		
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
