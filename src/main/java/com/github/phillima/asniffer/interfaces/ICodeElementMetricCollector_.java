package com.github.phillima.asniffer.interfaces;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.phillima.asniffer.model.CodeElementModel;

public interface ICodeElementMetricCollector_ {

    public void execute(CompilationUnit cu, CodeElementModel codeElementMetricModel, Node codeElementNode);

}
