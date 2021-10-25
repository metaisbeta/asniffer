package com.github.phillima.asniffer;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
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
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.*;

public class AM {

    private MetricsExecutor storage;
    private Stream<Stream<String>> partitions;

    public AM(MetricsExecutor storage, Stream<Stream<String>> partitions) {
        this.storage = storage;
        this.partitions = partitions;
        StaticJavaParser.setConfiguration(StaticJavaParser.getConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17_PREVIEW));
    }

    public AMReport calculate() {
        partitions.forEach(stream ->
                storage.accept(stream
                        .map(File::new)
                        .filter(File::isFile)
                        .map(file -> parseFile(file))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                )
        );
        return storage.getReport();
    }

    private CompilationUnit parseFile(File file) {
        try {
            return StaticJavaParser.parse(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }


}
