package org.eclipse.epsilon.emc.jdt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 * This is an AST model containing all AST nodes in the given Java source code.
 * 
 * @author Cheng Yun
 * 
 */
public class ASTModel extends ASTVisitor implements ASTReflection {
	protected List<Object> all = null;
	protected HashMap<String, ArrayList<ASTNode>> allOfKind;
	protected HashMap<String, ArrayList<ASTNode>> allOfType;

	public ASTModel(Collection<IJavaProject> projects, boolean ifBindings)
			throws JavaModelException {
		init(projects, ifBindings);
	}

	private void init(Collection<IJavaProject> projects, boolean ifBindings)
			throws JavaModelException {
		this.allOfKind = new HashMap<String, ArrayList<ASTNode>>();
		this.allOfType = new HashMap<String, ArrayList<ASTNode>>();

		for (IJavaProject project : projects) {
			for (IPackageFragment packageFragment : project
					.getPackageFragments()) {
				if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
					for (ICompilationUnit compilationUnit : packageFragment
							.getCompilationUnits()) {
						ASTParser parser = ASTParser.newParser(AST.JLS4);
						parser.setKind(ASTParser.K_COMPILATION_UNIT);
						parser.setSource(compilationUnit);
						// a switch for turning on/off resolve bindings
						parser.setResolveBindings(ifBindings);
						parser.createAST(null).accept(this);
					}
				}
			}
		}
	}

	public Collection<?> getAllOfKind(String type) throws JavaModelException {
		return allOfKind.get(type);
	}

	public Collection<?> getAllOfType(String type) throws JavaModelException {
		return allOfType.get(type);
	}

	@Override
	public void preVisit(ASTNode node) {
		String key = node.getClass().getSimpleName();
		initAllOfKind(key, node);
		initAllOfType(key, node);
		Class<?> superClass = node.getClass().getSuperclass();
		while (superClass != Object.class) {
			key = superClass.getSimpleName();
			initAllOfKind(key, node);
			superClass = superClass.getSuperclass();
		}
	}

	private void initAllOfKind(String key, ASTNode node) {
		if (allOfKind.containsKey(key)) {
			allOfKind.get(key).add(node);
		} else {
			allOfKind.put(key, new ArrayList<ASTNode>());
			allOfKind.get(key).add(node);
		}
	}

	private void initAllOfType(String key, ASTNode node) {
		if (allOfType.containsKey(key)) {
			allOfType.get(key).add(node);
		} else {
			allOfType.put(key, new ArrayList<ASTNode>());
			allOfType.get(key).add(node);
		}
	}

	public HashMap<String, ArrayList<ASTNode>> getAstModel() {
		return allOfKind;
	}
}
