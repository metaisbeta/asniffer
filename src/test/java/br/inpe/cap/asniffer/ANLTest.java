package br.inpe.cap.asniffer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class ANLTest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testANL() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		List<ElementMetric> anl = a.getElementMetric("ANL");
		
		for (ElementMetric elementMetric : anl) {
			System.out.println("Element: " + elementMetric.getElementName());
			System.out.println("Line: " + elementMetric.getLine());
			System.out.println("Metric Value: " + elementMetric.getMetricValue());
		}
		
		assertEquals(28, anl.size());
		
	}

}
