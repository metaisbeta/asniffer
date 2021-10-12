package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.phillima.asniffer.annotations.CodeElementMetric;
import com.github.phillima.asniffer.interfaces.ICodeElementMetricCollector_;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.utils.AnnotationUtilsJavaParser;

@CodeElementMetric
public class AEDJavaParser implements ICodeElementMetricCollector_ {

    @Override
    public void execute(CompilationUnit cu, CodeElementModel codeElementMetricModel, Node codeElementNode) {

        int aed = AnnotationUtilsJavaParser.checkForAnnotations(codeElementNode).size();

        codeElementMetricModel.setAed(aed);

    }
}
