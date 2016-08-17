package com.github.fengtan.solrgui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.tabs.DocumentsTabItem;

public class SolrGUIBackupIndexDialog extends Dialog {
	
	private Text backupName;
	
	public SolrGUIBackupIndexDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		new Label(composite, SWT.NULL).setText("Backup name");
		backupName = new Text(composite, SWT.BORDER);
		// TODO "Leave empty to use the default format (yyyyMMddHHmmssSSS)."
		// TODO set minimum size of textarea
	    
	    return composite;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Make a backup of the index");
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// button "OK' has ID "0".
		if (buttonId == 0) {
			DocumentsTabItem.table.backup(backupName.getText());
		}
		super.buttonPressed(buttonId);
	}
	
}
