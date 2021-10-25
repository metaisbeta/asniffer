package com.github.phillima.asniffer;

import com.github.javaparser.*;
import com.github.phillima.asniffer.interfaces.*;
import com.github.phillima.asniffer.metric.*;
import com.github.phillima.asniffer.utils.*;
import com.google.common.collect.*;
import org.apache.logging.log4j.*;

import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class AmFactory {

    // it's necessary investigate the optimal number of files to change the strategy to parallel
    private int MIN_NUMBER_FILES_TO_PARALLEL = 5_000;

    private int MIN_NUMBER_TO_PARTITIONS = 10_000;

    private int numberOfProcessors = Runtime.getRuntime().availableProcessors();

    private String path;

    private String projectName;

    private static final org.apache.logging.log4j.Logger logger =
            LogManager.getLogger(AmFactory.class);



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
            logger.info("The number of file is {}, the number of partitions is {} and the running process is parallel", files, partitions);
            return javaFiles.stream().map(it -> it.stream().parallel());
        }
        logger.info("The number of file is {}, the number of partitions is {}", files, partitions);
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
