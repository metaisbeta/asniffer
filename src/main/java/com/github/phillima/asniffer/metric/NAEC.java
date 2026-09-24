package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.filter.AnnotationFilter;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;


public class NAEC extends VoidVisitorAdapter<Object> implements IClassMetricCollector {


    private int annotatedElements = 0;

    private final AnnotationFilter filter;

    public NAEC(final AnnotationFilter filter) {
        this.filter = filter;
    }


    @Override
    public void visit(ClassOrInterfaceDeclaration node, Object obj) {
        if(filter.matches(node.getNameAsString())) checkForAnnotations(node);
        super.visit(node, obj);
    }


    @Override
    public void visit(EnumDeclaration node, Object obj) {
        if(filter.matches(node.getNameAsString())) checkForAnnotations(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(AnnotationDeclaration node, Object obj) {
        if(filter.matches(node.getNameAsString())) checkForAnnotations(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(MethodDeclaration node, Object obj) {
        if(filter.matches(node.getNameAsString())) checkForAnnotations(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(FieldDeclaration node, Object obj) {
        boolean hasFilteredAnnotation = node.getAnnotations()
                .stream()
                .anyMatch(annotation ->
                        filter.matches(annotation.getNameAsString())
                );

        if(hasFilteredAnnotation) checkForAnnotations(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(EnumConstantDeclaration node, Object obj) {
        if(filter.matches(node.getNameAsString())) checkForAnnotations(node);
        super.visit(node, obj);
    }

    @Override
    public void execute(CompilationUnit cu, ClassModel result, AMReport report) {
        cu.accept(this, null);
    }

    @Override
    public void setResult(ClassModel result) {
        result.addClassMetric("NAEC", annotatedElements);
    }

    private void checkForAnnotations(NodeWithAnnotations<?> node) {
        boolean hasFiltered = node.getAnnotations().stream()
                .anyMatch(a -> filter.matches(a.getNameAsString()));
        if (hasFiltered) annotatedElements++;
    }
}
