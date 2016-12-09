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
	
	@BeforeClass
	public static void beforeClass(){
		
		bot = new SWTWorkbenchBot();
		aSniffer = new AnnotationSniffer();
		try {
			//Close the welcome screen
			bot.viewByTitle("Welcome").close();
			
		} catch (WidgetNotFoundException e) {
		}
		
		//Create a Java project to be tested
		createTestProject("TestPlugin");
		
	}
	
	private static void createTestProject(String nameProject) {

		 IWorkspace workspace = ResourcesPlugin.getWorkspace();
         IWorkspaceRoot root = workspace.getRoot();
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
		compilationUnitNames = fillCompilationMap(namePackages.length);
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

	private static Map<String, String> fillCompilationMap(int numberOfPackages) {
		Map<String, String> compilationUnitNames = new HashMap<String, String>();
		
		for(int i = 0; i < 3; i++)
			compilationUnitNames.put("Classe"+(i+1),"Class");
		for(int i = 0; i < 3; i++)
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
					// TODO Auto-generated catch block
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
				buf.append("private int y;\n");
				buf.append("private int z;\n");
				break;
			case "Classe2":
				buf.append("@Annotation1\n");
				buf.append("@Annotation2(value1 = 2,\n");
				buf.append("value2 = 3)\n"); 
				buf.append("private int a;\n");
				buf.append("@Annotation1\n");
				buf.append("@Annotation2(value1 = 1,\n");
				buf.append("value2 = 2)\n"); 
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
				break;
			case "Annotation3":
				buf.append("Annotation1 value3();\n");
				break;
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
		assertEquals(6, numberOfLines.size());
		
	}
	//AC
	@Test
	public void testAC(){
		
		int expectedAC[] = {0,0,0,1,8,5};
		int[] numberAnnotations = aSniffer.getAC("TestPlugin").stream().mapToInt(i->i).toArray();
		Arrays.sort(expectedAC);
		Arrays.sort(numberAnnotations);
		assertArrayEquals(expectedAC, numberAnnotations);
	}
	//UAC
	@Test
	public void testUAC(){
		int expectedUAC[] = {0,0,0,1,3,2};
		int[] numberAnnotationsUnique = aSniffer.getUAC("TestPlugin").stream().mapToInt(i->i).toArray();
		Arrays.sort(expectedUAC);
		Arrays.sort(numberAnnotationsUnique);
		assertArrayEquals(expectedUAC, numberAnnotationsUnique);
	}
	
	//AED
	@Test
	public void testNumeroDeAnotacoesPorElemento(){
		
		int numAnotacao = aSniffer.getNumeroAnotacaoElemento("TestPlugin", "Classe2.java", "b");
		assertEquals(2, numAnotacao);
		
		numAnotacao = aSniffer.getNumeroAnotacaoElemento("TestPlugin", "Classe2.java", "a");
		assertEquals(1, numAnotacao);
		
	}
	
	@Test
	public void testNumeroClasses(){
		
		int numClasses = aSniffer.getNumClasses("TestPlugin");
		
		assertEquals(6, numClasses);
	}
	
	//NAC
	@Test
	public void testAnnotatedClass(){
		
		int numAnnotatedClass = aSniffer.getAnnotatedClass("TestPlugin");
		
		assertEquals(3, numAnnotatedClass);
	}
	
	//AA
	@Test
	public void testNumeroAtributosAnotacao(){
		
		int numAtributosAnotacao = aSniffer.getNumAtributosAnotacao("TestPlugin", "Classe2.java", "Annotation2");
		
		assertEquals(2, numAtributosAnotacao);
	}
	//LOCAD
	@Test
	public void testLOCAD(){
			
		//int numLOCAD = aSniffer.getLOCAD("TestPlugin", "Classe2.java", "Annotation2");
		assertEquals(2, 0);
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
		assertEquals(6, numberElements.size());
		/*assertEquals(3, numberElements.get(0).intValue());
		assertEquals(4, numberElements.get(1).intValue());
		assertEquals(6, numberElements.get(2).intValue());
		assertEquals(1, numberElements.get(3).intValue());
		assertEquals(3, numberElements.get(4).intValue());
		assertEquals(2, numberElements.get(5).intValue());*/
	}
	
	//Test to get the number of annotated elements
	@Test
	public void testGetNumberAnnotatedElements(){
		List<Integer> numberAnnotatedElements = new ArrayList<Integer>();
		numberAnnotatedElements = aSniffer.getNumberAnnotatedElements("TestPlugin");
		assertEquals(6, numberAnnotatedElements.size());
	}
	
	//Test to get the number of average number of attributes per annotations in a class
	//AAAC
	@Test
	public void testGetAverageNumberAttributesAnnotaton(){
		
		double expectedAAAC[] = {0,0,0,0,0.8,0.25};
		double[] numberAAA = aSniffer.getAvgNumberAtt("TestPlugin").stream().mapToDouble(i->i).toArray();
		Arrays.sort(expectedAAAC);
		Arrays.sort(numberAAA);
		assertArrayEquals(expectedAAAC, numberAAA, 0.01);
	}
	
	
	
	@AfterClass
	public static void finaliza(){
		
		bot.menu("File").menu("Exit").click();
		
	}

}
