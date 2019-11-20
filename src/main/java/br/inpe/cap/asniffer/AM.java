package br.inpe.cap.asniffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import com.google.common.collect.Lists;

import br.inpe.cap.asniffer.interfaces.IAnnotationMetricCollector;
import br.inpe.cap.asniffer.interfaces.IClassMetricCollector;
import br.inpe.cap.asniffer.interfaces.ICodeElementMetricCollector;
import br.inpe.cap.asniffer.model.AMReport;
import br.inpe.cap.asniffer.utils.FileUtils;

//Annotation Metric
public class AM {

	private static final int MAX_AT_ONCE;

	static {
		String jdtMax = System.getProperty("jdt.max");
		if(jdtMax!=null) {
			MAX_AT_ONCE = Integer.parseInt(jdtMax);
		} else {
			long maxMemory= Runtime.getRuntime().maxMemory() / (1 << 20); // in MiB
			
			if      (maxMemory >= 2000) MAX_AT_ONCE= 400;
			else if (maxMemory >= 1500) MAX_AT_ONCE= 300;
			else if (maxMemory >= 1000) MAX_AT_ONCE= 200;
			else if (maxMemory >=  500) MAX_AT_ONCE= 100;
			else                        MAX_AT_ONCE=  25;
		}
	}

	public AMReport calculate(String path, String projectName) {
		String[] srcDirs = FileUtils.getAllDirs(path);
		String[] javaFiles = FileUtils.getAllJavaFiles(path);
		
		MetricsExecutor storage = new MetricsExecutor(() -> includeClassMetrics(), () -> includeAnnotationMetrics(), () -> includeCodeElementMetrics() , projectName);
		
		List<List<String>> partitions = Lists.partition(Arrays.asList(javaFiles), MAX_AT_ONCE);

		for(List<String> partition : partitions) {
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			
			Map<String, String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
			parser.setCompilerOptions(options);
			parser.setEnvironment(null, srcDirs, null, true);
			parser.createASTs(partition.toArray(new String[partition.size()]), null, new String[0], storage, null);
		}
		
		return storage.getReport();
	}
	
	private List<IClassMetricCollector> includeClassMetrics(){
		
		List<IClassMetricCollector> metrics = new ArrayList<>();
		MetricContainer metricContainer = new MetricContainer();

		for (String metricName : metricContainer.getClassMetrics()) {
			try {
				Class<?> clazz = Class.forName(metricName);
				metrics.add((IClassMetricCollector) clazz.newInstance());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException  e) {
				e.printStackTrace();
			}
		}
		
		return metrics;
	}
	
	private List<IAnnotationMetricCollector> includeAnnotationMetrics(){
		
		List<IAnnotationMetricCollector> metrics = new ArrayList<>();
		MetricContainer metricContainer = new MetricContainer();

		for (String metricName : metricContainer.getAnnotationMetric()) {
			try {
				Class<?> clazz = Class.forName(metricName);
				metrics.add((IAnnotationMetricCollector) clazz.newInstance());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException  e) {
				e.printStackTrace();
			}
		}
		
		return metrics;
	}
	
	private List<ICodeElementMetricCollector> includeCodeElementMetrics(){
			
		List<ICodeElementMetricCollector> metrics = new ArrayList<>();
		MetricContainer metricContainer = new MetricContainer();

		for (String metricName : metricContainer.getCodeElementMetric()) {
			try {
				Class<?> clazz = Class.forName(metricName);
				metrics.add((ICodeElementMetricCollector) clazz.newInstance());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException  e) {
				e.printStackTrace();
			}
		}
		
		return metrics;
	}

}
