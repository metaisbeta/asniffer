package br.inpe.cap.asniffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import br.inpe.cap.asniffer.metric.AC;
import br.inpe.cap.asniffer.metric.ASC;
import br.inpe.cap.asniffer.metric.MetricCollector;
import br.inpe.cap.asniffer.metric.NAEC;
import br.inpe.cap.asniffer.metric.UAC;
import br.inpe.cap.asniffer.utils.FileUtils;
import com.google.common.collect.Lists;

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

	public List<Callable<MetricCollector>> pluggedMetrics; 

	public AM() {
		this.pluggedMetrics = new ArrayList<>();
	}
	
	public AM plug(Callable<MetricCollector> metric) {
		this.pluggedMetrics.add(metric);
		return this;
	}
	
	public AMReport calculate(String path) {
		String[] srcDirs = FileUtils.getAllDirs(path);
		String[] javaFiles = FileUtils.getAllJavaFiles(path);
		
		MetricsExecutor storage = new MetricsExecutor(() -> metrics());
		
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
	
	private List<MetricCollector> metrics() {
		List<MetricCollector> all = defaultMetrics();
		all.addAll(userMetrics());
		
		return all;
	}

	private List<MetricCollector> defaultMetrics() {
		return new ArrayList<>(Arrays.asList(new AC(), new UAC(), new ASC(), new NAEC()));
	}

	private List<MetricCollector> userMetrics() {
		try {
			List<MetricCollector> userMetrics = new ArrayList<MetricCollector>();
			
			for(Callable<MetricCollector> metricToBeCreated : pluggedMetrics) {
				userMetrics.add(metricToBeCreated.call());
			}

			return userMetrics;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
