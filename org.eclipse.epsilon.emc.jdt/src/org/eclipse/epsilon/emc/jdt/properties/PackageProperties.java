package org.eclipse.epsilon.emc.jdt.properties;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.epsilon.emc.jdt.JdtReader;
import org.eclipse.jdt.core.IPackageFragment;
/**
 * The enumeration for Package properties.
 * 
 * @author Cheng Yun
 *
 */
public enum PackageProperties {
	NAME("name", new Property() {
		@Override
		public Object run(Object object) {
			return ((IPackageFragment)object).getElementName(); 
		}
	}), FILES("compilationUnits", new Property() {
		@Override
		public Object run(Object object) {
			try {
				return JdtReader
						.getCompilationUnits((IPackageFragment)object);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			return null;
		}
	});
	
    private String command;
    private Property property;
    private static Map<String, PackageProperties> packageProperties;
    
    //static block for property map initialisation, 
    //set it static to avoid repeated initialisation on each call
    static {
    	packageProperties = new HashMap<String, PackageProperties>();
        for (PackageProperties p : PackageProperties.values()) {
        	packageProperties.put(p.command, p);    
        }
    }

    PackageProperties(String value, Property property) {
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
        if(!packageProperties.containsKey(command)) {
        	 return null;
        }
        return packageProperties.get(command).property;
    }
}

