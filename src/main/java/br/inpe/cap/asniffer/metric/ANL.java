package br.inpe.cap.asniffer.metric;


import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import br.inpe.cap.asniffer.annotations.AnnotationMetric;
import br.inpe.cap.asniffer.interfaces.IAnnotationMetricCollector;
import br.inpe.cap.asniffer.model.AnnotationMetricModel;

@AnnotationMetric
public class ANL implements IAnnotationMetricCollector {
	
	@Override
	public void execute(CompilationUnit cu, AnnotationMetricModel annotationMetricModel, Annotation annotation) {

		int anl = getNestingLevel(annotation);
		annotationMetricModel.addAnnotationMetric("ANL", anl);
		
	}
	
	private int getNestingLevel(Annotation annotation) {
		ASTNode parentNode;
		int anlCount = 0;
		parentNode = annotation.getParent();
		while(parentNode.getNodeType() != ASTNode.COMPILATION_UNIT) {
			if(parentNode.getNodeType() == ASTNode.MARKER_ANNOTATION ||
					parentNode.getNodeType() == ASTNode.NORMAL_ANNOTATION ||
					parentNode.getNodeType() == ASTNode.SINGLE_MEMBER_ANNOTATION)
				anlCount++;
			parentNode = parentNode.getParent();		
		}
		return anlCount;
	}

	

}
