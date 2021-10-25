package com.github.phillima.asniffer;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.model.CodeElementModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassInfo extends VoidVisitorAdapter<Object> {

    private CompilationUnit cu;
    private String className = null;
    private String type;
    private Map<Node, CodeElementModel> codeElementsInfo;
    private String packageName;

    public ClassInfo(CompilationUnit cu) {
        this.cu = cu;
        this.codeElementsInfo = new ConcurrentHashMap<>();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration node, Object obj) {
        String innerType = null;
        if (node.isInterface())
            innerType = "interface";
        else
            innerType = "class";

        CodeElementModel codeElementModel = new CodeElementModel(node.getName().getIdentifier(), innerType, node.getTokenRange().get().toRange().get().begin.line);
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);
    }


    @Override
    public void visit(CompilationUnit node, Object obj) {
        getFullClassName(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(EnumDeclaration node, Object obj) {
        String innerType = "enum";
        CodeElementModel codeElementModel = new CodeElementModel(node.getName().getIdentifier(), innerType, node.getTokenRange().get().toRange().get().begin.line);
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);
    }

    @Override
    public void visit(AnnotationDeclaration node, Object obj) {
        String innerType = "annotation-declaration";
        CodeElementModel codeElementModel = new CodeElementModel(node.getNameAsString(), innerType, node.getTokenRange().get().toRange().get().begin.line);
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);

    }

    @Override
    public void visit(MethodDeclaration node, Object obj) {
        CodeElementModel codeElementModel = new CodeElementModel(node.getName().toString(), "method", node.getTokenRange().get().toRange().get().begin.line);
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);
    }

    @Override
    public void visit(ConstructorDeclaration node, Object obj) {
        CodeElementModel codeElementModel = new CodeElementModel(node.getName().toString(), "constructor", node.getTokenRange().get().toRange().get().begin.line);
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);
    }

    @Override
    public void visit(FieldDeclaration node, Object obj) {
        String fieldName = node.getVariables().getFirst().get().getName().toString();
        CodeElementModel codeElementModel = new CodeElementModel(fieldName, "field", node.getTokenRange().get().toRange().get().begin.line);
        codeElementsInfo.put(node, codeElementModel);

        super.visit(node, obj);
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getType() {
        return type;
    }

    public Map<Node, CodeElementModel> getCodeElementsInfo() {
        return codeElementsInfo;
    }

    //Inner methods
    private String getFullClassName(Node node) {

        if (node instanceof CompilationUnit) {
            ((CompilationUnit) node).getPrimaryType()
                    .ifPresent(
                            typeDeclaration -> {
                                defineTypeInClassInfo(typeDeclaration);
                                cu.getPackageDeclaration().ifPresent(packageDeclaration ->
                                        packageName = packageDeclaration.getNameAsString());
                                className = generateClassName();
                            }
                    );
        }
        return null;
    }

    private String generateClassName() {
        return cu.getPackageDeclaration().get().getName() + "." + cu.getPrimaryTypeName().get();
    }

    private void defineTypeInClassInfo(TypeDeclaration<?> typeDeclaration) {
        if (typeDeclaration.isAnnotationDeclaration()) {
            type = "annotation-declaration";
        } else if (typeDeclaration.isEnumDeclaration() || typeDeclaration.isEnumConstantDeclaration()) {
            type = "enum";
        } else if (typeDeclaration.isClassOrInterfaceDeclaration()) {
            if (typeDeclaration.asClassOrInterfaceDeclaration().isInterface()) {
                type = "interface";
            } else {
                type = "class";
            }
        }
    }
}