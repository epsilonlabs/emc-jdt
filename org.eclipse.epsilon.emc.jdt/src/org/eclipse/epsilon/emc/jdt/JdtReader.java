package org.eclipse.epsilon.emc.jdt;

import java.beans.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * This class contains a set of static methods that are able to access Java
 * source code based Eclipse JDT. It supports accessing major Java model
 * elements and type-specific query of all AST nodes
 * 
 * @author Cheng Yun
 * 
 */
public class JdtReader {

	public static List<IProject> getIProjects() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// get all projects in the workspace
		IProject[] projects = root.getProjects();

		return Arrays.asList(projects);
	}

	public static List<IProject> getIProjects(String[] projectNames) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// get all projects in the workspace
		IProject[] projects = root.getProjects();
		List<IProject> projectsSelected = new ArrayList<IProject>();
		for (IProject project : projects) {
			for (String str : projectNames) {
				if (project.getName().equals(str)) {
					projectsSelected.add(project);
				}
			}
		}
		return projectsSelected;
	}

	public static IJavaProject getIJavaProject(IProject project)
			throws CoreException {
		IJavaProject javaProject;
		// the project should be a open Java project
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")
				&& project.isOpen()) {
			javaProject = JavaCore.create(project);
			return javaProject;
		}
		return null;
	}

	public static List<IJavaProject> getIJavaProjects(List<IProject> proejcts)
			throws CoreException {
		List<IJavaProject> sourceProjects = new ArrayList<IJavaProject>();
		for (IProject project : proejcts) {
			if (getIJavaProject(project) != null) {
				sourceProjects.add(getIJavaProject(project));
			}
		}
		return sourceProjects;
	}

	public static List<IPackageFragment> getIPackageFragments(
			IJavaProject javaProject, boolean sourceOnly) throws CoreException {
		
		if (javaProject != null) {
			List<IPackageFragment> packageFragments = new ArrayList<IPackageFragment>();
			if (sourceOnly) {
				for (IPackageFragment packageFragment : javaProject.getPackageFragments()) {
					if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
						packageFragments.add(packageFragment);
					}
				}
			} else {
				for (IPackageFragment packageFragment : javaProject.getPackageFragments()) {
					if (packageFragment != null) {
						packageFragments.add(packageFragment);
					}
				}
			}

			return packageFragments;
		}
		return Collections.emptyList();
	}

	public static List<IPackageFragment> getIPackageFragments(List<IJavaProject> javaProjects, boolean sourceOnly) throws CoreException {
		List<IPackageFragment> packageFragments = new ArrayList<IPackageFragment>();
		for (IJavaProject javaProject : javaProjects) {
			if (javaProject != null) {
				packageFragments.addAll(getIPackageFragments(javaProject, true));
			}
		}
		return packageFragments;
	}

	/**
	 * returns all ICompilationUnits in a package
	 * 
	 * @param packageFragment
	 * @return
	 * @throws JavaModelException
	 */
	public static List<ICompilationUnit> getICompilationUnits(
			IPackageFragment packageFragment) throws JavaModelException {
		ICompilationUnit[] iUnit = null;
		// determine if it is a java source code package
		if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
			iUnit = packageFragment.getCompilationUnits();
		}
		return Arrays.asList(iUnit);
	}

	/**
	 * creates a AST tree based on the ICompilationUnit passed in
	 * 
	 * @param unit
	 * @return
	 */
	public static CompilationUnit parse(ICompilationUnit iUnit) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(iUnit);
		parser.setResolveBindings(true);
		Visitor visitor = new Visitor();
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);
		unit.accept(visitor);
		return unit; // parse
	}

	/**
	 * return all CompilationUnits AST nodes in the given package
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static List<CompilationUnit> getCompilationUnits(
			IPackageFragment packageFragment) throws CoreException {
		if (packageFragment != null) {
			List<ICompilationUnit> iunit = getICompilationUnits(packageFragment);
			CompilationUnit[] units = new CompilationUnit[iunit.size()];
			// traverse all ICompilationUnits
			for (int i = 0; i < iunit.size(); i++) {
				units[i] = parse(iunit.get(i));
			}
			return Arrays.asList(units);
		}
		return null;
	}

	/**
	 * returns a list of class declaration AST nodes in the given ASTNode
	 * 
	 * @param unit
	 *            CompilationUnit
	 * @return an array of TypeDeclaration
	 */
	public static List<TypeDeclaration> getTypeDeclarations(ASTNode node) {
		if (node != null) {
			Visitor visitor = getVisitor(node);
			return visitor.getClasses();
		}
		return null;
	}

	/**
	 * returns an array that contains all the TypeDeclarationStatement AST nodes
	 * in the CompilationUnit ASTNode
	 * 
	 * @param unit
	 *            CompilationUnit
	 * @return a TypeDeclarationStatement node
	 */
	public static List<TypeDeclarationStatement> getTypeDeclarationStatements(
			CompilationUnit unit) {
		Visitor visitor = getVisitor(unit);
		return visitor.getTypeDeclarationStatements();
	}

	/**
	 * returns a list of class declaration AST nodes in the given compilation
	 * unit
	 * 
	 * @param unit
	 *            CompilationUnit
	 * @return an array of TypeDeclaration
	 */
	public static List<TypeDeclaration> getTypeDeclarations(CompilationUnit unit) {
		if (unit != null) {
			Visitor visitor = getVisitor(unit);
			return visitor.getClasses();
		}
		return null;
	}

	/**
	 * returns a list of EnumDeclaration AST nodes in the given TypeDeclaration
	 * ASTNode
	 * 
	 * @param typeDeclaration
	 *            TypeDeclaration
	 * @return an array of EnumDeclaration
	 */
	public static List<EnumDeclaration> getEnumDeclarations(ASTNode node) {
		if (node != null) {
			Visitor visitor = getVisitor(node);
			return visitor.getEnumDeclarations();
		}
		return null;
	}

	/**
	 * returns a list of AnnotationTypeDeclaration AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return a list of AnnotationTypeDeclaration
	 */
	public static List<AnnotationTypeDeclaration> getAnnotations(ASTNode node) {
		if (node != null) {
			Visitor visitor = getVisitor(node);
			return visitor.getAnnotations();
		}
		return null;
	}

	/**
	 * returns a list of Initializer AST nodes in the given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of Initializer
	 */
	public static List<Initializer> getInitializers(ASTNode node) {
		if (node != null) {
			Visitor visitor = getVisitor(node);
			return visitor.getInitializers();
		}
		return null;
	}

	/**
	 * returns a list of method MethodDeclaration nodes in the given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return a list of method declaration AST nodes
	 */
	public static List<MethodDeclaration> getMethodDeclarations(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getMethods();
	}

	/**
	 * returns an array that contains all the variable declaration statement AST
	 * nodes in the given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of VariableDeclarationStatement nodes
	 */
	public static List<VariableDeclarationStatement> getVariableDeclarationStatements(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getVariables();
	}

	/**
	 * returns an array that contains all ArrayType AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ArrayType nodes
	 */
	public static List<ArrayType> getArrayTypes(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getArrayTypes();
	}

	/**
	 * returns an array that contains all ParameterizedType AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ParameterizedType nodes
	 */
	public static List<ParameterizedType> getParameterizedTypes(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getParameterizedTypes();
	}

	/**
	 * returns an array that contains all PrimitiveType AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of PrimitiveType nodes
	 */
	public static List<PrimitiveType> getPrimitiveTypes(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getPrimitiveTypes();
	}

	/**
	 * returns an array that contains all QualifiedType AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of QualifiedType nodes
	 */
	public static List<QualifiedType> getQualifiedTypes(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getQualifiedTypes();

	}

	/**
	 * returns an array that contains all SimpleType AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of SimpleType nodes
	 */
	public static List<SimpleType> getSimpleTypes(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getSimpleTypes();
	}

	/**
	 * returns an array that contains all UnionType AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of UnionType nodes
	 */
	public static List<UnionType> getUnionTypes(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getUnionTypes();
	}

	/**
	 * returns an array that contains all WildcardType AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of WildcardType nodes
	 */
	public static List<WildcardType> getWildcardTypes(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getWildcardTypes();
	}

	/**
	 * returns an array that contains all the IfStatement AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of IfStatement nodes
	 */
	public static List<IfStatement> getIfStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getIfStatements();
	}

	/**
	 * returns an array that contains all the WhileStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of WhileStatement nodes
	 */
	public static List<WhileStatement> getWhileStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getWhileStatements();
	}

	/**
	 * returns an array that contains all the DoStatement AST nodes in the given
	 * ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of DoStatement nodes
	 */
	public static List<DoStatement> getDoStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getDoStatements();
	}

	/**
	 * returns an array that contains all the SwitchStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of SwitchStatement nodes
	 */
	public static List<SwitchStatement> getSwitchStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getSwitchStatements();
	}

	/**
	 * returns an array that contains all the ForStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ForStatement nodes
	 */
	public static List<ForStatement> getForStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getForStatements();
	}

	/**
	 * returns an array that contains all the EnhancedForStatement AST nodes in
	 * the given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of EnhancedForStatement nodes
	 */
	public static List<EnhancedForStatement> getEnhancedForStatements(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getEnhancedForStatements();
	}

	/**
	 * returns an array that contains all the TryStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of TryStatement nodes
	 */
	public static List<TryStatement> getTryStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getTryStatements();
	}

	/**
	 * returns an array that contains all the SynchronizedStatement AST nodes in
	 * the given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of SynchronizedStatement nodes
	 */
	public static List<SynchronizedStatement> getSynchronizedStatements(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getSynchronizedStatements();
	}

	/**
	 * returns an array that contains all the AssertStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of AssertStatement nodes
	 */
	public static List<AssertStatement> getAssertStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getAssertStatements();
	}

	/**
	 * returns an array that contains all the ThrowStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ThrowStatement nodes
	 */
	public static List<ThrowStatement> getThrowStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getThrowStatements();
	}

	/**
	 * returns an array that contains all the SwitchCase AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of SwitchCase nodes
	 */
	public static List<SwitchCase> getSwitchCases(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getSwitchCases();
	}

	/**
	 * returns an array that contains all the Block AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of Block nodes
	 */
	public static List<Block> getBlocks(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getBlocks();
	}
	
	/**
	 * returns an array that contains all the statement AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of Statement nodes
	 */
	@SuppressWarnings("unchecked")
	public static List<Statement> getStatements(ASTNode node) {
		List<Block> blocks = getBlocks(node);
		List<Statement> statements = new ArrayList<Statement>();
		if(blocks.size()>0){
			for(Block block : blocks){
				statements.addAll(block.statements());
			}
		}
		return statements;
	}

	/**
	 * returns an array that contains all the BreakStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of BreakStatement nodes
	 */
	public static List<BreakStatement> getBreakStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getBreakStatements();
	}

	/**
	 * returns an array that contains all the ContinueStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ContinueStatement nodes
	 */
	public static List<ContinueStatement> getContinueStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getContinueStatements();
	}

	/**
	 * returns an array that contains all the ConstructorInvocation AST nodes in
	 * the given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ConstructorInvocation nodes
	 */
	public static List<ConstructorInvocation> getConstructorInvocations(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getConstructorInvocations();
	}

	/**
	 * returns an array that contains all the EmptyStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of EmptyStatement nodes
	 */
	public static List<EmptyStatement> getEmptyStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getEmptyStatements();
	}

	/**
	 * returns an array that contains all the SuperConstructorInvocation AST
	 * nodes in the given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of SuperConstructorInvocation nodes
	 */
	public static List<SuperConstructorInvocation> getSuperConstructorInvocations(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getSuperConstructorInvocations();
	}

	/**
	 * returns an array that contains all the LabeledStatement AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of LabeledStatement nodes
	 */
	public static List<LabeledStatement> getLabeledStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getLabeledStatements();
	}

	/**
	 * returns an array that contains all the MethodInvocation AST nodes in the
	 * given node node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of MethodInvocation nodes
	 */
	public static List<MethodInvocation> getMethodInvocations(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getMethodInvocations();
	}

	/**
	 * returns an array that contains all the ExpressionStatement AST nodes in
	 * the given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ExpressionStatement nodes
	 */
	public static List<ExpressionStatement> getExpressionStatements(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getExpressionStatements();
	}

	// getters of Expressions
	/**
	 * returns an array that contains all the StringLiteral AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of StringLiteral nodes
	 */
	public static List<StringLiteral> getStringLiterals(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getStringLiterals();
	}

	/**
	 * returns an array that contains all the TypeLiteral AST nodes in the given
	 * ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of TypeLiteral nodes
	 */
	public static List<TypeLiteral> getTypeLiterals(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getTypeLiterals();
	}

	/**
	 * returns an array that contains all the NullLiteral AST nodes in the given
	 * ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of NullLiteral nodes
	 */
	public static List<NullLiteral> getNullLiterals(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getNullLiterals();
	}

	/**
	 * returns an array that contains all the InfixExpression AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of InfixExpression nodes
	 */
	public static List<InfixExpression> getInfixExpressions(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getInfixExpressions();
	}

	/**
	 * returns an array that contains all the ArrayAccess AST nodes in the given
	 * ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ArrayAccess nodes
	 */
	public static List<ArrayAccess> getArrayAccesses(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getArrayAccesses();
	}

	/**
	 * returns an array that contains all the ArrayCreation AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ArrayCreation nodes
	 */
	public static List<ArrayCreation> getArrayCreations(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getArrayCreations();
	}

	/**
	 * returns an array that contains all the ArrayInitializer AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ArrayInitializer nodes
	 */
	public static List<ArrayInitializer> getArrayInitializers(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getArrayInitializers();
	}

	/**
	 * returns an array that contains all the Assignment AST nodes in the given
	 * ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of Assignment nodes
	 */
	public static List<Assignment> getAssignments(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getAssignments();
	}

	/**
	 * returns an array that contains all the BooleanLiteral AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of BooleanLiteral nodes
	 */
	public static List<BooleanLiteral> getBooleanLiterals(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getBooleanLiterals();
	}

	/**
	 * returns an array that contains all the CastExpression AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of CastExpression nodes
	 */
	public static List<CastExpression> getCastExpressions(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getCastExpressions();
	}

	/**
	 * returns an array that contains all the CharacterLiteral AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of CharacterLiteral nodes
	 */
	public static List<CharacterLiteral> getCharacterLiterals(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getCharacterLiterals();
	}

	/**
	 * returns an array that contains all the ClassInstanceCreation AST nodes in
	 * the given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ClassInstanceCreation nodes
	 */
	public static List<ClassInstanceCreation> getClassInstanceCreations(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getClassInstanceCreations();
	}

	/**
	 * returns an array that contains all the ConditionalExpression AST nodes in
	 * the given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ConditionalExpression nodes
	 */
	public static List<ConditionalExpression> getConditionalExpressions(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getConditionalExpressions();
	}

	/**
	 * returns an array that contains all the FieldAccess AST nodes in the given
	 * ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of FieldAccess nodes
	 */
	public static List<FieldAccess> getFieldAccesses(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getFieldAccesses();
	}

	/**
	 * returns an array that contains all the InstanceofExpression AST nodes in
	 * the given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of InstanceofExpression nodes
	 */
	public static List<InstanceofExpression> getInstanceofExpressions(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getInstanceofExpressions();
	}

	/**
	 * returns an array that contains all the MarkerAnnotation AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of MarkerAnnotation nodes
	 */
	public static List<MarkerAnnotation> getMarkerAnnotations(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getMarkerAnnotations();
	}

	/**
	 * returns an array that contains all the NormalAnnotation AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of NormalAnnotation nodes
	 */
	public static List<NormalAnnotation> getNormalAnnotations(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getNormalAnnotations();
	}

	/**
	 * returns an array that contains all the NumberLiteral AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of NumberLiteral nodes
	 */
	public static List<NumberLiteral> getNumberLiterals(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getNumberLiterals();
	}

	/**
	 * returns an array that contains all the ParenthesizedExpression AST nodes
	 * in the given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ParenthesizedExpression nodes
	 */
	public static List<ParenthesizedExpression> getParenthesizedExpressions(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getParenthesizedExpressions();
	}

	/**
	 * returns an array that contains all the PostfixExpression AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of PostfixExpression nodes
	 */
	public static List<PostfixExpression> getPostfixExpressions(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getPostfixExpressions();
	}

	/**
	 * returns an array that contains all the PrefixExpression AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of PrefixExpression nodes
	 */
	public static List<PrefixExpression> getPrefixExpressions(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getPrefixExpressions();
	}

	/**
	 * returns an array that contains all the SingleMemberAnnotation AST nodes
	 * in the given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of SingleMemberAnnotation nodes
	 */
	public static List<SingleMemberAnnotation> getSingleMemberAnnotations(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getSingleMemberAnnotations();
	}

	/**
	 * returns an array that contains all the SuperFieldAccess AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of SuperFieldAccess nodes
	 */
	public static List<SuperFieldAccess> getSuperFieldAccesses(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getSuperFieldAccesses();
	}

	/**
	 * returns an array that contains all the QualifiedName AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of QualifiedName nodes
	 */
	public static List<QualifiedName> getQualifiedNames(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getQualifiedNames();
	}

	/**
	 * returns an array that contains all the SimpleName AST nodes in the given
	 * ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of SimpleName nodes
	 */
	public static List<SimpleName> getSimpleNames(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getSimpleNames();
	}

	/**
	 * returns an array that contains all the SuperMethodInvocation AST nodes in
	 * the given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of SuperMethodInvocation nodes
	 */
	public static List<SuperMethodInvocation> getSuperMethodInvocations(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getSuperMethodInvocations();
	}

	/**
	 * returns an array that contains all the ThisExpression AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ThisExpression nodes
	 */
	public static List<ThisExpression> getThisExpressions(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getThisExpressions();
	}

	/**
	 * returns an array that contains all the VariableDeclarationExpression AST
	 * nodes in the given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of VariableDeclarationExpression nodes
	 */
	public static List<VariableDeclarationExpression> getVariableDeclarationExpressions(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getVariableDeclarationExpressions();
	}

	// end of expression getters

	/**
	 * returns an array that contains all the ReturnStatement AST nodes in the
	 * given ASTNode node
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of ReturnStatement nodes
	 */
	public static List<ReturnStatement> getReturnStatements(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getReturnStatements();
	}
	
	/**
	 * returns an array that contains all the CatchClause AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of CatchClause nodes
	 */
	public static List<CatchClause> getCatchClauses(
			ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getCatchClauses();
	}

	/**
	 * returns an array that contains all the LineComment AST nodes in the given
	 * ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of LineComment nodes
	 * @see org.eclipse.jdt.core.dom.LineComment
	 */
	public static List<LineComment> getLineComments(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getLineComments();
	}

	/**
	 * returns an array that contains all the BlockComment AST nodes in the
	 * given ASTNode
	 * 
	 * @param node
	 *            ASTNode
	 * @return an array of BlockComment nodes
	 */
	public static List<BlockComment> getBlockComments(ASTNode node) {
		Visitor visitor = getVisitor(node);
		return visitor.getBlockComments();
	}

	/**
	 * returns the visitor of the given AST node
	 * 
	 * @param node
	 *            ASTNode
	 * @return the visitor of the given AST node
	 */
	private static Visitor getVisitor(ASTNode node) {
		Visitor visitor = new Visitor();
		node.accept(visitor);
		return visitor;
	}
	
	/**
	 * returns the requested type of parent of the given ASTNode, returns null
	 * if no requested parent node is found. If there are more than one parent
	 * of the requested type (e.g., nested classes, nested loops), only the
	 * nearest one will be returned.
	 * 
	 * @param node ASTNode under question
	 * @param parentClass the class of the parent type
	 * @return an ASTNode
	 * @since 31/08/2013
	 */
	public static ASTNode getParentASTNode(ASTNode node, Class<?> parentClass) {
		ASTNode parent = node.getParent();
		while (parent.getClass() != parentClass
				&& parent.getClass() != CompilationUnit.class) {
			parent = parent.getParent();
		}
		if (parent.getClass() == parentClass) {// requested parent is found
			return parent;
		} else {// requested parent not found
			return null;
		}
	}
}
