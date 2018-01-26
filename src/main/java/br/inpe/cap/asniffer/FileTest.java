package br.inpe.cap.asniffer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import br.inpe.cap.asniffer.output.PackagePojo;
import br.inpe.cap.asniffer.output.ProjectPojo;
import br.inpe.cap.asniffer.utils.FileUtils;
import br.inpe.cap.asniffer.utils.XMLUtils;

public class FileTest {

	static ASniffer aSniffer = ASniffer.getInstance();
	
	public static void main(String[] args) {

	    List<Path> paths = new ArrayList<Path>();
		for (Path path : FileUtils.listProjects("/Users/phillima/Documents/teste")) {
			String projectName = "";
			//Obtain class name from the path
			int endIndex = path.toString().lastIndexOf("/");
			    if (endIndex != -1)  
			    		projectName = path.toString().substring(endIndex+1); // not forgot to put check if(endIndex != -1)
			    paths = FileUtils.findFiles(path);
			    search(paths);
			prepareXML(MetricMapping.getPackagePojo(), projectName);
		}
	}
	
	private static void search(List<Path> paths) {
		
		List<Path> dir = new ArrayList<>();
		for (Path path : paths) {
			 String path_ = path.toString();
			    if(path_.contains(".java")) {
					//Search for annotations
					handleAnnotation(path);
					//Map the data into POJOs
					MetricMapping.map(aSniffer);
				}else {
					if(Files.isDirectory(path)) {
						dir.add(path);
					}
				}
		}
		for (Path path : dir) {
			search(FileUtils.findFiles(path.toString()));
		}
		
	}
	
	public static void handleAnnotation(Path sourceFilePath) {
		
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
		
		//System.out.println("Package name:" + packageName);
		System.out.println("Class name:" + className);
		aSniffer.setAllAnnotations(annotationVisitor.getAllAnnotations());
		aSniffer.setMarkerAnnotations(annotationVisitor.getMarkerAnnotations());
		aSniffer.setSingleAnnotations(annotationVisitor.getSingleAnnotations());
		aSniffer.setNormalAnnotations(annotationVisitor.getNormalAnnotations());
		aSniffer.setProgrammingElements(elementsVisitor.getElementsAnnotation());
		aSniffer.setPackageName(packageName);
		aSniffer.setClassName(className);
		aSniffer.execute(cu);
	}

	private static void prepareXML(List<PackagePojo> packages_, String projectName) {
		XMLUtils.createXMLFile(new ProjectPojo(projectName, packages_));
		
	}
}
