package com.github.phillima.asniffer;

import com.github.javaparser.StaticJavaParser;
import com.github.phillima.asniffer.interfaces.IAnnotationMetricCollector_;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector_;
import com.github.phillima.asniffer.interfaces.ICodeElementMetricCollector_;
import com.github.phillima.asniffer.metric.AAJavaParser;
import com.github.phillima.asniffer.metric.ACJavaParser;
import com.github.phillima.asniffer.metric.AEDJavaParser;
import com.github.phillima.asniffer.metric.ANLJavaParser;
import com.github.phillima.asniffer.metric.ASCJavaParser;
import com.github.phillima.asniffer.metric.LOCADJavaParser;
import com.github.phillima.asniffer.metric.NAECJavaParser;
import com.github.phillima.asniffer.metric.UACJavaParser;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AMJavaParser {

    public AMReport calculate(String path, String projectName) {
        String[] srcDirs = FileUtils.getAllDirs(path);
        String[] javaFiles = FileUtils.getAllJavaFiles(path);

        MetricsExecutorJavaParser storage = new MetricsExecutorJavaParser(() -> includeClassMetrics(),
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

    private List<IClassMetricCollector_> includeClassMetrics() {

        List<IClassMetricCollector_> metrics = new ArrayList<>();
        metrics.add(new ACJavaParser());
        metrics.add(new UACJavaParser());
        metrics.add(new ASCJavaParser());
        metrics.add(new NAECJavaParser());

        return metrics;
    }

    private List<IAnnotationMetricCollector_> includeAnnotationMetrics() {

        List<IAnnotationMetricCollector_> metrics = new ArrayList<>();
        metrics.add(new AAJavaParser());
        metrics.add(new ANLJavaParser());
        metrics.add(new LOCADJavaParser());

        return metrics;
    }

    private List<ICodeElementMetricCollector_> includeCodeElementMetrics() {

        List<ICodeElementMetricCollector_> metrics = new ArrayList<>();
        metrics.add(new AEDJavaParser());

        return metrics;
    }

}
