package com.github.fengtan.sophie.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class CComboDialog extends Dialog {
	
	private Shell shell;
	private String title;
	private String text;
	private String[] items;
	private IInputValidator validator;
	
	private Combo combo;
	private String value = null;

	public CComboDialog(Shell shell, String title, String text, String[] items, IInputValidator validator) {
		super(shell);
		this.shell = shell;
		this.title = title;
		this.text = text;
		this.items = items;
		this.validator = validator;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        
        Label label = new Label(composite, SWT.WRAP);
        GridData grid = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        grid.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        label.setText(text);
        label.setLayoutData(grid);
        label.setFont(parent.getFont());
		
        combo = new Combo(parent, SWT.SINGLE | SWT.BORDER); // TODO add some spacing
        combo.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		combo.setItems(items);

        applyDialogFont(composite);
        return composite;
	}

	/**
	 * Set title of the custom dialog.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			if (validator != null) {
				String errorMessage = validator.isValid(combo.getText());
				if (errorMessage != null) {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					messageBox.setText("Invalid value");
					messageBox.setMessage(errorMessage);
					messageBox.open();
					return;	
				}
			}
			value = combo.getText();
		}
		super.buttonPressed(buttonId);
	}
	
	public String getValue() {
		return value;
	}
	
}
