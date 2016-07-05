package org.eclipse.epsilon.emc.jdt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolEnumerationValueNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.exceptions.models.EolNotInstantiableModelElementTypeException;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.eol.models.Model;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;

/**
 * This is the JDT model class.
 * 
 * @author Cheng Yun
 * 
 */
public class JdtModel extends Model {
	private List<String> supportedTypes = Arrays.asList("JavaProject", "Package");
	private List<IJavaProject> javaProjects = new ArrayList<IJavaProject>();
	private ASTReflection astModel;

	public static final String PROPERTY_PROJECTS = "projects selected";
	public static final String PROPERTY_CACHING_STRATEGY = "model selected";
	public static final String PROPERTY_RESOLVE_BINDINGS = "resolve bindings";
	public static final int VIRTUAL = 0; // virtual storage approach
	public static final int PHYSICAL = 1; // real storage approach
	public static final int PHYSICAL_GC = 2; // real storage with garbage
	
	@Override
	public void load() throws EolModelLoadingException {

	}

	@Override
	public void load(StringProperties properties, IRelativePathResolver resolver)
			throws EolModelLoadingException {
		super.load(properties, resolver);
		String projects = properties.getProperty(JdtModel.PROPERTY_PROJECTS);
		String[] projectsSelected = null;
		if (projects.length() != 0) {
			projectsSelected = projects.split(",");
		}
		getJavaProjects(projectsSelected);

		boolean resolveBindings = Boolean.parseBoolean(properties
				.getProperty(JdtModel.PROPERTY_RESOLVE_BINDINGS));

		try {
			int cachingStrategy = Integer.parseInt(properties.getProperty(JdtModel.PROPERTY_CACHING_STRATEGY));

			switch (cachingStrategy) {
			case JdtModel.VIRTUAL:
				astModel = new ReflectiveASTVisitor(javaProjects, resolveBindings);
				break;
			case JdtModel.PHYSICAL:
				astModel = new ASTModel(javaProjects, resolveBindings);
				break;
			case JdtModel.PHYSICAL_GC:
				// TODO
				astModel = new ASTModel(javaProjects, resolveBindings);
				break;
			default:
				astModel = new ReflectiveASTVisitor(javaProjects, resolveBindings);
				break;
			}

		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getEnumerationValue(String enumeration, String label)
			throws EolEnumerationValueNotFoundException {
		return null;
	}

	@Override
	public Collection<?> allContents() {
		try {
			return astModel.getAllOfKind("ComopilationUnit");
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return new ArrayList<Object>();
	}

	@Override
	public Collection<?> getAllOfType(String type)
			throws EolModelElementTypeNotFoundException {

		// get java projects
		if ("JavaProject".equals(type)) {
			return javaProjects;
		}

		// get packages
		if ("Package".equals(type)) {
			try {
				return JdtReader.getIPackageFragments(javaProjects, true);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}

		try {
			return astModel.getAllOfType(type);
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Collection<?> getAllOfKind(String type)
			throws EolModelElementTypeNotFoundException {
		// get java projects
		if ("JavaProject".equals(type)) {
			return javaProjects;
		}

		// get packages
		if ("Package".equals(type)) {
			List<IPackageFragment> packages = new ArrayList<IPackageFragment>();
			try {
				// get packages
				packages.addAll(JdtReader.getIPackageFragments(javaProjects,
						true));
			} catch (CoreException e) {
				e.printStackTrace();
			}
			return packages;
		}

		try {
			return astModel.getAllOfKind(type);
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}

		// return getAllOfType(type);
	}

	@Override
	public String getTypeNameOf(Object instance) {
		if (instance instanceof IJavaProject) {
			return IJavaProject.class.toString();
		}

		if (instance instanceof IPackageFragment) {
			return IJavaProject.class.toString();
		}

		if (instance instanceof ASTNode) {
			ASTNode node = (ASTNode) instance;
			return ASTNode.nodeClassForType(node.getNodeType()).toString();
		}

		return "";
	}

	@Override
	public boolean isOfType(Object instance, String metaClass)
			throws EolModelElementTypeNotFoundException {
		return instance.getClass().getSimpleName().equals(metaClass);
	}

	@Override
	public Object createInstance(String type)
			throws EolModelElementTypeNotFoundException,
			EolNotInstantiableModelElementTypeException {

		return null;
	}

	@Override
	public Object getElementById(String id) {

		return null;
	}

	@Override
	public String getElementId(Object instance) {

		return null;
	}

	@Override
	public void setElementId(Object instance, String newId) {

	}

	@Override
	public void deleteElement(Object instance) throws EolRuntimeException {

	}

	@Override
	public boolean owns(Object instance) {
		if (instance instanceof IJavaElement || instance instanceof ASTNode) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isInstantiable(String type) {
		return true;
	}

	@Override
	public boolean hasType(String type) {
		try {
			return supportedTypes.contains(type)
					|| (Class.forName("org.eclipse.jdt.core.dom." + type) != null);
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	@Override
	public boolean store(String location) {
		return false;
	}

	@Override
	public boolean store() {
		return false;
	}

	@Override
	public IPropertyGetter getPropertyGetter() {
		return new JdtPropertyGetter();
	}

	private List<IJavaProject> getJavaProjects(String[] projectNames) {
		List<IProject> projects;
		if (projectNames == null) {
			// get all projects in the workspace including non-Java projects
			projects = JdtReader.getIProjects();
		} else {
			// get selected projects in the workspace
			projects = JdtReader.getIProjects(projectNames);
		}
		try {
			// get java projects
			javaProjects.addAll(JdtReader.getIJavaProjects(projects));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return javaProjects;
	}
}
