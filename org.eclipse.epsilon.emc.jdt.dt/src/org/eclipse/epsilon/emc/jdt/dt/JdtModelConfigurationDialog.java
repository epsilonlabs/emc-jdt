package org.eclipse.epsilon.emc.jdt.dt;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.epsilon.common.dt.launching.dialogs.AbstractModelConfigurationDialog;
import org.eclipse.epsilon.emc.jdt.JdtReader;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

public class JdtModelConfigurationDialog extends AbstractModelConfigurationDialog {

	private List list;
	private Button selectAll;
	private Button deselectAll;
	private Button virtualCache;
	private Button realCache;
	private Button realCacheGC;
	private Button bindingSwitch;
	private int modelSelected;
	private boolean bindingFlag;
	public static final String PROJECTS = "projects selected";
	public static final String CACHING_STRATEGY = "model selected";
	public static final String RESOLVE_BINDINGS = "resolve bindings";
	public static final int VIRTUAL = 0; // virtual storage approach
	public static final int PHYSICAL = 1; // real storage approach
	public static final int PHYSICAL_GC = 2; // real storage with garbage
												// collection
	public static final int TURNONBINDINGS = 10;
	public static final int TURNOFFBINDINGS = 20;

	@Override
	protected String getModelName() {
		return "Java (JDT) Model";
	}

	@Override
	protected String getModelType() {
		return "JDT";
	}

	private void createProjectSelectionGroup(Composite parent) {
		final Composite groupContent = createGroupContainer(parent, "Projects",
				1);

		getList(groupContent);
		createRadioButtonGroup(groupContent);
		groupContent.layout();
	}

	private void createRadioButtonGroup(Composite parent) {
		final Composite groupContent = createGroupContainer(parent, "", 2);
		getSelectButton(groupContent);
		getDeSelectButton(groupContent);
		groupContent.layout();
	}

	private void createModelSelectionGroup(Composite parent) {
		final Composite groupContent = createGroupContainer(parent, "Models:",
				2);
		virtualCache = createModelSelectionButton(groupContent,
				"Virtual caching", VIRTUAL);
		realCache = createModelSelectionButton(groupContent,
				"Physical caching", PHYSICAL);
		realCacheGC = createModelSelectionButton(groupContent,
				"Physical caching with GC (not implemented yet)", PHYSICAL);
		realCacheGC.setEnabled(false);// not implemented yet
		groupContent.layout();
	}

	private void createBidingSwitchGroup(Composite parent) {
		final Composite groupContent = createGroupContainer(parent,
				"Bindings:", 2);
		getBindingSwitch(groupContent);
		groupContent.layout();
	}

	private List getList(Composite groupContent) {
		list = new List(groupContent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);

		// project list
		final ArrayList<String> projectNameList = new ArrayList<String>();
		java.util.List<IJavaProject> javaProjects = getJavaProjects();
		for (IJavaProject javaProject : javaProjects) {
			projectNameList.add(javaProject.getProject().getName());
		}

		list.setItems(projectNameList.toArray(new String[projectNameList.size()]));
		list.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		return list;
	}

	private Button getSelectButton(Composite groupContent) {
		selectAll = new Button(groupContent, SWT.RADIO);

		// selection radio buttons
		selectAll.setText("Select all");
		selectAll.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (selectAll.getSelection() == true) {
					list.selectAll();
				}
			}
		});
		return selectAll;
	}

	private Button getDeSelectButton(Composite groupContent) {
		deselectAll = new Button(groupContent, SWT.RADIO);

		// selection radio buttons
		deselectAll.setText("Deselect all");
		deselectAll.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (deselectAll.getSelection() == true) {
					list.deselectAll();
				}
			}
		});
		return deselectAll;
	}

	/**
	 * @param groupContent
	 * @param text
	 * @param model
	 * @return
	 */
	private Button createModelSelectionButton(Composite groupContent,
			String text, int model) {
		final Button button = new Button(groupContent, SWT.RADIO);
		button.setText(text);

		button.addListener(SWT.Selection, new ModelSelectionLitener(button,
				model));
		return button;
	}

	private Button getBindingSwitch(Composite groupContent) {
		bindingSwitch = new Button(groupContent, SWT.CHECK);

		bindingSwitch.setText("Resolve bindings");
		bindingSwitch.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (bindingSwitch.getSelection() == true) {
					bindingFlag = true;
				} else {
					bindingFlag = false;
				}
			}
		});
		return realCacheGC;
	}

	private java.util.List<IJavaProject> getJavaProjects() {
		java.util.List<IProject> projects = JdtReader.getIProjects();
		try {
			// get all java projects
			return JdtReader.getIJavaProjects(projects);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void createGroups(Composite control) {
		createNameAliasGroup(control);
		createProjectSelectionGroup(control);
		createModelSelectionGroup(control);
		createBidingSwitchGroup(control);
	}

	protected void loadProperties() {
		super.loadProperties();
		if (properties == null)
			return;
		// get selected projects'names
		String[] selection = properties.getProperty(PROJECTS).split(",");
		list.setSelection(selection);

		// model selection
		String str = properties.getProperty(CACHING_STRATEGY);
		if (str != "") {
			int temp = Integer.parseInt(str);

			switch (temp) {
			case VIRTUAL:
				modelSelected = VIRTUAL;
				virtualCache.setSelection(true);
				break;
			case PHYSICAL:
				modelSelected = PHYSICAL;
				realCache.setSelection(true);
				break;
			case PHYSICAL_GC:
				modelSelected = PHYSICAL_GC;
				realCacheGC.setSelection(true);
				break;
			default:
				modelSelected = VIRTUAL;
				virtualCache.setSelection(true);
				break;
			}
		} else {
			modelSelected = VIRTUAL;
			virtualCache.setSelection(true);
		}

		// binding switch
		String bindingStr = properties.getProperty(RESOLVE_BINDINGS);
		if (bindingStr != "") {
			boolean bindingTemp = Boolean.parseBoolean(bindingStr);
			if (bindingTemp == true) {
				bindingFlag = true;
				bindingSwitch.setSelection(true);
			} else {
				bindingFlag = false;
				bindingSwitch.setSelection(false);
			}
		}

	}

	protected void storeProperties() {
		super.storeProperties();
		String projectStr = "";
		// put the names of selected projects in a string, separated by commas
		for (String str : list.getSelection()) {
			if (projectStr.equals(""))
				projectStr = str;
			else
				projectStr = projectStr + "," + str;
		}
		super.properties.put(PROJECTS, projectStr);
		super.properties.put(CACHING_STRATEGY, modelSelected);
		super.properties.put(RESOLVE_BINDINGS, bindingFlag);
	}

	// Lister for model selection buttons
	class ModelSelectionLitener implements Listener {
		Button selected;
		int model;

		public ModelSelectionLitener(Button selected, int model) {
			this.selected = selected;
			this.model = model;
		}

		public void handleEvent(Event event) {
			if (selected.getSelection() == true) {
				modelSelected = model;
			}
		}
	}
}
