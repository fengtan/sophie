package com.github.fengtan.solrgui.dialogs;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.beans.SolrGUIConfig;
import com.github.fengtan.solrgui.beans.SolrGUIServer;
import com.github.fengtan.solrgui.tabs.SolrGUITabFolder;

public class SolrGUIAddServerDialog extends Dialog {

	private static final String DEFAULT_SERVER_NAME = "collection1@localhost";
	private static final String DEFAULT_SERVER_URL = "http://localhost:8983/solr/collection1";
	private static final String DEFAULT_SERVER_ROWS = "500";
	
	private Text name;
	private Text url;
	private Text rows;
	
	private SolrGUITabFolder tabFolder;
	
	// TODO allow http auth
	// TODO rows should be controllable using the UI - not with a regular setting
	public SolrGUIAddServerDialog(Shell parentShell, SolrGUITabFolder tabFolder) {
		super(parentShell);
		this.tabFolder = tabFolder;
	}
		
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		// TODO display inline
		new Label(composite, SWT.NULL).setText("Name");
		name = new Text(composite, SWT.BORDER); // TODO border
		name.setText(DEFAULT_SERVER_NAME);
		
		new Label(composite, SWT.NULL).setText("URL");
		url = new Text(composite, SWT.BORDER); // TODO border
		url.setText(DEFAULT_SERVER_URL);
	    
		new Label(composite, SWT.NULL).setText("Rows"); // TODO what if user enters garbage (e.g. not a number)
	    rows = new Text(composite, SWT.BORDER); // TODO border
	    rows.setText(DEFAULT_SERVER_ROWS);
	    
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
			// TODO create popup to get URL/name/rows/query
			try {
				SolrGUIServer server = new SolrGUIServer(new URL(url.getText()), name.getText(), "*:*", Integer.parseInt(rows.getText()));
	            SolrGUIConfig.addServer(server);
	            tabFolder.addTabItem(server);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.buttonPressed(buttonId);
	}

}
