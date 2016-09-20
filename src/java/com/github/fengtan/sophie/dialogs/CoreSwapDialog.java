package com.github.fengtan.sophie.dialogs;

import java.util.Arrays;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.sophie.utils.SolrUtils;

public class CoreSwapDialog extends Dialog {
	
	private String coreName;
	private Combo otherCoreName;
	private String value = null;

	public CoreSwapDialog(Shell shell, String core1) {
		super(shell);
		this.coreName = core1;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		new Label(composite, SWT.NULL).setText("Swap core \""+coreName+"\" with:");
		otherCoreName = new Combo(parent, SWT.DROP_DOWN);
		Object[] coreObjects = SolrUtils.getCores().keySet().toArray();
		String[] coreStrings = Arrays.copyOf(coreObjects, coreObjects.length, String[].class); 
		otherCoreName.setItems(coreStrings);
		// TODO include some spacing (left-hand side).

	    return composite;
	}

	/**
	 * Set title of the custom dialog.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Swap cores");
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			value = otherCoreName.getText();
		} else {
			value = null;
		}
		super.buttonPressed(buttonId);
	}
	
	public String getValue() {
		return value;
	}
}
