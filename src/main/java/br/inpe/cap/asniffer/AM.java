package br.inpe.cap.asniffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import br.inpe.cap.asniffer.metric.AA;
import br.inpe.cap.asniffer.metric.AC;
import br.inpe.cap.asniffer.metric.AED;
import br.inpe.cap.asniffer.metric.ANL;
import br.inpe.cap.asniffer.metric.ASC;
import br.inpe.cap.asniffer.metric.LOCAD;
import br.inpe.cap.asniffer.metric.MetricCollector;
import br.inpe.cap.asniffer.metric.NAEC;
import br.inpe.cap.asniffer.metric.UAC;
import br.inpe.cap.asniffer.utils.FileUtils;
import com.google.common.collect.Lists;

//Annotation Metric
public class AM {

	private static final int MAX_AT_ONCE;
	private String userConfigFile;

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


	public AM(String userConfigFile) {
		this.userConfigFile = userConfigFile;
	}
	public AMReport calculate(String path, String projectName) {
		String[] srcDirs = FileUtils.getAllDirs(path);
		String[] javaFiles = FileUtils.getAllJavaFiles(path);
		
		MetricsExecutor storage = new MetricsExecutor(() -> includeMetrics(), projectName);
		
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
	
	private List<MetricCollector> includeMetrics(){
		
		List<MetricCollector> metrics = new ArrayList<>();
		//Read the default metrics
		metrics.addAll(Arrays.asList(new AC(), new UAC(), new ASC(),new NAEC(), new AED(), 
				new AA(), new ANL(), new LOCAD()));
	
		//Read the user metrics, if config file was supplied
		if(userConfigFile!=null) {
			ReadConfigFile configFile = new ReadConfigFile(userConfigFile);
			for (String metricName : configFile.getMetrics()) {
				try {
					Class<?> clazz = Class.forName(metricName);
					metrics.add((MetricCollector) clazz.newInstance());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException  e) {
					e.printStackTrace();
				}
			}
		}
		
		return metrics;
	}

}
