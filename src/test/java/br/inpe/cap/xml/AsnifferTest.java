package br.inpe.cap.xml;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.inpe.cap.asniffer.ASniffer;
import br.inpe.cap.asniffer.AnnotationVisitor;
import br.inpe.cap.asniffer.ElementVisitor;
import br.inpe.cap.asniffer.utils.FileUtils;

public class AsnifferTest {

	static ASniffer aSniffer;
	static String anlString, locadString,aaString, aedString;
	
	@BeforeClass
	public static void initialize() {
		aSniffer = ASniffer.getInstance();
		String path = "/Users/phillima/Documents/eclipse-workspace/teste/AbstractBulkCompositeIdTest.java";
		handleAnnotation(Paths.get(path));
		initializeMultiMetrics();
	}
	
	@Test
	public void testAc() {
		int Ac = aSniffer.getAc();
		assertEquals(26, Ac);
	}
	
	@Test
	public void testUac() {
		int Uac = aSniffer.getUac();
		assertEquals(18, Uac);
	}
	
	@Test
	public void testAnl() {
		Map<Annotation, Integer> anl = aSniffer.getAnl();
		StringBuilder anlBuilder = new StringBuilder();
		anl.forEach((k,v)->{
			anlBuilder.append(k + "\n");
			anlBuilder.append(v + "\n");
		});
		assertEquals(anlString, anlBuilder.toString());
	}
	
	@Test
	public void testLocad() {
		Map<Annotation, Integer> locad = aSniffer.getLocad();
		StringBuilder locadBuilder = new StringBuilder();
		locad.forEach((k,v)->{
			locadBuilder.append(k + "\n");
			locadBuilder.append(v + "\n");
		});
		assertEquals(locadString, locadBuilder.toString());
	}
	
	@Test
	public void testAa() {
		Map<Annotation, Integer> aa = aSniffer.getAa();
		StringBuilder aaBuilder = new StringBuilder();
		aa.forEach((k,v)->{
			aaBuilder.append(k + "\n");
			aaBuilder.append(v + "\n");
		});
		
		assertEquals(aaString, aaBuilder.toString());
	}
	
	@Test
	public void testAed() {
		Map<BodyDeclaration, Integer> aed = aSniffer.getAed();
		StringBuilder aedBuilder = new StringBuilder();
		aed.forEach((k,v)->{
			aedBuilder.append(k + "\n");
			aedBuilder.append(v + "\n");
		});
		
		aed.forEach((k,v)->{
			System.out.println(k + "\n" + v);
		});
		assertEquals(aedString, aedBuilder.toString());
	}
	
	private static void handleAnnotation(Path sourceFilePath) {
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);
	    AnnotationVisitor annotationVisitor = new AnnotationVisitor();
	    ElementVisitor elementsVisitor = new ElementVisitor();
	    
	    String javaFile = FileUtils.readFileAsString(sourceFilePath);
		String packageName = "";
		String className = "";
	    
	    parser.setSource(javaFile.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
				
		//Creating the compilation unit
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
 		
		//Visiting this compilation unit
		cu.accept(annotationVisitor);
		cu.accept(elementsVisitor);
		
		if(cu.getPackage() != null)
			packageName = cu.getPackage().getName().toString();
		else
			packageName = "";
		
		//Obtain class name from the path
		int endIndex = sourceFilePath.toString().lastIndexOf("/");
		    if (endIndex != -1)  
		    		className = sourceFilePath.toString().substring(endIndex+1); // not forgot to put check if(endIndex != -1)
		
		aSniffer.setAllAnnotations(annotationVisitor.getAllAnnotations());
		aSniffer.setMarkerAnnotations(annotationVisitor.getMarkerAnnotations());
		aSniffer.setSingleAnnotations(annotationVisitor.getSingleAnnotations());
		aSniffer.setNormalAnnotations(annotationVisitor.getNormalAnnotations());
		aSniffer.setProgrammingElements(elementsVisitor.getElementsAnnotation());
		aSniffer.setPackageName(packageName);
		aSniffer.setClassName(className);
		aSniffer.execute(cu);
	}

	private static void initializeMultiMetrics() {

		anlString = FileUtils.readFileAsString("/Users/phillima/Documents/eclipse-workspace/teste/anlTest.txt");
		locadString = FileUtils.readFileAsString("/Users/phillima/Documents/eclipse-workspace/teste/locadTest.txt");
		aaString = FileUtils.readFileAsString("/Users/phillima/Documents/eclipse-workspace/teste/aaTest.txt");
		aedString = FileUtils.readFileAsString("/Users/phillima/Documents/eclipse-workspace/teste/aedTest.txt");
		 
	}
	
}
