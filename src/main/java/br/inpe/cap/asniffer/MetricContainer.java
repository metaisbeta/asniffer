package br.inpe.cap.asniffer;

import java.util.ArrayList;
import java.util.List;

import br.inpe.cap.asniffer.utils.FileUtils;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

public class MetricContainer{

	public List<String> getMetrics(){
		
		FastClasspathScanner scan = new FastClasspathScanner(FileUtils.getJarDependencies());
		ScanResult result = scan.scan();
		List<String> metricNames = new ArrayList<>();
		
		for (String metricName : result.getNamesOfClassesWithAnnotation("br.inpe.cap.asniffer.annotations.AnnotationMetric")) 
			metricNames.add(metricName);
			
		return metricNames;
	}
}
