package com.github.fengtan.solrgui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.SolrGUI;
import com.github.fengtan.solrgui.config.Config;

// TODO is this class used ?
public class ConnectDialog extends Dialog {

	private static final String DEFAULT_URL = "http://localhost:8983/solr/collection1";
	
	private static ConnectDialog dialog = null; 
	
	private Text url;
	
	// TODO allow http auth
	private ConnectDialog() {
		super(SolrGUI.shell);
	}
	
	/**
	 * Singleton
	 */
	public static ConnectDialog getDialog() {
		if (dialog == null) {
			dialog = new ConnectDialog();
		}
		return dialog;
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
		newShell.setText("Connect to Solr");
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// button "OK' has ID "0".
		if (buttonId == 0) {
			// TODO do not create if server already exists.
	    	// TODO validate connection/url before saving
            Config.setURL(url.getText());
            // TODO connect to new URL
		}
		super.buttonPressed(buttonId);
	}

}
