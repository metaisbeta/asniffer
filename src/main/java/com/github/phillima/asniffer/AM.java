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

}
