package org.eclipse.epsilon.emc.jdt;

import java.util.ArrayList;
import java.util.Collection;
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

public class ReflectiveASTVisitor extends ASTVisitor implements ASTReflection {
	protected List<Object> all = null;
	protected String type = null;
	protected boolean ofTypeOnly = false;
	protected Collection<IJavaProject> projects = null;
	protected boolean ifBindings = false;

	public ReflectiveASTVisitor(Collection<IJavaProject> projects,
			boolean ifBindings) throws JavaModelException {
		this.projects = projects;
		this.ifBindings = ifBindings;
	}

	private Collection<?> getAll(String type) throws JavaModelException {
		all = new ArrayList<Object>();
		this.type = type;

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
		return all;
	}

	public Collection<?> getAllOfKind(String type) throws JavaModelException {
		this.ofTypeOnly = false;
		return getAll(type);
	}

	public Collection<?> getAllOfType(String type) throws JavaModelException {
		this.ofTypeOnly = true;
		return getAll(type);
	}

	@Override
	public void preVisit(ASTNode node) {
		if (node.getClass().getSimpleName().equals(type)) {
			all.add(node);
		} else if (!ofTypeOnly) {
			Class<?> superClass = node.getClass().getSuperclass();
			while (superClass != Object.class) {
				if (superClass.getSimpleName().equals(type)) {
					all.add(node);
					break;
				}
				superClass = superClass.getSuperclass();
			}
		}
	}

}
