package org.eclipse.epsilon.emc.jdt;

import java.util.Collection;

import org.eclipse.jdt.core.JavaModelException;

/**
 * The implementation of this interface retrieves model elements and return them
 * to the caller.
 * 
 * @author Cheng Yun
 * 
 */
public interface ASTReflection {
	/**
	 * This method returns all elements of given type, including the sub-type of
	 * the given type.
	 * 
	 * @param type
	 *            the type of requested elements
	 * @return a collection of requested elements
	 * @throws JavaModelException
	 */
	public Collection<?> getAllOfKind(String type) throws JavaModelException;

	/**
	 * This method returns elements of which the type is exactly the given type.
	 * 
	 * @param type
	 *            the type of requested elements
	 * @return a collection of requested elements
	 * @throws JavaModelException
	 */
	public Collection<?> getAllOfType(String type) throws JavaModelException;

}
