package br.inpe.cap.test.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.AM;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.MetricResult;

public class TestCodeElementMetric {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAED() {
		
		MetricResult result = report.getPackages().get(0).getByClassName("annotationtest.AnnotationTest");
		List<CodeElementModel> codeElements = result.getElementsReport();
		int aedValue = 0;
		
		for (CodeElementModel codeElement : codeElements) {
			if(codeElement.getLine()==131) {
				aedValue = codeElement.getAed();
			}
		}
		
		assertEquals(11, aedValue);
		
	}
	
}
