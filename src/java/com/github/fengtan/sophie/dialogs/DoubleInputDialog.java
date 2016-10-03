package com.github.fengtan.sophie.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DoubleInputDialog extends Dialog {
	
	private String title;
	private String text1;
	private String text2;
	private String defaultValue1;
	private String defaultValue2;
	
	private Text input1;
	private Text input2;
	
	private String value1 = null;
	private String value2 = null;
	
	public DoubleInputDialog(Shell shell, String title, String text1, String defaultValue1, String text2, String defaultValue2) {
		super(shell);
		this.title = title;
		this.text1 = text1;
		this.text2 = text2;
		this.defaultValue1 = defaultValue1;
		this.defaultValue2 = defaultValue2;
	}

	/**
	 * Set title of the custom dialog.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}

	public String getValue1() {
		return value1;
	}
	
	public String getValue2() {
		return value2;
	}


    protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			value1 = input1.getText();
			value2 = input2.getText();
		}
        super.buttonPressed(buttonId);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        
        GridData grid = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        grid.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        
        Label label1 = new Label(composite, SWT.WRAP);
        label1.setText(text1);
        label1.setLayoutData(grid);
        label1.setFont(parent.getFont());
        input1 = new Text(composite, SWT.SINGLE | SWT.BORDER);
        input1.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        input1.setText(defaultValue1);

        Label label2 = new Label(composite, SWT.WRAP);
        label2.setText(text2);
        label2.setLayoutData(grid);
        label2.setFont(parent.getFont());
        input2 = new Text(composite, SWT.SINGLE | SWT.BORDER);
        input2.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));        
        input2.setText(defaultValue2);
        
        applyDialogFont(composite);
        return composite;
    }

}
