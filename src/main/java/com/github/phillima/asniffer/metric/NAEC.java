package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;


public class NAEC extends VoidVisitorAdapter<Object> implements IClassMetricCollector {


    private int annotatedElements = 0;


    @Override
    public void visit(ClassOrInterfaceDeclaration node, Object obj) {
        checkForAnnotations(node);
        super.visit(node, obj);
    }


    @Override
    public void visit(EnumDeclaration node, Object obj) {
        checkForAnnotations(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(AnnotationDeclaration node, Object obj) {
        checkForAnnotations(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(MethodDeclaration node, Object obj) {
        checkForAnnotations(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(FieldDeclaration node, Object obj) {
        checkForAnnotations(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(EnumConstantDeclaration node, Object obj) {
        checkForAnnotations(node);
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

    private void checkForAnnotations(NodeWithAnnotations node) {

        if (node.getAnnotations().isNonEmpty()) {
            annotatedElements++;
        }
    }
}
