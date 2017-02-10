package br.inpe.cap.asniffer.handlers;

import java.util.ArrayList;
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
import org.eclipse.jdt.core.IImportDeclaration;
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
import br.inpe.cap.asniffer.output.MetricRepresentation;
import br.inpe.cap.asniffer.util.XmlUtils;

import static br.inpe.cap.asniffer.util.UnitParser.numberOfLinesOfCode;

public class AnnotationSniffer {
	int count = 0;
	
	int overallAc = 0, overallAA = 0, overallLOCAD = 0;
	IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot rootWorkspace = workspace.getRoot();
    IAnnotation[] annotations = new IAnnotation[0];
	List<IAnnotation> anotList;
	Set<String> anotSet;
	IPackageFragment[] packages = null;
	IMember member = null;
	IProject project;
	IJavaProject javaProject;
	Set<String> uacNames = new HashSet<>();
	List<MetricRepresentation> metricsOutputRepresentation = new ArrayList<>();
	int nestingCount = 0;
	private List<String> elementNames = new ArrayList<>();
	private List<Integer> elementType = new ArrayList<>();
	private List<Integer> elementValues = new ArrayList<>(); 
	
	public List<MetricRepresentation> getMetricsOutputRepresentation() {
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

	public MetricRepresentation getAC(ICompilationUnit compilationUnit) {
		
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
			//Clear all annotations fetched
			anotList.clear();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		//GAMBI
		overallAc += ac;
		//All annotation classes has been fetched
		System.out.println("Nome da classe: " + compilationUnit.getElementName());
		System.out.println("AC: " + ac);
		System.out.println("Cummulative AC: " + overallAc);
		return new MetricRepresentation("AC","Annotations in Class",ac);
	}

	public MetricRepresentation getUAC(ICompilationUnit compilationUnit) {
		
		count++;
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
			uac = (fetchUAC(uacBuilder).size());
			//Clear all annotations fetched
			anotList.clear();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		return new MetricRepresentation("UAC","Unique Annotations in Class",uac);
	}

	private Set<String> fetchUAC(StringBuilder uacBuilder) {
		
		Set<String> uacNames = new HashSet<>();
		String attributes = "teste";
		//Check the array anotList, and retrives only distinct annotations
		for(IAnnotation annotation : anotList){
			uacBuilder.delete(0, uacBuilder.length());//Clear string buffer, for better performance
			try {
				uacBuilder.append(annotation.getSource().trim().replaceAll("\n", ""));
				uacNames.add(uacBuilder.toString());
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return uacNames;
	}

	public MetricRepresentation getAED(ICompilationUnit compilationUnit) {
			
		Map<IJavaElement, Integer> annotMap = new HashMap<>();
		anotList = new ArrayList<>();
		int numAEDPerElement;
		elementNames.clear();
		elementType.clear();
		elementValues.clear(); 
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
				elementNames.add(entry.getKey().getElementName());
				elementType.add(entry.getKey().getElementType());
				elementValues.add(entry.getValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MetricRepresentation("AED", "Annotations in Element Declaration", 
								elementValues, elementNames, elementType);
	}
	
	//AA
	public MetricRepresentation getAA(ICompilationUnit compilationUnit) {
		anotList = new ArrayList<>();
		elementNames.clear();
		elementType.clear();
		elementValues.clear();

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
				elementNames.add(annotation.getElementName());
				elementType.add(IType.ANNOTATION);
				elementValues.add(annotation.getMemberValuePairs().length);
			}
			anotList.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MetricRepresentation("AA", "Attributes in Annotations", elementValues, 
											elementNames, elementType);
	}
	
	public MetricRepresentation getASC(ICompilationUnit compilationUnit){
		
		anotList = new ArrayList<IAnnotation>();
		Set<String> ascSchemas = new HashSet<>();
		int asc = 0;
		
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
			for (IAnnotation annotation : anotList) {
				String annotationName = annotation.getElementName();
				for (IImportDeclaration impDcl : compilationUnit.getImports())
					if (!annotationName.contains(".")) {
						String aName = getSimpleClassName(impDcl);
						if (annotationName.equals(aName))
							ascSchemas.add(getSchema(impDcl));
					} else if (annotationName.equals(impDcl.getElementName()))
						ascSchemas.add(getSchema(impDcl));
			}
			//All annotation classes has been fetched
			asc = ascSchemas.size();
			//Clear all annotations fetched
			anotList.clear();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		return new MetricRepresentation("ASC", "Annotations Schemas in Class", asc);
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
	
	public MetricRepresentation getANL(ICompilationUnit compilationUnit) {
	
		anotList = new ArrayList<IAnnotation>();
		metricsOutputRepresentation.clear(); //Makes sure this list contains only metrics for current project
		List<Integer> numANL = new ArrayList<>();
		Map<IAnnotation, Integer> anlMap = new HashMap<>();
		List<String> anlList = new ArrayList<>();
		elementNames.clear();
		elementType.clear();
		elementValues.clear();
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
			//Clear all annotations fetched
			for(IAnnotation annotation : anotList){
				String attributes[] = annotation.toString().split(" ");
				for(String att : attributes){
					if(att.contains("@"))
						anlList.add(att);
				}
				anlMap.put(annotation, anlList.size() - 1);//ANL starts at level 0
				anlList.clear();
			}
			Set<Map.Entry<IAnnotation, Integer>> entries = anlMap.entrySet();
			for(Map.Entry<IAnnotation, Integer> entry : entries){
				elementNames.add(entry.getKey().getElementName());
				elementType.add(entry.getKey().getElementType());
				elementValues.add(entry.getValue());
			}
			anotList.clear();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return new MetricRepresentation("ANL", "Annotation Nesting Level", 
									elementValues, elementNames, elementType);
	}

	//LOCAD
	public MetricRepresentation getLOCAD(ICompilationUnit compilationUnit) {
		
		anotList = new ArrayList<IAnnotation>();
		metricsOutputRepresentation.clear(); //Makes sure this list contains only metrics for current project
		elementNames.clear();
		elementType.clear();
		elementValues.clear(); 
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
						elementNames.add(annotation.getElementName());
						elementType.add(IJavaElement.ANNOTATION);
						elementValues.add(numberOfLinesOfCode(annotation.getSource()));
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
		return new MetricRepresentation("LOCAD", "Lines of Code in Annotation Declaration",
						elementValues, elementNames,elementType);
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

	private String resolverInnerAnnotations(){
		
		return null;
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
				e.printStackTrace();
			}
		}
		return classeList;
	
	}
	
	private String getSimpleClassName(IImportDeclaration impDcl) {
		String importName = impDcl.getElementName();
		String[] tmp = importName.split("\\.");
		return tmp[tmp.length - 1];
	}
	
	private String getSchema(IImportDeclaration impDcl) {
		String importName = impDcl.getElementName();
		String[] tmp = importName.split("\\.");
		String schema = "";
		for (int i = 0; i < tmp.length - 2; i++)
			schema += tmp[i] + ".";
		schema += tmp[tmp.length -2];
		return schema;
	}
	
}