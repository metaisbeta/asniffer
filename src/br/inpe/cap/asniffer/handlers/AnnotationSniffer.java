package br.inpe.cap.asniffer.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.Document;

import br.inpe.cap.asniffer.exceptions.FileFormatException;
import br.inpe.cap.asniffer.output.MetricOutputRepresentation;

import static br.inpe.cap.asniffer.util.UnitParser.numberOfLinesOfCode;

public class AnnotationSniffer {
	
	IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot rootWorkspace = workspace.getRoot();
    IAnnotation[] annotations = new IAnnotation[0];
	List<IAnnotation> anotList;
	Set<String> anotSet;
	IPackageFragment[] packages = null;
	IMember member = null;
	IProject project;
	IJavaProject javaProject;
	List<MetricOutputRepresentation> metricsOutputRepresentation = new ArrayList<>();
	int nestingCount = 0;
	
	public List<MetricOutputRepresentation> getMetricsOutputRepresentation() {
		return metricsOutputRepresentation;
	}

	public IProject fetchProject(String nameProject){
		// Fetch project on workspace
	    IProject[] projects = rootWorkspace.getProjects();
	    for (IProject project : projects) {
	    		if(project.getName().equals(nameProject))
	    			return project;
	    }
	    return null;
		
	}
    
	public List<Integer> getLOC(String nameProject) {
	
		project = fetchProject(nameProject);
		javaProject = JavaCore.create(project);
		IPackageFragment[] pacotes = null;
		List<Integer> numberOfLines = new ArrayList<Integer>();
		
		try {
			pacotes = javaProject.getPackageFragments();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		for (IPackageFragment pacote : pacotes) {
            try {
				if (pacote.getKind() == IPackageFragmentRoot.K_SOURCE) {
			        for(ICompilationUnit unit : pacote.getCompilationUnits()){
		    			Document doc = new Document(unit.getSource());
		    			numberOfLines.add(doc.getNumberOfLines());
					}
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return numberOfLines;
	}

	public int getAC(ICompilationUnit compilationUnit) {
		
		anotList = new ArrayList<IAnnotation>();
		int ac = 0;
		metricsOutputRepresentation.clear(); //Makes sure this list contains only metrics for current project
		
		try {
			for(IType type : compilationUnit.getAllTypes()){
				fetchAnnotations(type);
				for(IField field : type.getFields())
					fetchAnnotations(field);
				for(IMethod method : type.getMethods()){
					fetchAnnotations(method);
					for(ILocalVariable parameter : method.getParameters())
						fetchAnnotations(parameter);
				}
			}
			//All annotation classes has been fetched
			ac = (anotList.size());
			//Save Metric Representation
			metricsOutputRepresentation.add(new MetricOutputRepresentation("AC","Annotations in Class",anotList.size()));
			//Clear all annotations fetched
			anotList.clear();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		return ac;
	}

	public int getUAC(ICompilationUnit compilationUnit) {
		
		
		anotList = new ArrayList<IAnnotation>();
		int uac = 0;
		metricsOutputRepresentation.clear(); //Makes sure this list contains only metrics for current project
		StringBuilder uacBuilder = new StringBuilder();
		try {
			for(IType type : compilationUnit.getAllTypes()){
				fetchAnnotations(type);
				for(IField field : type.getFields())
					fetchAnnotations(field);
				for(IMethod method : type.getMethods()){
					fetchAnnotations(method);
					for(ILocalVariable parameter : method.getParameters())
						fetchAnnotations(parameter);
				}
			}
			//All annotation classes has been fetched
			
			uac = (fetchUAC(uacBuilder).size());
			//Save Metric Representation
			metricsOutputRepresentation.add(new MetricOutputRepresentation("UAC","Unique Annotations in Class",uac));
			//Clear all annotations fetched
			anotList.clear();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		return uac;
	}

	private Set<String> fetchUAC(StringBuilder uacBuilder) {
		
		Set<String> uacNames = new HashSet<>();
		//Check the array anotList, and retrives only distinct annotations
		for(IAnnotation annotation : anotList){
			uacBuilder.delete(0, uacBuilder.length());//Clear string buffer, for better performance
			
			try {
				for(IMemberValuePair attr : annotation.getMemberValuePairs()){
					String attributes[] = attr.getValue().toString().split(" ");
					uacBuilder.append(attr.getMemberName());
					uacBuilder.append("=");
					uacBuilder.append(attributes[0]);
				}
				uacBuilder.append(annotation.getElementName());
				uacNames.add(uacBuilder.toString());
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return uacNames;
	}

	public List<Integer> getAED(ICompilationUnit compilationUnit) {
			
		Map<IJavaElement, Integer> annotMap = new HashMap<>();
		List<Integer> numAED = new ArrayList<>();
		anotList = new ArrayList<>();
		int numAEDPerElement;
		metricsOutputRepresentation.clear();//Start a clear report for AED
		try {
			for(IType type : compilationUnit.getAllTypes()){
				numAEDPerElement = fetchAnnotations(type);
				annotMap.put(type,numAEDPerElement );
				for(IField field : type.getFields()){
					numAEDPerElement = fetchAnnotations(field);
					annotMap.put(field, numAEDPerElement);
				}
				for(IMethod method : type.getMethods()){
					numAEDPerElement = fetchAnnotations(method);
					annotMap.put(method, numAEDPerElement);
					for(ILocalVariable parameter : method.getParameters()){
						numAEDPerElement = fetchAnnotations(parameter);
						annotMap.put(parameter, numAEDPerElement);
					}
						
				}
			}
			//All annotations classes has been fetched
			Set<Map.Entry<IJavaElement, Integer>> entries = annotMap.entrySet();
			for(Map.Entry<IJavaElement, Integer> entry : entries){
				String elementName = entry.getKey().getElementName();
				int type = entry.getKey().getElementType();
				metricsOutputRepresentation.add(new MetricOutputRepresentation("AED", "Annotations in Element Declaration",
																			entry.getValue(), true, elementName, type));
				
				numAED.add(entry.getValue());
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return numAED;
	}
	
	//AA
	public List<Integer> getAA(ICompilationUnit compilationUnit) {
		List<Integer> numAA = new ArrayList<>();
		anotList = new ArrayList<>();
		metricsOutputRepresentation.clear();//Start a clear report for AED
		try {
			for(IType type : compilationUnit.getAllTypes()){
				fetchAnnotations(type);
				for(IField field : type.getFields()){
					fetchAnnotations(field);
				}
				for(IMethod method : type.getMethods()){
					fetchAnnotations(method);
					for(ILocalVariable parameter : method.getParameters())
						fetchAnnotations(parameter);
				}
			}
			//All annotations classes has been fetched
			for(IAnnotation annotation : anotList){
				numAA.add(annotation.getMemberValuePairs().length);
				metricsOutputRepresentation.add(new MetricOutputRepresentation("AA", "Attributes in Annotations", annotation.getMemberValuePairs().length, 
																				true, annotation.getElementName(), IJavaElement.ANNOTATION));
			}
			anotList.clear();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return numAA;
	}
	
	public int getNumClasses(String nameProject) {

		project = fetchProject(nameProject);
		javaProject = JavaCore.create(project);
		
		List<ICompilationUnit> classesList = new ArrayList<ICompilationUnit>();
		classesList = fetchNumberOfClasses(javaProject);
		
		return classesList.size();
		
	}

	public int getAnnotatedClass(String nameProject) {
		
		project = fetchProject(nameProject);
		javaProject = JavaCore.create(project);
		List<IType> classesList = new ArrayList<IType>();
		classesList = fetchAnnotatedClass(javaProject);
		
		return classesList.size();
	}
	

	//LOCAD
	public List<Integer> getLOCAD(ICompilationUnit compilationUnit) {
		
		anotList = new ArrayList<IAnnotation>();
		List<Integer> numLOCAD = new ArrayList<>();
		metricsOutputRepresentation.clear(); //Makes sure this list contains only metrics for current project
		
			try {
				for(IType type : compilationUnit.getAllTypes()){
					fetchAnnotations(type);
					for(IField field : type.getFields())
				    	fetchAnnotations(field);
				    for(IMethod method : type.getMethods()){
				    	fetchAnnotations(method);
				    	for(ILocalVariable parameter : method.getParameters())
				    	fetchAnnotations(parameter);
				    }
				}
				//All annotation classes has been fetched
				//Fetch LOCAD for each annotation
				for(IAnnotation annotation: anotList){
					try {
						metricsOutputRepresentation.add(new MetricOutputRepresentation("LOCAD", "Lines of Code for Annotation Declaration", numberOfLinesOfCode(annotation.getSource()), true, 
														annotation.getElementName(), IJavaElement.ANNOTATION)); 
						numLOCAD.add(numberOfLinesOfCode(annotation.getSource()));
					} catch (JavaModelException e) {
							e.printStackTrace();
					} catch (FileFormatException e) {
							e.printStackTrace();
					}
				}
				anotList.clear();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		return numLOCAD;
	}
	//ANL
	public int getNivelAninhamentoAnotacao(String projectName, String className,
	       								   String parentAnnotation, String targetAnnotation) {
		return 0;
	}
	
	//Fetch number of elements that can be annotated
	public List<Integer> getNumberElements(String projectName) {
	
		project = fetchProject(projectName);
		javaProject = JavaCore.create(project);
		List<Integer> elementsPerClass = new ArrayList<Integer>();
		int numberElements = 0;
		try {
			packages = javaProject.getPackageFragments();//Get all packages in the project
		} catch (JavaModelException e) {
			e.printStackTrace();
		} 
		
		for (IPackageFragment pacote : packages) {
			try {
				if (pacote.getKind() == IPackageFragmentRoot.K_SOURCE) {//Only interested in source code
					for(ICompilationUnit unit : pacote.getCompilationUnits()){
						for(IType type : unit.getAllTypes()){
							numberElements++; //The class declaration 
							IField[] fields = type.getFields();
							numberElements += fields.length;//Adds the number of members
							IMethod[] methods = type.getMethods();
							numberElements += methods.length;//Adds the number of methods
							for (IMethod method : methods){
								ILocalVariable[] parameters = method.getParameters();
								numberElements += parameters.length;//Adds the number of method's parameter
							}
						}
						elementsPerClass.add(numberElements);
						numberElements = 0;
					}
				}
				
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return elementsPerClass;
	}
	
	public List<Integer> getNumberAnnotatedElements(String projectName) {
	
		List<Integer> numberAnnotatedElements = new ArrayList<Integer>();
		int annotatedElements = 0;//The class is an element
		project = fetchProject(projectName);
		javaProject = JavaCore.create(project);
		
		try {
			packages = javaProject.getPackageFragments();//Get all packages in the project
		} catch (JavaModelException e) {
			e.printStackTrace();
		} 
		
		for (IPackageFragment pacote : packages) {
			try {
				if (pacote.getKind() == IPackageFragmentRoot.K_SOURCE) {//Only interested in source code
					for(ICompilationUnit unit : pacote.getCompilationUnits()){
						for(IType type : unit.getAllTypes()){
							if(type.getAnnotations().length != 0)
								annotatedElements++;//The class declaration is annotated
							for(IField field : type.getFields()){
								if(field.getAnnotations().length != 0)
									annotatedElements++;//The field declaration is annotated
							}
							for (IMethod method : type.getMethods()){
								if(method.getAnnotations().length != 0)
									annotatedElements++;//The method declaration is annotated
								//Check if the parameters are annotated
								for(ILocalVariable parameters : method.getParameters())
									if(parameters.getAnnotations().length != 0)
										annotatedElements++;//The parameter is annotated
							}
						}
						numberAnnotatedElements.add(annotatedElements);
						annotatedElements = 0;
					}
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return numberAnnotatedElements;
	}
	
	public List<Double> getAvgNumberAtt(String projectName) {
	
		int avgNumberAtt = 0, count = 0;
		project = fetchProject(projectName);
		javaProject = JavaCore.create(project);
		anotList = new ArrayList<IAnnotation>();
		List<Double> avgNumberAttPerClass = new ArrayList<Double>();
		try {
			packages = javaProject.getPackageFragments();//Get all packages in the project
		} catch (JavaModelException e) {
			e.printStackTrace();
		} 
		
		for (IPackageFragment pacote : packages) {
			try {
				if (pacote.getKind() == IPackageFragmentRoot.K_SOURCE) //Only interested in source code
					for(ICompilationUnit unit : pacote.getCompilationUnits()){
						for(IType type : unit.getAllTypes()){
				    		fetchAnnotations(type);
				    		for(IField field : type.getFields())
				    			fetchAnnotations(field);
				    		for(IMethod method : type.getMethods()){
				    			fetchAnnotations(method);
				    			for(ILocalVariable parameter : method.getParameters())
				    				fetchAnnotations(parameter);
				    		}
				    	}
						if(anotList==null || anotList.size() == 0){
							avgNumberAttPerClass.add((double) 0);//No annotations on this class, therefore no attributes
							continue;
						}else
							for(IAnnotation annotation: anotList){
								try {
									count++;
									avgNumberAtt += annotation.getMemberValuePairs().length;
								} catch (JavaModelException e) {
									e.printStackTrace();
								}
							}
						avgNumberAttPerClass.add(avgNumberAtt/((double)count));
						count = 0;
						avgNumberAtt = 0;
						anotList.clear();
					}
			} catch (JavaModelException e1) {
				e1.printStackTrace();
			}
		}
		return avgNumberAttPerClass;
	}
	
	
	//Inner helper methods
	private int fetchAnnotations(IMember member) throws JavaModelException {
		
		nestingCount = 0;
		
		if (member instanceof IType){
			annotations = ((IType) member).getAnnotations();
			for(IAnnotation annotation : annotations){
				anotList.add(annotation);
				nestingCount++;
				nestingCount = fetchNestedAnnotation(annotation, nestingCount);
			}
		} else if(member instanceof IField){
			annotations = ((IField) member).getAnnotations();
			for(IAnnotation annotation : annotations){
				anotList.add(annotation);
				nestingCount++;
				nestingCount = fetchNestedAnnotation(annotation, nestingCount);
			}
		} else if(member instanceof IMethod){
			annotations = ((IMethod) member).getAnnotations();
			for(IAnnotation annotation : annotations){
				anotList.add(annotation);
				nestingCount++;
				nestingCount = fetchNestedAnnotation(annotation, nestingCount);
			}
		}
		
		return nestingCount;
	}
	
	//For method's parameter
	private int fetchAnnotations(ILocalVariable parameter) throws JavaModelException {
		
		nestingCount = 0;
		
		annotations = parameter.getAnnotations();
		for(IAnnotation annotation : annotations){
			anotList.add(annotation);
			nestingCount++;
			nestingCount = fetchNestedAnnotation(annotation, nestingCount);
		}
		
		return nestingCount;
	}
	private int fetchNestedAnnotation(IAnnotation annotation, int nestingCount) {
		
		IMemberValuePair[] annotationAttr = null;
		
		try {
			annotationAttr = annotation.getMemberValuePairs();
		} catch (JavaModelException e) {
			e.printStackTrace();
		} 
		for(IMemberValuePair members : annotationAttr){
			
			if(members.getValueKind() == IMemberValuePair.K_ANNOTATION)
				if (members.getValue() instanceof Object[]) {
					Object[] innerAnnotations = (Object[]) members.getValue();
					for (Object o : innerAnnotations){
						fetchNestedAnnotation((IAnnotation) o, nestingCount);
					}
				} else {
					anotList.add((IAnnotation) members.getValue());
					nestingCount++;
				}
		}
		
		return nestingCount;
	}

	private List<IType> fetchAnnotatedClass(IJavaProject javaProject) {

		List<IType> classeList = new ArrayList<IType>();
		IMember member = null;
		
		try {
			packages = javaProject.getPackageFragments();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		for (IPackageFragment pacote : packages) {
			
			try {
				if (pacote.getKind() == IPackageFragmentRoot.K_SOURCE) {
				   for(ICompilationUnit unit : pacote.getCompilationUnits()){
					   for(IType type : unit.getAllTypes()){
						   	member = type;
							if (member instanceof IType)
								annotations = ((IType) member).getAnnotations();
				        	if(annotations.length != 0){
				        		classeList.add(type);
				        		break;
				        	}
				        	if(hasAnnotatedField(type)){
				        		classeList.add(type);
				        		break;
				        	}
				        	if(hasAnnotatedMethod(type)){
				        		classeList.add(type);
				        		break;
				        	}
					   }
					}
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		return classeList;
	
	
	}
	
	private boolean hasAnnotatedMethod(IType type) {
		try {
			for (IMethod method : type.getMethods()){
				member = method;
			    if (member instanceof IMethod){
			   		annotations = ((IMethod) member).getAnnotations();
			   		if(annotations.length != 0)
			   			return true;
			    }
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private boolean hasAnnotatedField(IType type) {
		
		try {
			for (IField field : type.getFields()){
				member = field;
			    if (member instanceof IField){
					annotations = ((IField) member).getAnnotations();
					if(annotations.length != 0)
			    		 return true;
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	private List<ICompilationUnit> fetchNumberOfClasses(IJavaProject javaProject) {

		List<ICompilationUnit> classeList = new ArrayList<ICompilationUnit>();
		
		try {
			packages = javaProject.getPackageFragments();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		for (IPackageFragment pacote : packages) {
			
			try {
				if (pacote.getKind() == IPackageFragmentRoot.K_SOURCE) {
				   for(ICompilationUnit unit : pacote.getCompilationUnits()){
					   if(unit.getElementName().contains(".java"))
						   classeList.add(unit);						   
					}
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		return classeList;
	
	}
}