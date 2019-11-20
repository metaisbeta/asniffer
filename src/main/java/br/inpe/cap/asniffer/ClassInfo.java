package br.inpe.cap.asniffer;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import br.inpe.cap.asniffer.model.CodeElementModel;

public class ClassInfo extends ASTVisitor{

	private CompilationUnit cu;
	private String className;
	private String type;
	private Map<BodyDeclaration,CodeElementModel> codeElementsInfo = new HashMap<BodyDeclaration, CodeElementModel>();
	private String packageName;

	public ClassInfo(CompilationUnit cu) {
		this.cu = cu;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		getFullClassName(node.resolveBinding());
		
		if(node.isInterface()) type = "interface";
		else type = "class";
		
		CodeElementModel codeElementModel = new CodeElementModel(className, type, cu.getLineNumber(node.getStartPosition()));
		codeElementsInfo.put(node,codeElementModel);
		
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnumDeclaration node) {
		type = "enum";
		getFullClassName(node.resolveBinding());
		CodeElementModel codeElementModel = new CodeElementModel(className, type, cu.getLineNumber(node.getStartPosition()));
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
	private void getFullClassName(ITypeBinding binding) {
		if(binding!=null) {
			this.className = binding.getBinaryName();
			this.packageName = binding.getPackage().getName();
		}
	}
}