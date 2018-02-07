package br.inpe.cap.asniffer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.inpe.cap.asniffer.utils.FileUtils;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

public class MetricContainer{

	public List<String> getMetrics(){
		String jarDependenciesPath = System.getProperty("user.dir") + File.separator + "scripts" + File.separator + "ignoreJarDependencies.txt";
		FastClasspathScanner scan = new FastClasspathScanner(FileUtils.getJarDependencies(jarDependenciesPath));
		ScanResult result = scan.scan();
		List<String> metricNames = new ArrayList<>();
		
		for (String metricName : result.getNamesOfClassesWithAnnotation("br.inpe.cap.asniffer.annotations.AnnotationMetric")) 
			metricNames.add(metricName);
			
		return metricNames;
	}
}
