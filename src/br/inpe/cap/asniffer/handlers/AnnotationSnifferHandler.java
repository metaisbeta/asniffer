package br.inpe.cap.asniffer.handlers;

import java.util.ArrayList;
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
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import br.inpe.cap.asniffer.output.ClassRepresentation;
import br.inpe.cap.asniffer.output.GenerateCSV;
import br.inpe.cap.asniffer.output.OutputRepresentation;
import br.inpe.cap.asniffer.output.MetricRepresentation;
import br.inpe.cap.asniffer.output.PackageRepresentation;
import br.inpe.cap.asniffer.util.XmlUtils;

public class AnnotationSnifferHandler extends AbstractHandler {

    AnnotationSniffer aSniffer = new AnnotationSniffer();    
	GenerateCSV csvWriter = new GenerateCSV();
	
	private List<ClassRepresentation> classOutputRep = new ArrayList<>();
	private List<PackageRepresentation> packageOutputRep = new ArrayList<>();
	private OutputRepresentation metricOutputRep = null;
	private List<MetricRepresentation> metricRep = new ArrayList<>();
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
                // Get the root of the workspace
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
                IWorkspaceRoot root = workspace.getRoot();
                String projectName = "";
                //String cwdPath = System.getProperty("user.dir");
                // Get all projects in the workspace
                IProject[] projects = root.getProjects();
                // Loop over all projects
                IJavaProject javaProject = null;
                IPackageFragment[] packages = null;
                MessageDialog.openInformation(
        				window.getShell(),
        				"Annotation Sniffer",
        				"Begin!");
                for (IProject project : projects) {
                        try {
                            projectName = project.getName();
                            //Must be a java project
                            if (project.isNatureEnabled("org.eclipse.jdt.core.javanature") && project.isOpen()){
                            	javaProject = JavaCore.create(project);
                               	packages = javaProject.getPackageFragments();
                               	for (IPackageFragment package_ : packages) {
                            		try {
                            			if (package_.getKind() == IPackageFragmentRoot.K_SOURCE){//Only source code
                            				if(package_.getCompilationUnits().length != 0){//The package has no compilation unit
                                				//For each compilation unit, fetch all metrics
                            					for(ICompilationUnit unit : package_.getCompilationUnits()){
                                					MetricRepresentation metric = aSniffer.getAC(unit);
                                					metricRep.add(metric);
                            						if(metric.getSingleMetricValue()!=0){
                                    					metricRep.add(aSniffer.getUAC(unit));
                                    					metricRep.add(aSniffer.getASC(unit));
                                    					metricRep.add(aSniffer.getLOCAD(unit));
                                    					metricRep.add(aSniffer.getAED(unit));
                                    					metricRep.add(aSniffer.getAA(unit));
                                    					metricRep.add(aSniffer.getANL(unit));
                            						}
                                					classOutputRep.add(new ClassRepresentation(metricRep, unit.getElementName()));
                                					metricRep.clear();
                            					}
                                				//At this point all metrics for all compilation units inside a package has been fetched
                                        		//XmlUtils.prepareXML(packages_, fileName, projectName)
                                        		packageOutputRep.add(new PackageRepresentation(package_.getElementName(), classOutputRep));
                                        		classOutputRep.clear();
                            				}
                            			}
                            		}catch (Exception e) {
										e.printStackTrace();
									}
                               	}
                               	//At this point all metrics have been fetched for a specific project
                               	metricOutputRep = new OutputRepresentation(packageOutputRep, projectName);
                               	packageOutputRep.clear();
                               	XmlUtils.writeXML(metricOutputRep);
                               	}
                        } catch (CoreException e) {
                                e.printStackTrace();
                        }
                }
                //Writes a csv file
                /*for (Integer num : numUAC)
		  	  	  		allValues.add(String.valueOf(num));
                csvWriter.writeCSV("AnnotationUsage", "UAC.csv", cwdPath + "/output/", allValues, 100000000);
                allValues.clear();*/
                MessageDialog.openInformation(
        				window.getShell(),
        				"Annotation Sniffer",
        				"Finished! ");
                return null;
        }
}
