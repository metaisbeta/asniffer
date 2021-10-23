package com.github.phillima.asniffer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.phillima.asniffer.model.CodeElementType;
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
	private CodeElementType type;
	private Map<BodyDeclaration,CodeElementModel> codeElementsInfo;
	private String packageName;

	public ClassInfo(CompilationUnit cu) {
		this.cu = cu;
		this.codeElementsInfo = new ConcurrentHashMap<BodyDeclaration, CodeElementModel>();
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		CodeElementType innerType = CodeElementType.CLASS;
		if(node.isInterface())
			innerType = CodeElementType.INTERFACE;
		if(node.isPackageMemberTypeDeclaration())
			getFullClassName(node.resolveBinding(),innerType);

		CodeElementModel codeElementModel = new CodeElementModel(node.getName().getIdentifier(), innerType, cu.getLineNumber(node.getStartPosition()));
		codeElementsInfo.put(node,codeElementModel);
		
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnumDeclaration node) {
		if(node.isPackageMemberTypeDeclaration()) {
			getFullClassName(node.resolveBinding(),CodeElementType.ENUM);
		}
		CodeElementModel codeElementModel = new CodeElementModel(node.getName().getIdentifier(), CodeElementType.ENUM, cu.getLineNumber(node.getStartPosition()));
		codeElementsInfo.put(node,codeElementModel);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		if(node.isPackageMemberTypeDeclaration())
			getFullClassName(node.resolveBinding(),CodeElementType.ANNOTATION_DECLARATION);
		CodeElementModel codeElementModel = new CodeElementModel(node.getName().getIdentifier(), CodeElementType.ANNOTATION_DECLARATION, cu.getLineNumber(node.getStartPosition()));
		codeElementsInfo.put(node,codeElementModel);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		CodeElementModel codeElementModel = new CodeElementModel(node.getName().toString(), CodeElementType.METHOD, cu.getLineNumber(node.getStartPosition()));
		codeElementsInfo.put(node,codeElementModel);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		Object o = node.fragments().get(0);
		if(o instanceof VariableDeclarationFragment){
			String fieldName = ((VariableDeclarationFragment) o).getName().toString();
			CodeElementModel codeElementModel = new CodeElementModel(fieldName, CodeElementType.FIELD, cu.getLineNumber(node.getStartPosition()));
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
	
	public CodeElementType getType() {
		return type;
	}

	public Map<BodyDeclaration, CodeElementModel> getCodeElementsInfo() {
		return codeElementsInfo;
	}
	
	//Inner methods
	private String getFullClassName(ITypeBinding binding, CodeElementType type) {
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