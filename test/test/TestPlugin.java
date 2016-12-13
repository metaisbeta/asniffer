package test;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
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
import org.eclipse.jface.text.Document;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.handlers.AnnotationSniffer;

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
	         bot.menu("File").menu("New").menu("Java Project").click();
	 		 shell = bot.shell("New Java Project");
	 		 shell.activate();
	 		 //bot.tree().expandNode("General").select("Project");
	 		 //bot.button("Next >").click();
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
		List<Integer> numAC = new ArrayList<>(); 
		for(ICompilationUnit compilationUnit : compilationUnits){
			numAC.add(aSniffer.getAC(compilationUnit));
		}
		int[] numACArray = numAC.stream().mapToInt(i->i).toArray();
		
		Arrays.sort(expectedAC);
		Arrays.sort(numACArray);
		assertArrayEquals(expectedAC, numACArray);
	}
	//UAC
	@Test
	public void testUAC(){
		
		int expectedUAC[] = {0,0,0,3,2,3,0};
		List<Integer> numUAC = new ArrayList<>(); 
		for(ICompilationUnit compilationUnit : compilationUnits){
			numUAC.add(aSniffer.getUAC(compilationUnit));
		}
		int[] numUACArray = numUAC.stream().mapToInt(i->i).toArray();
		
		Arrays.sort(expectedUAC);
		Arrays.sort(numUACArray);
		assertArrayEquals(expectedUAC, numUACArray);
	}
	
	//AED
	@Test
	public void testAED(){
		
		int expectedAED[] = {1,1,2,2,1,1,1,2,2,1,0,0,0,0,0,0,0,0,1,1,0,0};
		List<Integer> numAED = new ArrayList<>(); 
		for(ICompilationUnit compilationUnit : compilationUnits){
			numAED.addAll(aSniffer.getAED(compilationUnit));
		}
		int[] numAEDArray = numAED.stream().mapToInt(i->i).toArray();
		
		Arrays.sort(expectedAED);
		Arrays.sort(numAEDArray);
		assertArrayEquals(expectedAED, numAEDArray);
	}
	
	//ASC
	@Test
	public void testASC(){
		
		int expectedASC[] = {0,0,0,0,0,0,0};
		List<Integer> numASC = new ArrayList<>(); 
		for(ICompilationUnit compilationUnit : compilationUnits){
			numASC.add(aSniffer.getASC(compilationUnit));
		}
		int[] numASCArray = numASC.stream().mapToInt(i->i).toArray();
		
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
		
		int expectedAA[] = {0,0,0,0,0,0,1,1,0,0,0,3,3,0,1,1};
		List<Integer> numAA = new ArrayList<>(); 
		for(ICompilationUnit compilationUnit : compilationUnits){
			numAA.addAll(aSniffer.getAA(compilationUnit));
		}
		int[] numAAArray = numAA.stream().mapToInt(i->i).toArray();
		
		Arrays.sort(expectedAA);
		Arrays.sort(numAAArray);
		assertArrayEquals(expectedAA, numAAArray);
	}
	//LOCAD
	@Test
	public void testLOCAD(){
		
		int expectedLOCAD[] = {1,1,1,1,1,1,1,1,1,1,1,1,3,3,1,1};
		
		List<Integer> numLOCAD = new ArrayList<>();
		for(ICompilationUnit compilationUnit : compilationUnits){
			numLOCAD.addAll(aSniffer.getLOCAD(compilationUnit));
		}
		int[] numLOCADArray = numLOCAD.stream().mapToInt(i->i).toArray();
		
		Arrays.sort(expectedLOCAD);
		Arrays.sort(numLOCADArray);
		assertArrayEquals(expectedLOCAD, numLOCADArray);
	}
	
	//ANL
	//@Test
	public void testAninhamentoAnotacao(){
			
		int nivelAninhamentoAnotacao = aSniffer.getNivelAninhamentoAnotacao("TestPlugin", "Classe2.java", "Annotation3", "Annotation1");
			
		assertEquals(2, nivelAninhamentoAnotacao);
	}
	
	//Test to get number of elements in a class(class declaration, attributes, methods and etc..)
	@Test
	public void testGetNumberElements(){
		List<Integer> numberElements = new ArrayList<Integer>();
		numberElements = aSniffer.getNumberElements("TestPlugin");
		assertEquals(7, numberElements.size());
		/*assertEquals(3, numberElements.get(0).intValue());
		assertEquals(4, numberElements.get(1).intValue());
		assertEquals(6, numberElements.get(2).intValue());
		assertEquals(1, numberElements.get(3).intValue());
		assertEquals(3, numberElements.get(4).intValue());
		assertEquals(2, numberElements.get(5).intValue());*/
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
	
	
	//AA
	@Test
	public void testANL(){
		
		int expectedANL[] = {0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0};
		List<Integer> numANL = new ArrayList<>(); 
		for(ICompilationUnit compilationUnit : compilationUnits){
			numANL.addAll(aSniffer.getANL(compilationUnit));
		}
		int[] numANLArray = numANL.stream().mapToInt(i->i).toArray();
		
		Arrays.sort(expectedANL);
		Arrays.sort(numANLArray);
		assertArrayEquals(expectedANL, numANLArray);
	}
	
	
	@AfterClass
	public static void finaliza(){
		
		bot.menu("File").menu("Exit").click();
		
	}

}
