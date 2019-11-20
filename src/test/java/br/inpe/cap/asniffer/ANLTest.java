package br.inpe.cap.asniffer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.model.CodeElementModel;
import br.inpe.cap.asniffer.model.MetricResult;

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
		List<CodeElementModel> anl = a.getElementMetric("ANL");
		
		for (CodeElementModel elementMetric : anl) {
			System.out.println("Element: " + elementMetric.getElementName());
			System.out.println("Line: " + elementMetric.getLine());
			System.out.println("Metric Value: " + elementMetric.getMetricValue());
		}
		
		assertEquals(28, anl.size());
		
	}

}
