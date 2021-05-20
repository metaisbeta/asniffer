package com.github.phillima.asniffer.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

public class AnnotationUtils {
	
	//INNER HELPER METHODS
	public static List<Annotation> checkForAnnotations(BodyDeclaration node) {
		
		List<Annotation> annotations = new ArrayList<Annotation>();
		for (Object modifier : node.modifiers()) {
			if(modifier instanceof Annotation) 
				checkForNestedAnnotations(annotations,(Annotation)modifier);
		}

		if(node instanceof MethodDeclaration) {
			//Check for parameters
			List<SingleVariableDeclaration> parameters = ((MethodDeclaration)node).parameters();
			parameters.forEach((param) -> {
				List<Object> modifiers = param.modifiers();
				//search for annotations on these parameters
				for (Object annotation : modifiers) {
					if(annotation instanceof Annotation)
						checkForNestedAnnotations(annotations, (Annotation) annotation);
				}
			});
		}

		return annotations;
	}
	
	public static void checkForNestedAnnotations(List<Annotation> annotations, Annotation annotation) {
		
		annotations.add(annotation);
		if(annotation instanceof NormalAnnotation) {
			List<MemberValuePair> arguments  = ((NormalAnnotation) annotation).values();
			
			//Inspecting arguments for inner annotations
			for(MemberValuePair value : arguments) {
				Expression  argArray = value.getValue();
				if(argArray instanceof ArrayInitializer) {
					for (Object memberValuePair : ((ArrayInitializer)argArray).expressions()) {
						if(memberValuePair instanceof Annotation) 
							checkForNestedAnnotations(annotations, (Annotation) memberValuePair);
					}
				}else if (argArray instanceof Annotation) {
					checkForNestedAnnotations(annotations, (Annotation) argArray);
				}
			}
		}
	}

}
