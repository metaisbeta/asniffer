package com.github.phillima.asniffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.phillima.asniffer.metric.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import com.google.common.collect.Lists;

import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.interfaces.ICodeElementMetricCollector;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.utils.FileUtils;

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
		
		MetricsExecutor storage = new MetricsExecutor(() -> includeClassMetrics(), 
						includeAnnotationMetrics(),
						includeCodeElementMetrics() , projectName);
		List<List<String>> partitions = Lists.partition(Arrays.asList(javaFiles), MAX_AT_ONCE);

		for(List<String> partition : partitions) {
			ASTParser parser = ASTParser.newParser(AST.JLS14);
			
			Map<String, String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_14, options);
			parser.setCompilerOptions(options);
			parser.setEnvironment(null, srcDirs, null, true);
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			parser.createASTs(partition.toArray(new String[partition.size()]), null, new String[0], storage, null);
		}
		
		return storage.getReport();
	}
	
	private List<IClassMetricCollector> includeClassMetrics(){
		
		List<IClassMetricCollector> metrics = new ArrayList<>();
		metrics.add(new AC());
		metrics.add(new UAC());
		metrics.add(new ASC());
		metrics.add(new NAEC());

    	return metrics;
	}
	
	private List<IAnnotationMetricCollector> includeAnnotationMetrics(){
		
		List<IAnnotationMetricCollector> metrics = new ArrayList<>();
		metrics.add(new AA());
		metrics.add(new ANL());
		metrics.add(new LOCAD());

		return metrics;
	}
	
	private List<ICodeElementMetricCollector> includeCodeElementMetrics(){
			
		List<ICodeElementMetricCollector> metrics = new ArrayList<>();
		metrics.add(new AED());
		
		return metrics;
	}

}
