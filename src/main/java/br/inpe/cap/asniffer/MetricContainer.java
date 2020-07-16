package br.inpe.cap.asniffer;

import java.util.ArrayList;
import java.util.List;

import br.inpe.cap.asniffer.utils.FileUtils;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

public class MetricContainer{

	ScanResult result = null;
	
	private static MetricContainer instance;
	
	public static MetricContainer getInstance() {
		if(instance == null)
			return new MetricContainer();
		return instance;
	}
	
	private MetricContainer() {
		FastClasspathScanner scan = new FastClasspathScanner(FileUtils.getJarDependencies());
		result = scan.scan();
	}
	
	public List<String> getClassMetrics(){
		
		List<String> classMetricNames = new ArrayList<>();
		
		for (String metricName : result.getNamesOfClassesWithAnnotation("br.inpe.cap.asniffer.annotations.ClassMetric")) 
			classMetricNames.add(metricName);
			
		return classMetricNames;
	}
	
	public List<String> getAnnotationMetric(){
		
		List<String> annotationMetric = new ArrayList<>();
		
		for (String metricName : result.getNamesOfClassesWithAnnotation("br.inpe.cap.asniffer.annotations.AnnotationMetric")) 
			annotationMetric.add(metricName);
			
		return annotationMetric;
		
	}
	
	public List<String> getCodeElementMetric(){
		
		List<String> codeElementMetric = new ArrayList<>();
				
		for (String metricName : result.getNamesOfClassesWithAnnotation("br.inpe.cap.asniffer.annotations.CodeElementMetric")) 
			codeElementMetric.add(metricName);
			
		return codeElementMetric;
	}
}
