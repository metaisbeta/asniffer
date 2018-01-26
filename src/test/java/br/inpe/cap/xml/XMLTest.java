package br.inpe.cap.xml;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.inpe.cap.asniffer.output.ClassPojo;
import br.inpe.cap.asniffer.output.MetricsValues;
import br.inpe.cap.asniffer.output.MultiMetric;
import br.inpe.cap.asniffer.output.PackagePojo;
import br.inpe.cap.asniffer.output.ProjectPojo;
import br.inpe.cap.asniffer.output.SimpleMetric;
import br.inpe.cap.asniffer.utils.XMLUtils;

public class XMLTest {

	ProjectPojo project;
	
	@Before
	public void setPOJO() {
		
		List<SimpleMetric> metrics = new ArrayList<>();
		SimpleMetric metric = new SimpleMetric("AC",2);
		SimpleMetric metric2 = new SimpleMetric("UAC", 3);
		SimpleMetric metric3 = new SimpleMetric("ASC", 4);
		metrics.add(metric);
		metrics.add(metric2);
		metrics.add(metric3);
		
		List<MultiMetric> multiMetrics = new ArrayList<>();
		List<MetricsValues> metricsValues = new ArrayList<>();
		metricsValues.add(new MetricsValues("Atributo1", 2));
		metricsValues.add(new MetricsValues("Atributo2",3));
		metricsValues.add(new MetricsValues("Atributo3",4));
		MultiMetric multiMetric = new MultiMetric("AED",metricsValues);
		
		List<MetricsValues> multiMetricsValues = new ArrayList<>();
		multiMetricsValues.add(new MetricsValues("Annotation1",5));
		multiMetricsValues.add(new MetricsValues("Annotation2",6));
		multiMetricsValues.add(new MetricsValues("Annotation3",7));
		MultiMetric multiMetric2 = new MultiMetric("AA",multiMetricsValues);
		multiMetrics.add(multiMetric);
		multiMetrics.add(multiMetric2);
		
		List<ClassPojo> classes = new ArrayList<>();
		ClassPojo clazz = new ClassPojo("Classe1", metrics, multiMetrics);
		ClassPojo clazz2 = new ClassPojo("Classe2", metrics, multiMetrics);
		ClassPojo clazz3 = new ClassPojo("Classe3", metrics, multiMetrics);
		classes.add(clazz);
		classes.add(clazz2);
		classes.add(clazz3);

		List<PackagePojo> packages_ = new ArrayList<>();
		PackagePojo package_ = new PackagePojo("package.com.br.1", classes);
		PackagePojo package_2 = new PackagePojo("package.com.br.2", classes);
		PackagePojo package_3 = new PackagePojo("package.com.br.3", classes);
		packages_.add(package_);
		packages_.add(package_2);
		packages_.add(package_3);
		
		project = new ProjectPojo("Test Project",packages_);
	}
	
	@Test	
	public void firstTest() {
		XMLUtils.createXMLFile(project);
		assertTrue(true);
	}
	
}
