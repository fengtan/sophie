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

public class SolrGUIBackupIndexDialog extends Dialog {
	
	private Text backupName;
	private SolrGUITable table;
	
	public SolrGUIBackupIndexDialog(Shell parentShell, SolrGUITable table) {
		super(parentShell);
		this.table = table;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		// TODO "please provide name ; leave empty for yyyy-mm-ddd"
		new Label(composite, SWT.NULL).setText("Backup name");
		backupName = new Text(composite, SWT.BORDER);
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
			table.backup(backupName.getText());
		}
		super.buttonPressed(buttonId);
	}
	
}
