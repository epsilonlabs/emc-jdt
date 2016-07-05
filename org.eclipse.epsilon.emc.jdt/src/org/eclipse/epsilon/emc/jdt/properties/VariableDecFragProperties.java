package org.eclipse.epsilon.emc.jdt.properties;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public enum VariableDecFragProperties {
	NAME("name", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return ((VariableDeclarationFragment)object).getName().getFullyQualifiedName();
		}
	});

    private String command;
    private ASTNodeProperty property;
    private static Map<String, VariableDecFragProperties> VariableFragProperties;
    
    //static block for property map initialisation, 
    //set it static to avoid repeated initialisation on each call
    static {
    	VariableFragProperties = new HashMap<String, VariableDecFragProperties>();
        for (VariableDecFragProperties p : VariableDecFragProperties.values()) {
        	VariableFragProperties.put(p.command, p);    
        }
    }

    VariableDecFragProperties(String value, ASTNodeProperty property) {
        this.command= value;
        this.property = property;
    }
    
    /**
     * returns the requested property, throws RuntimeException if command not found
     * @param command a specific value of the enumeration
     * @return a property
     * @throws RuntimeException
     */
    public static ASTNodeProperty getProperty(String command) {
        if(!VariableFragProperties.containsKey(command)) {
        	 return null;
        }
        return VariableFragProperties.get(command).property;
    }
}
