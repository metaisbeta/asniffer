package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
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
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.inpe.cap.asniffer.handlers.AnnotationSniffer;
import br.inpe.cap.asniffer.output.MetricRepresentation;
import br.inpe.cap.asniffer.util.XmlUtils;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestPlugin {

private static SWTWorkbenchBot bot;
private static AnnotationSniffer aSniffer;
private static SWTBotShell shell;
private static IProject[] projects;
private static IJavaProject javaProject = null;
private static IPackageFragment[] packages = null;
private static IWorkspace workspace;
private static IWorkspaceRoot root;
private static List<ICompilationUnit> compilationUnits = new ArrayList<>();

	@BeforeClass
	public static void beforeClass(){
		String projectName = "";
		bot = new SWTWorkbenchBot();
		aSniffer = new AnnotationSniffer();
		try {
			//Close the welcome screen
			bot.viewByTitle("Welcome").close();
			
		} catch (WidgetNotFoundException e) {
		}
		
		//Create a Java project to be tested
		createTestProject("TestPlugin");
		bot.sleep(5000);
		workspace = ResourcesPlugin.getWorkspace();
		root = workspace.getRoot();
		projects = root.getProjects();
		//Now fetches the workspace for all projects and get the compilation units
		for (IProject project : projects) {
             try {
                 projectName = project.getName();
                 //Must be a java project
                 if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")){
                   	javaProject = JavaCore.create(project);
                   	packages = javaProject.getPackageFragments();
                   	for (IPackageFragment package_ : packages) {
               			try {
               				if (package_.getKind() == IPackageFragmentRoot.K_SOURCE){//Only source code
               					//For each compilation unit, fetch all metrics
               					for(ICompilationUnit unit : package_.getCompilationUnits()){
               						compilationUnits.add(unit);//Compilation Units are ready to serve as test inputs
               					}
                 				
                 			}
                 					
                 		}catch (Exception e) {
								e.printStackTrace();
						}
                 					
                    }	
                 }
             }catch (Exception e) {
            	 e.printStackTrace();
             }
		}
	}
		
	private static void createTestProject(String nameProject) {
		
		 workspace = ResourcesPlugin.getWorkspace();
		 root = workspace.getRoot();
		 
         // Get all projects in the workspace
         IProject[] projects = root.getProjects();
         
         String[] namePackages = {"br.inpe.cap.pacote1"};
         
         
         if(!root.getProject("TestPlugin").exists()){
	         bot.menu("File").menu("New").menu("Project...").click();
	 		 shell = bot.shell("New Project");
	 		 shell.activate();
	 		 bot.tree().expandNode("Java").select("Java Project");
	 		 bot.button("Next >").click();
	 		 bot.textWithLabel("Project name:").
	 			setText("TestPlugin");
	 		 bot.button("Finish").click();
	 		 bot.sleep(7000);
	 		 workspace = ResourcesPlugin.getWorkspace();
	         root = workspace.getRoot();
	         projects = root.getProjects();
	         for (IProject project : projects) {
	             try {
	                     // only work on open projects with the Java nature
	                     if (project.isOpen() & project.isNatureEnabled(JavaCore.NATURE_ID)
	                    	 & project.getName().equals(nameProject)) {
	                             createPackage(project, namePackages);
	                     }
	             } catch (CoreException e) {
	                     e.printStackTrace();
	             }
	         }
         }
	}

	private static void createPackage(IProject project, String[] namePackages) 
					throws JavaModelException {
		Map<String, String> compilationUnitNames = new HashMap<String, String>();
		compilationUnitNames = fillCompilationMap();
		IJavaProject javaProject = JavaCore.create(project);
		List<IPackageFragment> fragment = new ArrayList<IPackageFragment>();
        IFolder folder = project.getFolder("src");
        // folder.create(true, true, null);
        IPackageFragmentRoot srcFolder = javaProject
                        .getPackageFragmentRoot(folder);
        for(String packages : namePackages){
        	fragment.add(srcFolder.createPackageFragment(
        			packages, true, null));
        }
        createClass(javaProject, fragment, compilationUnitNames);
	}

	private static Map<String, String> fillCompilationMap() {
		Map<String, String> compilationUnitNames = new HashMap<String, String>();
		
		for(int i = 0; i < 3; i++)
			compilationUnitNames.put("Classe"+(i+1),"Class");
		for(int i = 0; i < 4; i++)
			compilationUnitNames.put("Annotation"+(i+1), "Annotation");
		
		return compilationUnitNames;
	}

	private static void createClass(IJavaProject javaProject, List<IPackageFragment> fragments, 
			Map<String, String> compilationUnitNames) {
			Iterator it = compilationUnitNames.entrySet().iterator();
		    
			while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        String contents = classContents(pair.getKey().toString(), fragments.get(0).getElementName()); //Fetch a string representing a compilation unit
		        try {
					fragments.get(0).createCompilationUnit(pair.getKey().toString()+".java", contents, true, null);
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
		        it.remove(); // avoids a ConcurrentModificationException
		    }
	}

	private static String classContents(String compilationUnitName, String packageName) {
		StringBuffer buf= new StringBuffer();
		buf.append("package " + packageName+";\n");
		if(compilationUnitName.contains("Class")){
			buf.append("@Annotation1\n");
			buf.append("public class " + compilationUnitName+"{\r\n");
		}else
			buf.append("public @interface " + compilationUnitName+"{\r\n");
		
		switch (compilationUnitName){
		
			case "Classe1":
				buf.append("@Annotation4(arrayValue = {1,2,3,4})\n");
				buf.append("private int y;\n");
				buf.append("@Annotation4(arrayValue = {1,6,3,4})\n");
				buf.append("private int z;\n");
				break;
			case "Classe2":
				buf.append("final int xy = 3;\n");
				buf.append("@Annotation1\n");
				buf.append("@Annotation2(value1 =  2,\n");
				buf.append("value2 = 3,\n"); 
				buf.append("value3 =\"String de teste: \" +  xy)\n"); 
				buf.append("private int a;\n");
				buf.append("@Annotation1\n");
				buf.append("@Annotation2(value1 = 1,\n");
				buf.append("value2 = 2,\n"); 
				buf.append("value3 = \"String de teste: \" + xy)\n"); 
				buf.append("private int b;\n");
				break;
			case "Classe3":
				buf.append("@Annotation1\n");
				buf.append("private int c;\n");
				buf.append("@Annotation1\n");
				buf.append("private int d;\n");
				buf.append("@Annotation3(value3 = @Annotation1)\n");
				buf.append("private char e;\n");
				buf.append("@Annotation3(value3 = @Annotation1)\n");
				buf.append("public void setC(@Annotation1 int c){");
				buf.append("this.c = c;");
				buf.append("}");
				break;
			case "Annotation1":
				break;
			case "Annotation2":
				buf.append("int value1();\n");
				buf.append("double value2();\n");
				buf.append("String value3();\n");
				break;
			case "Annotation3":
				buf.append("Annotation1 value3();\n");
				break;
			case "Annotation4":
				buf.append("int[] arrayValue();\n");
		}
		buf.append("}");
		String content= buf.toString();
		return content;
	}
	
	//Begin Tests
	//LOC
	@Test
	public void testLOC(){
		
		List<Integer> numberOfLines = new ArrayList<Integer>();
		numberOfLines  = aSniffer.getLOC("TestPlugin");
		assertEquals(7, numberOfLines.size());
		
	}
	//AC
	@Test
	public void testAC(){

		int expectedAC[] = {0,0,0,3,8,5,0};
		int[] numACArray = {0,0,0,0,0,0,0};
		List<MetricRepresentation> acMetricOutput = new ArrayList<>(); 
		for(ICompilationUnit compilationUnit : compilationUnits){
			acMetricOutput.add(aSniffer.getAC(compilationUnit));
		}
		for(int i = 0; i < acMetricOutput.size(); i++){
			numACArray[i] = (int) acMetricOutput.get(i).getSingleMetricValue();
		}
		Arrays.sort(expectedAC);
		Arrays.sort(numACArray);
		assertArrayEquals(expectedAC, numACArray);

	}
	//UAC
	@Test
	public void testUAC(){
		
		int expectedUAC[] = {0,0,0,3,2,3,0};
		int[] numUACArray = {0,0,0,0,0,0,0};
		List<MetricRepresentation> uacMetricOutput = new ArrayList<>(); 
		for(ICompilationUnit compilationUnit : compilationUnits){
			uacMetricOutput.add(aSniffer.getUAC(compilationUnit));
		}
		for(int i = 0; i < uacMetricOutput.size(); i++){
			numUACArray[i] = (int) uacMetricOutput.get(i).getSingleMetricValue();
		}
		Arrays.sort(expectedUAC);
		Arrays.sort(numUACArray);
		assertArrayEquals(expectedUAC, numUACArray);
		//Metricas coletadas
		
	}	
	//AED
	@Test
	public void testAED(){
		
		int expectedAED[] = {1,1,2,2,1,1,1,2,2,1,0,0,0,0,0,0,0,0,1,1,0,0};
		int[] numAEDArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		List<Integer> numAED = new ArrayList<>();
		
		List<MetricRepresentation> aedMetricOutput = new ArrayList<>();
		for(ICompilationUnit compilationUnit : compilationUnits){
			aedMetricOutput.add(aSniffer.getAED(compilationUnit));
		}
		
		for(MetricRepresentation classRep : aedMetricOutput)
			for(Integer metricValue : classRep.getMultiMetricValue())
				numAED.add(metricValue);
		numAEDArray = numAED.stream().mapToInt(i->i).toArray();
		Arrays.sort(expectedAED);
		Arrays.sort(numAEDArray);
		assertArrayEquals(expectedAED, numAEDArray);
	}
	
	//ASC
	@Test
	public void testASC(){
		
		int expectedASC[] = {0,0,0,0,0,0,0};
		int[] numASCArray = {0,0,0,0,0,0,0};
		List<MetricRepresentation> ascMetricOutput = new ArrayList<>(); 
		for(ICompilationUnit compilationUnit : compilationUnits){
			ascMetricOutput.add(aSniffer.getASC(compilationUnit));
		}
		for(int i = 0; i < ascMetricOutput.size(); i++){
			numASCArray[i] = (int) ascMetricOutput.get(i).getSingleMetricValue();
		}
		Arrays.sort(expectedASC);
		Arrays.sort(numASCArray);
		assertArrayEquals(expectedASC, numASCArray);
	}
	
	@Test
	public void testNumberClasses(){
		
		int numClasses = aSniffer.getNumClasses("TestPlugin");
		
		assertEquals(7, numClasses);
	}
	
	//NAC
	@Test
	public void testAnnotatedClass(){
		
		int numAnnotatedClass = aSniffer.getAnnotatedClass("TestPlugin");
		
		assertEquals(3, numAnnotatedClass);
	}
	//AA
	@Test
	public void testAA(){
		
		int[] expectedAA = {0,0,0,0,0,0,1,1,0,0,0,3,3,0,1,1};
		int[] numAAArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		List<Integer> numAA = new ArrayList<>();
		List<MetricRepresentation> aaMetricOutput = new ArrayList<>();
		for(ICompilationUnit compilationUnit : compilationUnits){
			aaMetricOutput.add(aSniffer.getAA(compilationUnit));
		}
		for(MetricRepresentation classRep : aaMetricOutput)
			for(Integer metricValue : classRep.getMultiMetricValue())
				numAA.add(metricValue);
		numAAArray = numAA.stream().mapToInt(i->i).toArray();
		Arrays.sort(expectedAA);
		Arrays.sort(numAAArray);
		assertArrayEquals(expectedAA, numAAArray);
	}
	//LOCAD
	@Test
	public void testLOCAD(){
		
		int expectedLOCAD[] = {1,1,1,1,1,1,1,1,1,1,1,1,3,3,1,1};
		int[] numLOCADArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		List<Integer> numLOCAD = new ArrayList<>();
		
		List<MetricRepresentation> locadMetricOutput = new ArrayList<>();
		for(ICompilationUnit compilationUnit : compilationUnits){
			locadMetricOutput.add(aSniffer.getLOCAD(compilationUnit));
		}
		
		for(MetricRepresentation classRep : locadMetricOutput)
			for(Integer metricValue : classRep.getMultiMetricValue())
				numLOCAD.add(metricValue);
		numLOCADArray = numLOCAD.stream().mapToInt(i->i).toArray();
		Arrays.sort(expectedLOCAD);
		Arrays.sort(numLOCADArray);
		assertArrayEquals(expectedLOCAD, numLOCADArray);
	}
	
	//Test to get number of elements in a class(class declaration, attributes, methods and etc..)
	@Test
	public void testGetNumberElements(){
		List<Integer> numberElements = new ArrayList<Integer>();
		numberElements = aSniffer.getNumberElements("TestPlugin");
		assertEquals(7, numberElements.size());
	}
	
	//Test to get the number of annotated elements
	//NAC
	@Test
	public void testGetNumberAnnotatedElements(){
		List<Integer> numberAnnotatedElements = new ArrayList<Integer>();
		numberAnnotatedElements = aSniffer.getNumberAnnotatedElements("TestPlugin");
		assertEquals(7, numberAnnotatedElements.size());
	}
	
	//Test to get the number of average number of attributes per annotations in a class
	//AAAC
	@Test
	public void testGetAverageNumberAttributesAnnotaton(){
		
		double expectedAAAC[] = {0,0,0,0,1.2,0.25,0.66};
		double[] numberAAA = aSniffer.getAvgNumberAtt("TestPlugin").stream().mapToDouble(i->i).toArray();
		Arrays.sort(expectedAAAC);
		Arrays.sort(numberAAA);
		assertArrayEquals(expectedAAAC, numberAAA, 0.01);
	}
	
	
	//ANL
	@Test
	public void testANL(){
		
		int[] expectedANL = {0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0};
		int[] numANLArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		List<Integer> numANL = new ArrayList<>();
		List<MetricRepresentation> anlMetricOutput = new ArrayList<>();
		for(ICompilationUnit compilationUnit : compilationUnits){
			anlMetricOutput.add(aSniffer.getANL(compilationUnit));
		}
		for(MetricRepresentation classRep : anlMetricOutput)
			for(Integer metricValue : classRep.getMultiMetricValue())
				numANL.add(metricValue);
		numANLArray = numANL.stream().mapToInt(i->i).toArray();
		Arrays.sort(expectedANL);
		Arrays.sort(numANLArray);
		assertArrayEquals(expectedANL, numANLArray);
	}
	
	
	
	@AfterClass
	public static void endEclipse(){
		
		bot.menu("File").menu("Exit").click();
		
	}

}
