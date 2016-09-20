package com.github.fengtan.sophie.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

public abstract class DocumentEditValueDialog extends Dialog {

	private Object value = null;
	
	public DocumentEditValueDialog(Shell parentShell) {
		super(parentShell);
		// Allow user to resize the dialog window.
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	// Set title of the custom dialog.
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Edit value");
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// TODO NPE when editing unstored field
		// TODO cannot edit empty values (window closes)
		if (buttonId == IDialogConstants.OK_ID) {
			value = getValue();
		} else {
			value = null;
		}
		super.buttonPressed(buttonId);
	}
	
	public Object getValue() {
		return value;
	}

	protected abstract Object fetchValue();
	
}
