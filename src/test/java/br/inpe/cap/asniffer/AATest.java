package br.inpe.cap.asniffer;

import static org.junit.Assert.assertEquals;

import java.util.List;
import br.inpe.cap.asniffer.AMReport;

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
		List<ElementMetric> aa = a.getElementMetric("AA");
		
		for (ElementMetric elementMetric : aa) {
			System.out.println("Element: " + elementMetric.getElementName());
			System.out.println("Type: " + elementMetric.getType());
			System.out.println("Line: " + elementMetric.getLine());
			System.out.println("Metric Value: " + elementMetric.getMetricValue());
		}
		
		assertEquals(28, aa.size());
	}
	
}
