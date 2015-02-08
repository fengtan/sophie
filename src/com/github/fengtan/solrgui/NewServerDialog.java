package com.github.fengtan.solrgui;

import org.eclipse.swt.widgets.Shell;

public class NewServerDialog {

	private Shell dialog;
	
	public NewServerDialog(Shell shell) {
		dialog = new Shell(shell);
        dialog.setText("New Solr server");
        dialog.setSize(200, 200);
	}
	
	public void open() {
		dialog.open();
	}
	
}
