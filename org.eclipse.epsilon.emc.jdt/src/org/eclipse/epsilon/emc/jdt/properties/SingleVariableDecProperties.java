package org.eclipse.epsilon.emc.jdt.properties;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public enum SingleVariableDecProperties {
	NAME("name", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return ((SingleVariableDeclaration)object).getName().toString();
		}
	}), MODIFIERS("modifiers", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return Util.getModifiers(((SingleVariableDeclaration)object).getModifiers());
		}
	});

    private String command;
    private ASTNodeProperty property;
    private static Map<String, SingleVariableDecProperties> sVariableProperties;
    
    //statiEc block for property map initialisation, 
    //set it static to avoid repeated initialisation on each call
    static {
    	sVariableProperties = new HashMap<String, SingleVariableDecProperties>();
        for (SingleVariableDecProperties p : SingleVariableDecProperties.values()) {
        	sVariableProperties.put(p.command, p);    
        }
    }

    SingleVariableDecProperties(String value, ASTNodeProperty property) {
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
        if(!sVariableProperties.containsKey(command)) {
        	 return null;
        }
        return sVariableProperties.get(command).property;
    }
}
