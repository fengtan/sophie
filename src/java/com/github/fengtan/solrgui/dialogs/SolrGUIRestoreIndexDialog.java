package com.github.fengtan.solrgui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.tables.SolrGUITable;

public class SolrGUIRestoreIndexDialog extends Dialog {
	
	private Text backupName;
	private SolrGUITable table;
	
	public SolrGUIRestoreIndexDialog(Shell parentShell, SolrGUITable table) {
		super(parentShell);
		this.table = table;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		new Label(composite, SWT.NULL).setText("Backup name");
		backupName = new Text(composite, SWT.BORDER);
		// TODO "Available backup names"
		// TODO leave empty for xxx
		// TODO set minimum size of textarea
	    
	    return composite;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Restore index from a backup");
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// button "OK' has ID "0".
		if (buttonId == 0) {
			table.restore(backupName.getText());
		}
		super.buttonPressed(buttonId);
	}
	
}
