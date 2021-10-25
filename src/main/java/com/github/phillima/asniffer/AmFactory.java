package com.github.phillima.asniffer;


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
import com.github.phillima.asniffer.utils.FileUtils;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class AmFactory {

    // it's necessary investigate the optimal number of files to change the strategy to parallel
    private int MIN_NUMBER_FILES_TO_PARALLEL = 5_000;

    private int MIN_NUMBER_TO_PARTITIONS = 10_000;

    private int numberOfProcessors = Runtime.getRuntime().availableProcessors();

    private String path;

    private String projectName;

    public AmFactory(String path, String projectName) {
        this.path = path;
        this.projectName = projectName;
    }

    public AM createAm() {
        String[] javaFiles = FileUtils.getAllJavaFiles(path);

        MetricsExecutor storage = new MetricsExecutor(() ->
                includeClassMetrics(),
                includeAnnotationMetrics(),
                includeCodeElementMetrics(),
                projectName
        );

        var stream = generatePartitionsStream(javaFiles);
        return new AM(storage, stream);

    }

    private Stream<Stream<String>> generatePartitionsStream(String[] javaFiles) {
        int numberPartitions = (javaFiles.length / MIN_NUMBER_TO_PARTITIONS) + 1;
        var partitions = Lists.partition(List.of(javaFiles), numberPartitions);
        return generateStream(partitions, javaFiles.length, numberPartitions);
    }

    private Stream<Stream<String>> generateStream(List<List<String>> javaFiles, int files, int partitions) {
        if (files >= MIN_NUMBER_FILES_TO_PARALLEL * partitions && numberOfProcessors > 1) {
            return javaFiles.stream().map(it -> it.stream().parallel());
        }
        return javaFiles.stream().map(Collection::stream);
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
