package com.github.fengtan.sophie.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CoreAddDialog extends Dialog {
	
	private static final String DEFAULT_NAME = "collectionX";
	private static final String DEFAULT_INSTANCE_DIR = "/path/to/solr/collectionX";
	
	private Text coreName;
	private Text instanceDir;
	
	private String coreNameValue = null;
	private String instanceDirValue = null;
	
	public CoreAddDialog(Shell shell) {
		super(shell);
	}
		
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		new Label(composite, SWT.NULL).setText("Name");
		coreName = new Text(composite, SWT.BORDER);
		coreName.setText(DEFAULT_NAME);
		
		new Label(composite, SWT.NULL).setText("Instance directory");
		instanceDir = new Text(composite, SWT.BORDER);
		instanceDir.setText(DEFAULT_INSTANCE_DIR);
	    
	    return composite;
	}

	/**
	 * Set title of the custom dialog.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add new core");
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			coreNameValue = coreName.getText();
			instanceDirValue = instanceDir.getText();
		} else {
			coreNameValue = null;
			instanceDirValue = null;
		}
		super.buttonPressed(buttonId);
	}
	
	public String getCoreNameValue() {
		return coreNameValue;
	}
	
	public String getInstanceDirValue() {
		return instanceDirValue;
	}

}
