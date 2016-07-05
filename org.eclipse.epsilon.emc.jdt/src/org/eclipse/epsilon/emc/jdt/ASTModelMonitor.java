package org.eclipse.epsilon.emc.jdt;

import java.util.Collection;
import java.util.HashMap;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

/**
 * AST model with automatic garbage collection.
 * 
 * @author Cheng Yun
 * 
 */
// TODO for automatic garbage collection
public class ASTModelMonitor extends ASTModel implements Runnable {
	public static HashMap<Object, Integer> counter = new HashMap<Object, Integer>();

	public ASTModelMonitor(Collection<IJavaProject> projects, boolean ifBindings)
			throws JavaModelException {
		super(projects, ifBindings);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
