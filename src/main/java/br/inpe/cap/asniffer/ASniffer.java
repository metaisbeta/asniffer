package br.inpe.cap.asniffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import br.inpe.cap.asniffer.annotations.Clazz;
import br.inpe.cap.asniffer.annotations.MultMetric;
import br.inpe.cap.asniffer.annotations.Package_;
import br.inpe.cap.asniffer.annotations.SimpMetric;

public class ASniffer {
	
	private static final ASniffer INSTANCE = new ASniffer();
	
	private List<NormalAnnotation> normalAnnotations;
	private List<SingleMemberAnnotation> singleAnnotations;
	private List<MarkerAnnotation> markerAnnotations;
	private List<Annotation> allAnnotations;
	private List<BodyDeclaration> programmingElements;
	
	private int ac,uac,asc;
	private Map<Annotation, Integer> locad,anl,aa;
	private Map<BodyDeclaration, Integer> aed;

	private String className;
	private String packageName;
	
	private ASniffer() {	}
	
	public static ASniffer getInstance() {
		return INSTANCE;
	}
	
	@SimpMetric
	public int getAc() {
		return this.ac;
	}
	
	@SimpMetric
	public int getUac() {
		return this.uac;
	}
	
	@SimpMetric
	public int getAsc() {
		return this.asc;
	}
	
	@Clazz
	public String getClassName() {
		return className;
	}

	@Package_
	public String getPackageName() {
		return packageName;
	}
	
	@MultMetric
	public Map<Annotation, Integer> getLocad() {
		return this.locad;
	}
	
	@MultMetric
	public Map<Annotation, Integer> getAnl() {
		return this.anl;
	}

	@MultMetric
	public Map<Annotation, Integer> getAa() {
		return this.aa;
	}
	
	@MultMetric
	public Map<BodyDeclaration, Integer> getAed() {
		return this.aed;
	}
	
	public void execute(CompilationUnit cu) {
		this.uac = executeUAC();
		this.ac = executeAC();
		this.asc = executeASC(cu);
		this.locad = executeLOCAD(cu);
		this.anl = executeANL();
		this.aa = executeAA();
		this.aed = executeAED();
	}
	
	
	//Annotations Schema in Class
	private int executeASC(CompilationUnit cu) {
		
		Set<String> imports = new HashSet<>();
		if(executeAC() <= 0) {
			System.out.println("No annotation in this class");
			return 0;
		}else {
			Set<String> anotSet = new HashSet<>();
			for (Annotation annotation : allAnnotations) {
				int endIndex = annotation.toString().indexOf("(");
				if(endIndex == -1)
					anotSet.add(annotation.toString().substring(1));
				else
					anotSet.add(annotation.toString().substring(1,endIndex));
			}
				
			for (Object importName : cu.imports()) {
				for(String annotation : anotSet) {
					if(importName.toString().contains(annotation)) {
						String import_ = importName.toString().replaceAll("import ", "");
						int endIndex = import_.lastIndexOf(";");
						int beginIndex = import_.lastIndexOf(".");
						if((import_.substring(beginIndex+1, endIndex)).equals(annotation)) 
							imports.add(import_.substring(0, beginIndex));
					}
						
				}
			}
			for(String importString : imports) 
				System.out.println("Imports for annot: " + importString);
		}
		return imports.size();
	}

	//Annotations in Class
	private int executeAC() {
		return allAnnotations.size();
	}
	
	//Unique Annotations in Class
	private int executeUAC(){
		Set<String> anotSet = new HashSet<>();
		for (Annotation annotation : allAnnotations) 
			anotSet.add(annotation.toString());
		return anotSet.size();
	}
	
	//LOC in Annotation Declaration
	public Map<Annotation, Integer> executeLOCAD(CompilationUnit cu){
		
		int startLineNumber, endLineNumber, locad;
		Map<Annotation,Integer> locadValues = new LinkedHashMap<>();
		
		for (Annotation annotation : allAnnotations) {
			startLineNumber = cu.getLineNumber(annotation.getStartPosition());
			int nodeLength = annotation.getLength();
			endLineNumber = cu.getLineNumber(annotation.getStartPosition() + nodeLength);
			locad = endLineNumber - startLineNumber + 1;
			locadValues.put(annotation, locad);
		}
		
		return locadValues;
	}
	
	//Attributes in Annotation
	public Map<Annotation, Integer> executeAA(){
		
		Map<Annotation,Integer> aaValues = new LinkedHashMap<>();
		
		for (NormalAnnotation nAnnotation : normalAnnotations) 
			aaValues.put(nAnnotation, nAnnotation.values().size());
		
		for (SingleMemberAnnotation singleAnnotation : singleAnnotations) 
			aaValues.put(singleAnnotation, 1);
		
		return aaValues;
		
	} 
	
	//Annotation Nesting Level
	public Map<Annotation, Integer> executeANL() {
	
		ASTNode parentNode;
		Map<Annotation,Integer> anl = new LinkedHashMap<>();
		for (Annotation annotation : allAnnotations) {
			int anlCount = 0;
			parentNode = annotation.getParent();
			while(parentNode.getNodeType() != ASTNode.COMPILATION_UNIT) {
				if(parentNode.getNodeType() == ASTNode.MARKER_ANNOTATION ||
						parentNode.getNodeType() == ASTNode.NORMAL_ANNOTATION ||
						parentNode.getNodeType() == ASTNode.SINGLE_MEMBER_ANNOTATION)
					anlCount++;
				parentNode = parentNode.getParent();		
			}
			anl.put(annotation, anlCount);
		}
		return anl;
	}
	
	//Annotation in Elements Declaration
	public Map<BodyDeclaration, Integer> executeAED() {
		Map<BodyDeclaration, Integer> aed = new LinkedHashMap<>();
		int aedCount;
		for (BodyDeclaration element : programmingElements) {
			aedCount = 0;
			for(Object modifier: element.modifiers())
				if(modifier instanceof Annotation) 
					aedCount++;
			aed.put(element, aedCount);
		}
		
		return aed;
	}
	
	//SETTERS
	public void setNormalAnnotations(List<NormalAnnotation> normalAnnotations) {
		this.normalAnnotations = normalAnnotations;
	}

	public void setSingleAnnotations(List<SingleMemberAnnotation> singleAnnotations) {
		this.singleAnnotations = singleAnnotations;
	}

	public void setMarkerAnnotations(List<MarkerAnnotation> markerAnnotations) {
		this.markerAnnotations = markerAnnotations;
	}

	public void setAllAnnotations(List<Annotation> allAnnotations) {
		this.allAnnotations = allAnnotations;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public void setProgrammingElements(List<BodyDeclaration> programmingElements) {
		this.programmingElements = programmingElements;
	}


}
