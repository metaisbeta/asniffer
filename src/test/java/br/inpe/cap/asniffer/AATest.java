package br.inpe.cap.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.MetricResult;

import org.junit.BeforeClass;
import org.junit.Test;

public class AATest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testAA() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		List<CodeElementModel> aa = a.getElementMetric("AA");
		
		for (CodeElementModel elementMetric : aa) {
			System.out.println("Element: " + elementMetric.getElementName());
			System.out.println("Type: " + elementMetric.getType());
			System.out.println("Line: " + elementMetric.getLine());
			System.out.println("Metric Value: " + elementMetric.getMetricValue());
		}
		
		assertEquals(28, aa.size());
	}
	
}
