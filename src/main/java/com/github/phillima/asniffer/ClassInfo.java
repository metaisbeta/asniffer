package com.github.phillima.asniffer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.github.phillima.asniffer.model.CodeElementModel;

public class ClassInfo extends ASTVisitor{

	private CompilationUnit cu;
	private String className = null;
	private String type;
	private Map<BodyDeclaration,CodeElementModel> codeElementsInfo;
	private String packageName;

	public ClassInfo(CompilationUnit cu) {
		this.cu = cu;
		this.codeElementsInfo = new ConcurrentHashMap<BodyDeclaration, CodeElementModel>();
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		String innerType = null;
		if(node.isInterface())
			innerType = "interface";
		else 
			innerType = "class";
		if(node.isPackageMemberTypeDeclaration()) {
			getFullClassName(node.resolveBinding(),innerType);
		}
		CodeElementModel codeElementModel = new CodeElementModel(node.getName().getIdentifier(), innerType, cu.getLineNumber(node.getStartPosition()));
		codeElementsInfo.put(node,codeElementModel);
		
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnumDeclaration node) {
		if(node.isPackageMemberTypeDeclaration()) {
			getFullClassName(node.resolveBinding(),"enum");
			type = "enum";
		}
		CodeElementModel codeElementModel = new CodeElementModel(node.getName().getIdentifier(), "enum", cu.getLineNumber(node.getStartPosition()));
		codeElementsInfo.put(node,codeElementModel);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		if(node.isPackageMemberTypeDeclaration()){
			type = "annotation-declaration";
			getFullClassName(node.resolveBinding(),type);

		}
		CodeElementModel codeElementModel = new CodeElementModel(node.getName().getIdentifier(), "annotation-declaration", cu.getLineNumber(node.getStartPosition()));
		codeElementsInfo.put(node,codeElementModel);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		CodeElementModel codeElementModel = new CodeElementModel(node.getName().toString(), "method", cu.getLineNumber(node.getStartPosition()));
		codeElementsInfo.put(node,codeElementModel);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		Object o = node.fragments().get(0);
		if(o instanceof VariableDeclarationFragment){
			String fieldName = ((VariableDeclarationFragment) o).getName().toString();
			CodeElementModel codeElementModel = new CodeElementModel(fieldName, "field", cu.getLineNumber(node.getStartPosition()));
			codeElementsInfo.put(node,codeElementModel);
		}
		return super.visit(node);
	}
	
	public String getPackageName() {
		return this.packageName;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getType() {
		return type;
	}

	public Map<BodyDeclaration, CodeElementModel> getCodeElementsInfo() {
		return codeElementsInfo;
	}
	
	//Inner methods
	private String getFullClassName(ITypeBinding binding, String type) {
		this.type = type;
		if(binding!=null) {
			if(className==null) {
				this.className = binding.getBinaryName();
				this.packageName = binding.getPackage().getName();
			}else
				return binding.getBinaryName();
		}
		return null;
	}
}