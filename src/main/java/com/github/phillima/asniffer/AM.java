package com.github.phillima.asniffer;

import com.github.javaparser.StaticJavaParser;
import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.interfaces.ICodeElementMetricCollector;
import com.github.phillima.asniffer.metric.AA;
import com.github.phillima.asniffer.metric.AC;
import com.github.phillima.asniffer.metric.AED;
import com.github.phillima.asniffer.metric.ANL;
import com.github.phillima.asniffer.metric.ASC;
import com.github.phillima.asniffer.metric.LOCAD;
import com.github.phillima.asniffer.metric.NAEC;
import com.github.phillima.asniffer.metric.UAC;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AM {

<<<<<<< HEAD
    public AMReport calculate(String path, String projectName) {
        String[] srcDirs = FileUtils.getAllDirs(path);
        String[] javaFiles = FileUtils.getAllJavaFiles(path);

        MetricsExecutor storage = new MetricsExecutor(() -> includeClassMetrics(),
                includeAnnotationMetrics(),
                includeCodeElementMetrics(), projectName);

        storage.accept(Arrays.stream(javaFiles)
                .map(pathName -> new File(pathName))
                .filter(File::isFile)
                .map(file -> {
                    try {
                        return StaticJavaParser.parse(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList()));
        return storage.getReport();
    }

    private List<IClassMetricCollector> includeClassMetrics() {

        List<IClassMetricCollector> metrics = new ArrayList<>();
        metrics.add(new AC());
        metrics.add(new UAC());
        metrics.add(new ASC());
        metrics.add(new NAEC());

        return metrics;
    }

    private List<IAnnotationMetricCollector> includeAnnotationMetrics() {

        List<IAnnotationMetricCollector> metrics = new ArrayList<>();
        metrics.add(new AA());
        metrics.add(new ANL());
        metrics.add(new LOCAD());

        return metrics;
    }

    private List<ICodeElementMetricCollector> includeCodeElementMetrics() {

        List<ICodeElementMetricCollector> metrics = new ArrayList<>();
        metrics.add(new AED());

        return metrics;
    }
=======
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
>>>>>>> 5bc214c3ea34520d01f904ec7fa2ddd94cc66b07

}
