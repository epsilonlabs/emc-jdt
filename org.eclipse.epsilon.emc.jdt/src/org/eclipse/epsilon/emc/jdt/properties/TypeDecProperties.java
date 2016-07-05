package org.eclipse.epsilon.emc.jdt.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.emc.jdt.JdtReader;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * The enumeration for TypeDeclaration properties.
 * 
 * @author Cheng Yun
 *
 */
public enum TypeDecProperties {
	NAME("name", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return ((TypeDeclaration)object).getName().getFullyQualifiedName();
		}
	}), ANNOS("annotations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return Util.getAnnotations((TypeDeclaration)object);
		}
	}), MODIFIERS("modifiers", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return Util.getModifiers(((TypeDeclaration)object).getModifiers());
		}
	}), BLOCKS("blocks", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getBlocks((TypeDeclaration)object);
		}
	}), STATEMENTS("statements", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return Util.getStatements((TypeDeclaration) object);
		}
	}), INCLASSES("innerClasses", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			List<TypeDeclaration> innerClasses = new ArrayList<TypeDeclaration>();
			for (TypeDeclaration type : JdtReader
					.getTypeDeclarations((TypeDeclaration)object)) {
				// if the parent of this class is not a CompilationUnit, 
				// we judge that this class is an inner class
				if (!(ASTNode.nodeClassForType(type.getParent()
						.getNodeType()).toString()
						.equals("class org.eclipse.jdt.core.dom.CompilationUnit"))) {
					innerClasses.add(type);
				}
			}
			return innerClasses;
		}
	}), INENUM("innerEnumerations", new ASTNodeProperty() {
		@Override
		public Object run(ASTNode object) {
			return JdtReader.getEnumDeclarations((TypeDeclaration)object);
		}
	});

    private String command;
    private ASTNodeProperty property;
    private static Map<String, TypeDecProperties> typeDecProperties;
    
    //static block for property map initialisation, 
    //set it static to avoid repeated initialisation on each call
    static {
    	typeDecProperties = new HashMap<String, TypeDecProperties>();
        for (TypeDecProperties p : TypeDecProperties.values()) {
        	typeDecProperties.put(p.command, p);    
        }
    }

    TypeDecProperties(String value, ASTNodeProperty property) {
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
        if(!typeDecProperties.containsKey(command)) {
        	 return null;
        }
        return typeDecProperties.get(command).property;
    }
}
