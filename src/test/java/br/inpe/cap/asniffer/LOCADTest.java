package br.inpe.cap.asniffer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.MetricResult;

public class LOCADTest {

	private static AMReport report;
	
	@BeforeClass
	public static void setUp() {
		String testFilePath = System.getProperty("user.dir") + "/annotationtest";
		report = new AM().calculate(testFilePath, "project");
	}
	
	@Test
	public void testLOCAD() {
		
		MetricResult a = report.getByClassName("annotationtest.AnnotationTest");
		List<CodeElementModel> locad = a.getElementMetric("LOCAD");
		for (CodeElementModel elementMetric : locad) {
			System.out.println("Element: " + elementMetric.getElementName());
			System.out.println("Line: " + elementMetric.getLine());
			System.out.println("Metric Value: " + elementMetric.getMetricValue());
		}
		assertEquals(28, locad.size());
	}

}
