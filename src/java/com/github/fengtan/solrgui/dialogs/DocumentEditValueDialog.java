package com.github.fengtan.solrgui.dialogs;

import java.util.Objects;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.solrgui.SolrGUI;

public abstract class DocumentEditValueDialog extends Dialog {
	
	private Object defaultValue;
	private TableItem item;
	private int columnIndex;
	
	public DocumentEditValueDialog(Shell parentShell) {
		super(parentShell);
		// Allow user to resize the dialog window.
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	protected abstract Object getValue();
	
	// Set title of the custom dialog.
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Edit value");
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// button "OK' has ID "0".
		if (buttonId == 0) {
			Object value = getValue();
			if (!Objects.equals(defaultValue, value)) {
				SolrGUI.tabFolder.getDocumentsTabItem().getTable().updateDocument(item, columnIndex, value);	
			}
		}
		super.buttonPressed(buttonId);
	}

	// TODO cannot edit empty values (window closes)
	public int open(Object defaultValue, TableItem item, int columnIndex) {
		this.defaultValue = defaultValue;
		this.item = item;
		this.columnIndex = columnIndex;
		return super.open();
	}
	
}
