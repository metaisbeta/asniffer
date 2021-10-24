package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.phillima.asniffer.interfaces.ICodeElementMetricCollector;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.utils.AnnotationUtils;

public class AED implements ICodeElementMetricCollector {

    @Override
    public void execute(CompilationUnit cu, CodeElementModel codeElementMetricModel, Node codeElementNode) {

        int aed = AnnotationUtils.checkForAnnotations(codeElementNode).size();

        codeElementMetricModel.setAed(aed);

    }
}
