package com.github.fengtan.sophie.dialogs;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class ExceptionDialog extends Dialog {

	private static final int LIST_ITEM_COUNT = 7;

	private Button detailsButton;
	private Throwable throwable;
	private List list;
	private boolean listCreated = false;

	public static void open(Shell shell, Throwable throwable) {
		new ExceptionDialog(shell, throwable).open();
	}
	
	private ExceptionDialog(Shell shell, Throwable throwable) {
		super(shell);
		this.throwable = throwable;
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	protected void buttonPressed(int id) {
		if (id == IDialogConstants.DETAILS_ID) {
			toggleDetails();
		} else {
			super.buttonPressed(id);
		} 
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("An error happened");
	}


	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		detailsButton = createButton(parent, IDialogConstants.DETAILS_ID, IDialogConstants.SHOW_DETAILS_LABEL, false);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		((GridLayout) composite.getLayout()).numColumns = 2;
	
		Label label = new Label(composite, SWT.WRAP);
		label.setText(throwable.getMessage());
		GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
		data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
		label.setLayoutData(data);

		return composite;
	}

	protected List createDropDownList(Composite parent) {
		list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	
		for (String line:ExceptionUtils.getFullStackTrace(throwable).split("\n")) {
			list.add(line);
		}
			
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
		data.heightHint = list.getItemHeight() * LIST_ITEM_COUNT;
		list.setLayoutData(data);
		listCreated = true;
		return list;
	}

	private void toggleDetails() {
		Point windowSize = getShell().getSize();
		Point oldSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);
	
		if (listCreated) {
			list.dispose();
			listCreated = false;
			detailsButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
		} else {
			list = createDropDownList((Composite)getContents());
			detailsButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
		}
	
		Point newSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);
	
		getShell().setSize(new Point(windowSize.x, windowSize.y + (newSize.y - oldSize.y)));
	}

}