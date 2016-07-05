package org.eclipse.epsilon.emc.jdt.properties;

import org.eclipse.jdt.core.dom.ASTNode;
/**
 * The implementation of this interface returns the requested property on the given ASTNode.
 * @author Cheng Yun
 *
 */
public interface ASTNodeProperty {
	public Object run(ASTNode node);
}
