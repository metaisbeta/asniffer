package br.inpe.cap.asniffer.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import br.inpe.cap.asniffer.output.GenerateCSV;
import br.inpe.cap.asniffer.output.MetricOutputRepresentation;
import br.inpe.cap.asniffer.util.XmlUtils;

public class SampleHandler extends AbstractHandler {

    AnnotationSniffer aSniffer = new AnnotationSniffer();    
	double numClasses, numClassesAnotadas, porcentagemClassesAnotadas, numClassesTotal, numClassesAnotadasTotal;
	List<Double> numAvgAttClass = new ArrayList<>();
	List<Integer> numElementsPerClass = new ArrayList<>();
	List<String> allValues = new ArrayList<>();
	List<String> data = new ArrayList<String>();
	GenerateCSV csvWriter = new GenerateCSV();
	private List<Integer> numLinesOfCode = new ArrayList<>();
	private List<Integer> numNAECs = new ArrayList<>();
	private List<Integer> numAC = new ArrayList<>();
	private List<Integer> numUAC = new ArrayList<>();
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
                // Get the root of the workspace
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
                IWorkspaceRoot root = workspace.getRoot();
                String nameProject = "";
                String cwdPath = System.getProperty("user.dir");
                // Get all projects in the workspace
                IProject[] projects = root.getProjects();
                // Loop over all projects
                data.clear();
                allValues.clear();
                numClassesTotal = 0;
                numClassesAnotadasTotal = 0;
                
                for (IProject project : projects) {
                        try {
                                nameProject = project.getName();
                                // check if we have a Java project
                                /*MessageDialog.openInformation(
                        				window.getShell(),
                        				"Project",
                        				nameProject);*/
                                if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")){
                                /*	numClasses = aSniffer.getNumClasses(nameProject);
                                	numClassesAnotadas = aSniffer.getAnnotatedClass(nameProject);
                                	numAvgAttClass = aSniffer.getAvgNumberAtt(nameProject);
                                	numElementsPerClass = aSniffer.getNumberElements(nameProject);
                                	numLinesOfCode  = aSniffer.getLOC(nameProject);
                                	numNAECs  = aSniffer.getNumberAnnotatedElements(nameProject);*/
                                	numAC = aSniffer.getAC(nameProject);
                                	aSniffer.getLOCAD(nameProject);
                                	/*numUAC = aSniffer.getUAC(nameProject);
                                	porcentagemClassesAnotadas = (numClassesAnotadas/numClasses) * 100;
                                	numClassesAnotadasTotal += numClassesAnotadas;
                                	numClassesTotal += numClasses;
                                	for (Double avgElements : numAvgAttClass) 
                                		  allValues.add(String.valueOf(avgElements)); 
                            		MessageDialog.openInformation(
                            				window.getShell(),
                            				"Project",
                            				"AVG");
                            		for (Integer numElements : numElementsPerClass)
                              		  	  allValues.add(String.valueOf(numElements)); 
                            		MessageDialog.openInformation(
                            				window.getShell(),
                            				"Project",
                            				"NEC");
                            		for (Integer numLOC : numLinesOfCode)
                            		  	  allValues.add(String.valueOf(numLOC));
                            		MessageDialog.openInformation(
                            				window.getShell(),
                            				"Project",
                            				"LOC");
                            		for (Integer numNAEC : numNAECs)
                          		  	  	  allValues.add(String.valueOf(numNAEC)); 
                            		MessageDialog.openInformation(
                            				window.getShell(),
                            				"Project",
                            				"NAEC");*/
                            		/*for (Integer num : numAC)
                        		  	  	  allValues.add(String.valueOf(num)); 
                            		MessageDialog.openInformation(
                            				window.getShell(),
                            				"Project",
                            				"AC");*/
                            		/*for (Integer num : numUAC)
                        		  	  	  allValues.add(String.valueOf(num)); 
                            		MessageDialog.openInformation(
                            			window.getShell(),
                            			"Project",
                            			 "UAC");
                            		/*data.add(nomeProjeto);
                                	data.add(String.valueOf(numClasses));
                                	data.add(String.valueOf(numClassesAnotadas));
                                	data.add(String.valueOf(porcentagemClassesAnotadas));
                                	MessageDialog.openInformation(
                            				window.getShell(),
                            				"Annotation Sniffer",
                            				"Nome do projeto: " + nomeProjeto + "\n"
                            				+"Numero de Classes: "+ numClasses + "\n"
                            				+"Numero de Classes Anotadas: " + numClassesAnotadas + "\n"
                            				+"Porcentagem de Classes Anotadas: " + porcentagemClassesAnotadas + "\n"
                            				+"CWD" + cwdPath + "\n"
                            				);*/
                                	//csvWriter.writeCSV("AnnotationUsage", "AnnotationUsage.csv", cwdPath + "/output/", data,4);
                                System.out.println("Working on project: " + nameProject);
                            	csvWriter.writeCSV("AnnotationUsage", "AnnotationUsage.csv", cwdPath + "/output/", allValues, (int) numClasses);
                                }	
                        } catch (CoreException e) {
                                e.printStackTrace();
                        }
                        XmlUtils.writeXml(aSniffer.getMetricsOutputRepresentation(), nameProject);
                }
                
                //write csv with total values and mean
                //data.clear();
                /*data.add("Summary");
                data.add(String.valueOf(numClassesTotal));
                data.add(String.valueOf(numClassesAnotadasTotal));
                data.add(String.valueOf((numClassesAnotadasTotal/numClassesTotal) * 100));
                */
                //csvWriter.writeCSV("AnnotationUsage", "AnnotationUsage.csv", cwdPath + "/output/", allValues,4);
                MessageDialog.openInformation(
        				window.getShell(),
        				"Annotation Sniffer",
        				"Concluido! ");
                return null;
        }
        /*private void printProjectInfo(IProject project) throws CoreException,
                        JavaModelException {
                System.out.println("Working in project " + project.getName());
                // check if we have a Java project
                if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
                        IJavaProject javaProject = JavaCore.create(project);
                        printPackageInfos(javaProject);
                }
        }

        private void printPackageInfos(IJavaProject javaProject)
                        throws JavaModelException {
                IPackageFragment[] packages = javaProject.getPackageFragments();
                for (IPackageFragment mypackage : packages) {
                        // Package fragments include all packages in the
                        // classpath
                        // We will only look at the package from the source
                        // folder
                        // K_BINARY would include also included JARS, e.g.
                        // rt.jar
                        if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
                                System.out.println("Package " + mypackage.getElementName());
                                printICompilationUnitInfo(mypackage);

                        }

                }
        }

        private void printICompilationUnitInfo(IPackageFragment mypackage)
                        throws JavaModelException {
                for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
                        printCompilationUnitDetails(unit);

                }
        }

        private void printIMethods(ICompilationUnit unit) throws JavaModelException {
                IType[] allTypes = unit.getAllTypes();
                for (IType type : allTypes) {
                        printIMethodDetails(type);
                }
        }

        private void printCompilationUnitDetails(ICompilationUnit unit)
                        throws JavaModelException {
                System.out.println("Source file " + unit.getElementName());
                Document doc = new Document(unit.getSource());
                System.out.println("Has number of lines: " + doc.getNumberOfLines());
                printIMethods(unit);
        }

        private void printIMethodDetails(IType type) throws JavaModelException {
                IMethod[] methods = type.getMethods();
                for (IMethod method : methods) {

                        System.out.println("Method name " + method.getElementName());
                        System.out.println("Signature " + method.getSignature());
                        System.out.println("Return Type " + method.getReturnType());

                }
        }*/
}
