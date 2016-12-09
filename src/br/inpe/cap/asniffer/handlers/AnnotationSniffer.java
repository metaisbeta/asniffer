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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
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

	public List<Integer> getAC(String nameProject) {
		
		project = fetchProject(nameProject);
		javaProject = JavaCore.create(project);
		anotList = new ArrayList<IAnnotation>();
		List<Integer> ac = new ArrayList<>();
		metricsOutputRepresentation.clear(); //Makes sure this list contains only metrics for current project
		
		try {
			packages = javaProject.getPackageFragments();
		} catch (JavaModelException e) {
			e.printStackTrace();
		} 
		
		for (IPackageFragment package_ : packages) {
			try {
				if (package_.getKind() == IPackageFragmentRoot.K_SOURCE)//Only source code
				    for(ICompilationUnit unit : package_.getCompilationUnits()){
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
				    //All annotation classes has been fetched
				    ac.add(anotList.size());
				    //Save Metric Representation
				    metricsOutputRepresentation.add(new MetricOutputRepresentation(package_.getElementName(), unit.getElementName(), "AC","Annotations in Class",anotList.size()));
				    //Clear all annotations fetched
				    anotList.clear();
				    }
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return ac;
	}

	public List<Integer> getUAC(String nameProject) {
		project = fetchProject(nameProject);
		javaProject = JavaCore.create(project);
		anotSet = new HashSet<String>();
		anotList = new ArrayList<IAnnotation>();
		List<Integer> uac = new ArrayList<>();
		StringBuilder uacBuilder = new StringBuilder();
		try {
			packages = javaProject.getPackageFragments();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		for (IPackageFragment package_ : packages) {
			try {
				if (package_.getKind() == IPackageFragmentRoot.K_SOURCE)//Only source code
				    for(ICompilationUnit unit : package_.getCompilationUnits()){
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
				    //All annotation classes has been fetched
				    //Save only the unique ones
				    anotSet = fetchUAC(uacBuilder);
				    //anotSet.addAll(anotList);
				    uac.add(anotSet.size());
				    anotList.clear();//Clear all annotations fetched
				    anotSet.clear();//Clear the array holding the unique annotations
				    }
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
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
					uacBuilder.append(attr.getMemberName());
					uacBuilder.append("=");
					uacBuilder.append(attr.getValue());
				}
				uacBuilder.append(annotation.getElementName());
				uacNames.add(uacBuilder.toString());
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return uacNames;
	}

	public int getNumeroAnotacaoElemento(String nameProject, String nomeClasse, String nomeElemento) {
		 project = fetchProject(nameProject);
		javaProject = JavaCore.create(project);
		
		anotList = new ArrayList<IAnnotation>();
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
				        	if(unit.getElementName().equals(nomeClasse)){
				        		
				        		for(IType type : unit.getAllTypes()){
				        			
				        			if(type.getElementName().equals(nomeElemento)){
				        				annotations = ((IType) member).getAnnotations();
			        					for(IAnnotation annotation : annotations)
			        						anotList.add(annotation);
				        			}
				        			for (IField field : type.getFields()){
				        				member = field;
					        			if (member instanceof IField && member.getElementName().equals(nomeElemento)){
					        				annotations = ((IField) member).getAnnotations();
					        				for(IAnnotation annotation : annotations)
					        					anotList.add(annotation);
					        			}
					        				
				        			}
				        			for (IMethod method : type.getMethods()){
				        				member = method;
				        				if (member instanceof IMethod && member.getElementName().equals(nomeElemento)){
						        				annotations = ((IMethod) member).getAnnotations();
					        				for(IAnnotation annotation : annotations)
					        					anotList.add(annotation);
				        				}
				        			}
				        		}
				        						        		
				        		return anotList.size();
				    		}
						}
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		return 0;
	}
	
	public int getNumClasses(String nameProject) {

		project = fetchProject(nameProject);
		javaProject = JavaCore.create(project);
		
		List<ICompilationUnit> classesList = new ArrayList<ICompilationUnit>();
		classesList = buscaClassesproject(javaProject);
		
		return classesList.size();
		
	}

	public int getAnnotatedClass(String nameProject) {
		
		project = fetchProject(nameProject);
		javaProject = JavaCore.create(project);
		List<IType> classesList = new ArrayList<IType>();
		classesList = fetchAnnotatedClass(javaProject);
		
		return classesList.size();
	}
	
	//AA
	public int getNumAtributosAnotacao(String projectName, String className, String annotationName) {
		
		project = fetchProject(projectName);
		javaProject = JavaCore.create(project);
		anotList = new ArrayList<IAnnotation>();
		try {
			packages = javaProject.getPackageFragments();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		for (IPackageFragment pacote : packages) {
			//fetchAnnotations(javaProject);
			if(anotList!=null)
				break;
		}
		
		for(IAnnotation annotation: anotList){
			if(annotation.getElementName().equals(annotationName))
				try {
					return annotation.getMemberValuePairs().length;
				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return 0;
	}

	//LOCAD
	public void getLOCAD(String projectName) {
		
		project = fetchProject(projectName);
		javaProject = JavaCore.create(project);
		anotList = new ArrayList<IAnnotation>();
		metricsOutputRepresentation.clear(); //Makes sure this list contains only metrics for current project
		try {
			packages = javaProject.getPackageFragments();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}  
		
		for (IPackageFragment package_ : packages) {
			try {
				if (package_.getKind() == IPackageFragmentRoot.K_SOURCE)//Only source code
				    for(ICompilationUnit unit : package_.getCompilationUnits()){
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
				    	//All annotation classes has been fetched
					    for(IAnnotation annotation: anotList){
								try {
									metricsOutputRepresentation.add(new MetricOutputRepresentation(package_.getElementName(), unit.getElementName(), 
																		"LOCAD", "Lines of Code for Annotation Declaration", numberOfLinesOfCode(annotation.getSource()), true, 
																		annotation.getElementName(), "Annotation")); 
								} catch (JavaModelException e) {
									e.printStackTrace();
								} catch (FileFormatException e) {
									e.printStackTrace();
								}
						}
					    anotList.clear();
				    }
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
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
	private void fetchAnnotations(IMember member) throws JavaModelException {
		
		if (member instanceof IType){
			annotations = ((IType) member).getAnnotations();
			for(IAnnotation annotation : annotations){
				anotList.add(annotation);
				fetchNestedAnnotation(annotation);
			}
		} else if(member instanceof IField){
			annotations = ((IField) member).getAnnotations();
			for(IAnnotation annotation : annotations){
				anotList.add(annotation);
				fetchNestedAnnotation(annotation);
			}
		} else if(member instanceof IMethod){
			annotations = ((IMethod) member).getAnnotations();
			for(IAnnotation annotation : annotations){
				anotList.add(annotation);
				fetchNestedAnnotation(annotation);
			}
		}
	}
	
	//For method's parameter
	private void fetchAnnotations(ILocalVariable parameter) throws JavaModelException {
		annotations = parameter.getAnnotations();
		for(IAnnotation annotation : annotations){
			anotList.add(annotation);
			fetchNestedAnnotation(annotation);
		}
	}
	private void fetchNestedAnnotation(IAnnotation annotation) {
		
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
						fetchNestedAnnotation((IAnnotation) o);
					}
				} else {
					anotList.add((IAnnotation) members.getValue());
				}
		}
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

	private List<ICompilationUnit> buscaClassesproject(IJavaProject javaProject) {

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