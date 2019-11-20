package br.inpe.cap.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.MetricResult;

public class AEDTest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAED() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		//Assert.assertEquals(2, a.getElementMetric("AED"));
	
		List<CodeElementModel> aed = a.getElementMetric("AED");
		
		for (CodeElementModel elementMetric : aed) {
			System.out.println("Element: " + elementMetric.getElementName());
			System.out.println("Type: " + elementMetric.getType());
			System.out.println("Line: " + elementMetric.getLine());
			System.out.println("Metric Value: " + elementMetric.getMetricValue());
		}
		
		
		assertEquals(32, aed.size());
		
	}
	
}
