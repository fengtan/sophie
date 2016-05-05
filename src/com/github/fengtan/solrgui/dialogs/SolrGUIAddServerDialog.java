package com.github.fengtan.solrgui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.beans.SolrGUIConfig;
import com.github.fengtan.solrgui.tabs.SolrGUITabFolder;

public class SolrGUIAddServerDialog extends Dialog {

	private static final String DEFAULT_URL = "http://localhost:8983/solr/collection1";

	private Text url;
	
	private SolrGUITabFolder tabFolder;
	
	// TODO allow http auth
	public SolrGUIAddServerDialog(Shell parentShell, SolrGUITabFolder tabFolder) {
		super(parentShell);
		this.tabFolder = tabFolder;
	}
		
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		new Label(composite, SWT.NULL).setText("URL");
		url = new Text(composite, SWT.BORDER);
		url.setText(DEFAULT_URL);
	    
	    return composite;
	}

	// Set title of the custom dialog.
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add new server");
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// button "OK' has ID "0".
		if (buttonId == 0) {
			// TODO do not create if server already exists.
	    	// TODO validate connection/url before saving
            SolrGUIConfig.addServer(url.getText());
            tabFolder.addTabItem(url.getText());
		}
		super.buttonPressed(buttonId);
	}

}
