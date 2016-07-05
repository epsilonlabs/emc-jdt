package org.eclipse.epsilon.emc.jdt.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.emc.jdt.JdtReader;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumDeclaration;

/**
 * The enumeration for EnumDeclaration properties.
 * 
 * @author Cheng Yun
 *
 */
public enum EnumDecProperties {
	NAME("name", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return ((EnumDeclaration)object).getName().getFullyQualifiedName();
		}
	}), ANNOS("annotations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return Util.getAnnotations((EnumDeclaration)object);
		}
	}), MODIFIERS("modifiers", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return Util.getModifiers(((EnumDeclaration)object).getModifiers());
		}
	}), METHOD("methods", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getMethodDeclarations((EnumDeclaration)object);
		}
	}), STATEMENTS("statements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return Util.getStatements((EnumDeclaration) object);
		}
	}), INCLASSES("innerClasses", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getTypeDeclarations((EnumDeclaration)object);
		}
	}), INENUM("innerEnumerations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			List<EnumDeclaration> innerEnums = new ArrayList<EnumDeclaration>();
			for (EnumDeclaration enumTemp : JdtReader
					.getEnumDeclarations((EnumDeclaration)object)) {
				// if the parent of this enumeration is not a CompilationUnit, 
				// we judge that this class is an inner enumeration
				if (!(ASTNode.nodeClassForType(enumTemp.getParent()
						.getNodeType()).toString()
						.equals("class org.eclipse.jdt.core.dom.CompilationUnit"))) {
					innerEnums.add(enumTemp);
				}
			}
			return innerEnums;
		}
	});

    private String command;
    private ASTNodeProperty property;
    private static Map<String, EnumDecProperties> enumDecProperties;
    
    //static block for property map initialisation, 
    //set it static to avoid repeated initialisation on each call
    static {
    	enumDecProperties = new HashMap<String, EnumDecProperties>();
        for (EnumDecProperties p : EnumDecProperties.values()) {
        	enumDecProperties.put(p.command, p);    
        }
    }

    EnumDecProperties(String value, ASTNodeProperty property) {
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
        if(!enumDecProperties.containsKey(command)) {
        	 return null;
        }
        return enumDecProperties.get(command).property;
    }
}
