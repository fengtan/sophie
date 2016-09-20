package com.github.fengtan.sophie.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.sophie.Sophie;

public class DocumentEditTextValueDialog extends DocumentEditValueDialog {

	private String defaultValue;
	private Text text;
	
	public DocumentEditTextValueDialog(String defaultValue) {
		super(Sophie.shell);
		this.defaultValue = defaultValue;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		text = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setText(defaultValue);
		GridData grid = new GridData();
		grid.widthHint = 500;
		grid.heightHint = 50;
		grid.horizontalAlignment = GridData.FILL;;
		grid.verticalAlignment = GridData.FILL;;
		grid.grabExcessHorizontalSpace = true;
		grid.grabExcessVerticalSpace = true;
		text.setLayoutData(grid);
		return composite;
	}

	@Override
	protected Object getValue() {
		return text.getText();
	}

}
