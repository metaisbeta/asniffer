package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.phillima.asniffer.filter.AnnotationFilter;
import com.github.phillima.asniffer.interfaces.ICodeElementMetricCollector;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.utils.AnnotationUtils;

import java.util.stream.Collectors;

public class AED implements ICodeElementMetricCollector {

    private final AnnotationFilter filter;

    public AED(AnnotationFilter filter) {
        this.filter = filter;
    }

    @Override
    public void execute(CompilationUnit cu, CodeElementModel codeElementMetricModel, Node codeElementNode) {

        int aed = (int) AnnotationUtils.checkForAnnotations(codeElementNode).stream()
                .filter(annotationExpr -> filter.matches(annotationExpr.getNameAsString()))
                .count();

        codeElementMetricModel.setAed(aed);

    }
}
