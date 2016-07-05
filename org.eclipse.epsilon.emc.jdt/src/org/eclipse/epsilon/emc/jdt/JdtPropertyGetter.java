package org.eclipse.epsilon.emc.jdt;

import org.eclipse.epsilon.emc.jdt.properties.CompilationUnitProperties;
import org.eclipse.epsilon.emc.jdt.properties.EnumDecProperties;
import org.eclipse.epsilon.emc.jdt.properties.JavaProjectProperties;
import org.eclipse.epsilon.emc.jdt.properties.PackageProperties;
import org.eclipse.epsilon.emc.jdt.properties.SingleVariableDecProperties;
import org.eclipse.epsilon.emc.jdt.properties.Properties;
import org.eclipse.epsilon.emc.jdt.properties.TypeDecProperties;
import org.eclipse.epsilon.emc.jdt.properties.VariableDecFragProperties;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.introspection.java.JavaPropertyGetter;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * This class retrieves the value of properties of JDT model element
 * 
 * @author Cheng Yun
 * 
 */
public class JdtPropertyGetter extends JavaPropertyGetter {
	@Override
	public Object invoke(Object object, String property)
			throws EolRuntimeException {
		try {
			if (object instanceof IJavaProject) {// JavaProjact
				Object o = JavaProjectProperties.getProperty(property).run(
						object);
				operateCounter(o);
				return o;
			}

			if (object instanceof IPackageFragment) {// Package
				Object o = PackageProperties.getProperty(property).run(object);
				operateCounter(o);
				return o;
			}

			if (object instanceof ASTNode) {// ASTNode
				ASTNode node = (ASTNode) object;
				if (property.equals("type")) {// ASTNode.type
					operateCounter(object);
					return ASTNode.nodeClassForType(node.getNodeType());
				}
			}

			if (object instanceof CompilationUnit) {// JavaFile
				Object o = CompilationUnitProperties.getProperty(property).run(
						(ASTNode) object);
				operateCounter(o);
				return o;
			}

			if (object instanceof TypeDeclaration) {// Class
				Object o = TypeDecProperties.getProperty(property).run(
						(ASTNode) object);
				operateCounter(o);
				return o;
			}

			if (object instanceof EnumDeclaration) {// Enumeration
				Object o = EnumDecProperties.getProperty(property).run(
						(ASTNode) object);
				operateCounter(o);
				return o;
			}

			if (object instanceof FieldDeclaration) {// Field
				Object o = Properties.getProperty(property).run(
						(ASTNode) object);
				operateCounter(o);
				return o;
			}

			if (object instanceof MethodDeclaration) {// Method
				Object o = Properties.getProperty(property).run(
						(ASTNode) object);
				operateCounter(o);
				return o;
			}

			if (object instanceof VariableDeclaration) {// VariableDeclaration
				if (object instanceof SingleVariableDeclaration) {
					Object o = SingleVariableDecProperties
							.getProperty(property).run((ASTNode) object);
					operateCounter(o);
					return o;
				}

				if (object instanceof VariableDeclarationFragment) {
					Object o = VariableDecFragProperties.getProperty(property)
							.run((ASTNode) object);
					operateCounter(o);
					return o;
				}
			}
			// Statement or Expression
			if ((object instanceof Statement) || (object instanceof Expression)
					|| (object instanceof CatchClause)) {
				ASTNode node = (ASTNode) object;
				Object o = Properties.getProperty(property).run(node);
				operateCounter(o);
				return o;
			}
		} catch (NullPointerException e) {
			Object o = super.invoke(object, property);
			operateCounter(o);
			return o;
		}
		Object o = super.invoke(object, property);
		operateCounter(o);
		return o;
	}

	// TODO for automatic garbage collection
	private void operateCounter(Object object) {
		if (ASTModelMonitor.counter.containsKey(object)) {
			ASTModelMonitor.counter.put(object,
					ASTModelMonitor.counter.get(object) + 1);
		} else {
			ASTModelMonitor.counter.put(object, 1);
		}
	}
}
