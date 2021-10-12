package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.annotations.ClassMetric;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector_;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ClassMetric
public class UACJavaParser extends VoidVisitorAdapter<Object> implements IClassMetricCollector_ {

    Set<String> uniqueAnnotations = new HashSet<>();

    @Override
    public void visit(MarkerAnnotationExpr node, Object obj) {
        uniqueAnnotations.add(node.getNameAsString());
        super.visit(node, obj);
    }

    @Override
    public void visit(NormalAnnotationExpr node, Object obj) {
        uniqueAnnotations.add(node.getTokenRange().get().toString().replaceAll("(\t|\n)", ""));
        super.visit(node, obj);
    }

    @Override
    public void visit(SingleMemberAnnotationExpr node, Object obj) {
        uniqueAnnotations.add(node.getTokenRange().get().toString().replaceAll("(\t|\n)", ""));
        super.visit(node, obj);
    }


    @Override
    public void execute(CompilationUnit cu, ClassModel result, AMReport report) {
        this.visit(cu, null);
    }

    @Override
    public void setResult(ClassModel result) {
        result.addClassMetric("UAC", uniqueAnnotations.size());

    }

}
