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

public class AnnotationSnifferHandler extends AbstractHandler {

    AnnotationSniffer aSniffer = new AnnotationSniffer();    
	double numClasses, numClassesAnotadas, porcentagemClassesAnotadas, numClassesTotal, numClassesAnotadasTotal;
	List<Double> numAvgAttClass = new ArrayList<>();
	List<Integer> numElementsPerClass = new ArrayList<>();
	List<String> allValues = new ArrayList<>();
	List<String> data = new ArrayList<String>();
	GenerateCSV csvWriter = new GenerateCSV();
	private List<Integer> numLOCAD = new ArrayList<>();
	private List<Integer> numAED = new ArrayList<>();
	private List<Integer> numNAECs = new ArrayList<>();
	private List<Integer> numAA = new ArrayList<>();
	private List<Integer> numAC = new ArrayList<>();
	private List<Integer> numUAC = new ArrayList<>();
	private String metricsAlias[] = {"AC", "LOCAD", "AED", "AA", "UAC"};
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
                // Get the root of the workspace
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
                IWorkspaceRoot root = workspace.getRoot();
                String projectName = "";
                String cwdPath = System.getProperty("user.dir");
                // Get all projects in the workspace
                IProject[] projects = root.getProjects();
                // Loop over all projects
                IJavaProject javaProject = null;
                IPackageFragment[] packages = null;
                
                data.clear();
                allValues.clear();
                numClassesTotal = 0;
                numClassesAnotadasTotal = 0;
                
                for (IProject project : projects) {
                        try {
                            projectName = project.getName();
                            //Must be a java project
                            if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")){
                               	javaProject = JavaCore.create(project);
                               	packages = javaProject.getPackageFragments();
                               	//Creates XML file (beginning) for each metric
                               	for(String metricAlias : metricsAlias){
                               		XmlUtils.writeXMLBeginning(projectName, metricAlias);
                               	}
                               	for (IPackageFragment package_ : packages) {
                            		try {
                            			if (package_.getKind() == IPackageFragmentRoot.K_SOURCE){//Only source code
                            				if(package_.getCompilationUnits().length != 0){//The package has no compilation unit
                            					for(String metricAlias : metricsAlias){
                            						XmlUtils.writePackage(package_.getElementName(), projectName, metricAlias);
                                               	}
                                				//For each compilation unit, fetch all metrics
                                				for(ICompilationUnit unit : package_.getCompilationUnits()){
                                					for(String metricAlias : metricsAlias){
                                						XmlUtils.writeClass(unit.getElementName(), projectName, metricAlias);
                                                   	}
                                					numAC.add(aSniffer.getAC(unit));
                                					XmlUtils.writeXml(aSniffer.getMetricsOutputRepresentation(), projectName, "AC");
                                					numLOCAD.addAll(aSniffer.getLOCAD(unit));
                                					XmlUtils.writeXml(aSniffer.getMetricsOutputRepresentation(), projectName, "LOCAD");
                                					numAED.addAll(aSniffer.getAED(unit));
                                					XmlUtils.writeXml(aSniffer.getMetricsOutputRepresentation(), projectName, "AED");
                                					numAA.addAll(aSniffer.getAA(unit));
                                					XmlUtils.writeXml(aSniffer.getMetricsOutputRepresentation(), projectName, "AA");
                                					numUAC.add(aSniffer.getUAC(unit));
                                					XmlUtils.writeXml(aSniffer.getMetricsOutputRepresentation(), projectName, "UAC");
                                					
                                				}	
                            				}
                            			}
                            		}catch (Exception e) {
										e.printStackTrace();
									}
                               	}				
                             }
                        } catch (CoreException e) {
                                e.printStackTrace();
                        }
                        //Finishes the XML file
                        //XmlUtils.writeXml(aSniffer.getMetricsOutputRepresentation(), projectName);
                        for(String metricAlias : metricsAlias){
                        	XmlUtils.writeXMLEnd(projectName, metricAlias);
                       	}
                }
                
                MessageDialog.openInformation(
        				window.getShell(),
        				"Annotation Sniffer",
        				"Concluido! ");
                return null;
        }
}
