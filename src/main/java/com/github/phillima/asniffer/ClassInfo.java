package com.github.phillima.asniffer;


import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.model.CodeElementType;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;


public class ClassInfo extends VoidVisitorAdapter<Object> {

    private CompilationUnit cu;
    private String className = null;
    private CodeElementType type;
    private Map<Node, CodeElementModel> codeElementsInfo;
    private String packageName;

    public ClassInfo(CompilationUnit cu) {
        this.cu = cu;
        this.codeElementsInfo = new ConcurrentHashMap<>();
    }

    @Override
    public void visit(CompilationUnit node, Object obj) {
        getFullClassName(node);
        super.visit(node, obj);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration node, Object obj) {
        CodeElementType innerType;
        if (node.isInterface())
            innerType = CodeElementType.INTERFACE;
        else
            innerType = CodeElementType.CLASS;

        CodeElementModel codeElementModel
                = new CodeElementModel(node.getName().getIdentifier(), innerType, getLineStart(node));
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);
    }


    @Override
    public void visit(EnumDeclaration node, Object obj) {
        CodeElementType innerType = CodeElementType.ENUM;
        CodeElementModel codeElementModel =
                new CodeElementModel(node.getName().getIdentifier(), innerType, getLineStart(node));
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);
    }

    @Override
    public void visit(AnnotationDeclaration node, Object obj) {
        CodeElementType innerType = CodeElementType.ANNOTATION_DECLARATION;
        CodeElementModel codeElementModel =
                new CodeElementModel(node.getName().getIdentifier(), innerType, getLineStart(node));
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);

    }

    @Override
    public void visit(MethodDeclaration node, Object obj) {
        CodeElementType innerType = CodeElementType.METHOD;
        CodeElementModel codeElementModel =
                new CodeElementModel(node.getName().getIdentifier(), innerType, getLineStart(node));
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);
    }

    @Override
    public void visit(ConstructorDeclaration node, Object obj) {
        CodeElementType innerType = CodeElementType.CONSTRUCTOR;
        CodeElementModel codeElementModel =
                new CodeElementModel(node.getName().getIdentifier(), innerType, getLineStart(node));
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);
    }

    @Override
    public void visit(FieldDeclaration node, Object obj) {
        CodeElementType innerType = CodeElementType.FIELD;
        String fieldName = node.getVariables().getFirst().map(VariableDeclarator::getName).toString();
        CodeElementModel codeElementModel
                = new CodeElementModel(fieldName, innerType, getLineStart(node));
        codeElementsInfo.put(node, codeElementModel);
        super.visit(node, obj);
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getClassName() {
        return className;
    }

    public CodeElementType getType() {
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
                                packageName = cu.getPackageDeclaration().get().getNameAsString();
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
            type = CodeElementType.ANNOTATION_DECLARATION;
        } else if (typeDeclaration.isEnumDeclaration() || typeDeclaration.isEnumConstantDeclaration()) {
            type = CodeElementType.ENUM;
        } else if (typeDeclaration.isClassOrInterfaceDeclaration()) {
            if (typeDeclaration.asClassOrInterfaceDeclaration().isInterface()) {
                type = CodeElementType.INTERFACE;
            } else {
                type = CodeElementType.CLASS;
            }
        }
    }

    // if doest not exist a range for the node, return value -1;
    private Integer getLineStart(Node node) {
        Optional<TokenRange> tokenRange = node.getTokenRange();
        if (tokenRange.isPresent()) {
            return tokenRange.get().toRange()
                    .map(range -> range.begin.line)
                    .orElse(-1);
        }
        throw new RuntimeException("Exists a node without a range");

    }
}