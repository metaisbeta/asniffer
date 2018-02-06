package br.inpe.cap.asniffer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class LOCADTest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM(null).calculate(testFilePath, "project");
	}
	
	@Test
	public void testLOCAD() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		List<ElementMetric> locad = a.getElementMetric("LOCAD");
		for (ElementMetric elementMetric : locad) {
			System.out.println("Element: " + elementMetric.getElementName());
			System.out.println("Line: " + elementMetric.getLine());
			System.out.println("Metric Value: " + elementMetric.getMetricValue());
		}
		assertEquals(28, locad.size());
	}

}
