package com.github.phillima.asniffer.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;

public final class AnnotationUtils {

	private AnnotationUtils() { }
	
    //INNER HELPER METHODS
	public static List<AnnotationExpr> checkForAnnotations(Node node) {

        List<AnnotationExpr> annotations = new ArrayList<>();
        if (node instanceof NodeWithAnnotations) {
            NodeWithAnnotations<?> nodeWithAnnotations = ( (NodeWithAnnotations<?>) node);
            for (ListIterator<AnnotationExpr> it = nodeWithAnnotations.getAnnotations().listIterator(); it.hasNext(); ) {
                AnnotationExpr annotationExpr = it.next();
                checkForNestedAnnotations(annotations, annotationExpr);
            }
            if (nodeWithAnnotations instanceof NodeWithParameters) {
                checkForParametersWithAnnotations(annotations, (NodeWithParameters<Node>) nodeWithAnnotations);
            }
        }
        return annotations;
    }

    private static void checkForParametersWithAnnotations(List<AnnotationExpr> annotations, NodeWithParameters<Node> node) {

        node.getParameters()
                .forEach(it -> {
                    if (it instanceof NodeWithAnnotations) {
                        ((NodeWithAnnotations) it).getAnnotations().forEach(annotation -> annotations.add((AnnotationExpr) annotation));
                    }
                });
    }

    public static void checkForNestedAnnotations(List<AnnotationExpr> annotations, AnnotationExpr annotation) {

        annotations.add(annotation);
        if (annotation instanceof NormalAnnotationExpr) {
            NodeList<MemberValuePair> arguments = ((NormalAnnotationExpr) annotation).getPairs();

            //Inspecting arguments for inner annotations
            for (MemberValuePair value : arguments) {
                Expression argArray = value.getValue();
                if (argArray instanceof ArrayInitializerExpr) {
                    for (Object memberValuePair : ((ArrayInitializerExpr) argArray).getValues()) {
                        if (memberValuePair instanceof AnnotationExpr)
                            checkForNestedAnnotations(annotations, (AnnotationExpr) memberValuePair);
                    }
                } else if (argArray instanceof AnnotationExpr) {
                    checkForNestedAnnotations(annotations, (AnnotationExpr) argArray);
                }
            }
        }
    }

}
