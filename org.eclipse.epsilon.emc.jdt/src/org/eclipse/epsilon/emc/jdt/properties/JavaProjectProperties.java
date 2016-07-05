package org.eclipse.epsilon.emc.jdt.properties;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.epsilon.emc.jdt.JdtReader;
import org.eclipse.jdt.core.IJavaProject;

/**
 * The enumeration for JavaProject properties.
 * 
 * @author Cheng Yun
 *
 */
public enum JavaProjectProperties {
	NAME("name", new Property() {
		@Override
		public Object run(Object object) {
			return ((IJavaProject)object).getProject().getName(); 
		}
	}), PACKS("packages", new Property() {
		@Override
		public Object run(Object object) {
			try {
				return JdtReader.getIPackageFragments(
						((IJavaProject)object), true);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			return null;
		}
	});
	
    private String command;
    private Property property;
    private static Map<String, JavaProjectProperties> javaProjectProperties;
    
    //static block for property map initialisation, 
    //set it static to avoid repeated initialisation on each call
    static {
    	javaProjectProperties = new HashMap<String, JavaProjectProperties>();
        for (JavaProjectProperties p : JavaProjectProperties.values()) {
        	javaProjectProperties.put(p.command, p);    
        }
    }

    JavaProjectProperties(String value, Property property) {
        this.command= value;
        this.property = property;
    }
    
    /**
     * returns the requested property, throws RuntimeException if command not found
     * @param command a specific value of the enumeration
     * @return a property
     * @throws RuntimeException
     */
    public static Property getProperty(String command) {
        if(!javaProjectProperties.containsKey(command)) {
        	 return null;
        }
        return javaProjectProperties.get(command).property;
    }
}

