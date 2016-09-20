package com.github.fengtan.sophie.dialogs;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.sophie.Sophie;

public class CoreAddDialog extends Dialog {
	
	private static final String DEFAULT_NAME = "collectionX";
	private static final String DEFAULT_INSTANCE_DIR = "/path/to/solr/collectionX";
	
	private static CoreAddDialog dialog = null; 
	
	private Text coreName;
	private Text instanceDir;
	
	// TODO allow http auth
	private CoreAddDialog() {
		super(Sophie.shell);
	}
	
	/**
	 * Singleton
	 */
	public static CoreAddDialog getDialog() {
		if (dialog == null) {
			dialog = new CoreAddDialog();
		}
		return dialog;
	}
		
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		new Label(composite, SWT.NULL).setText("Name");
		coreName = new Text(composite, SWT.BORDER);
		coreName.setText(DEFAULT_NAME);
		
		new Label(composite, SWT.NULL).setText("Instance directory");
		instanceDir = new Text(composite, SWT.BORDER);
		instanceDir.setText(DEFAULT_INSTANCE_DIR);
	    
	    return composite;
	}

	/**
	 * Set title of the custom dialog.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add new core");
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// button "OK' has ID "0".
		if (buttonId == 0) {
			try {
				// TODO createCore is overloaded with additional params (schema file etc).
				CoreAdminRequest.createCore(coreName.getText(), instanceDir.getText(), Sophie.client);
				Sophie.tabFolder.getCoresTabItem().getTable().refresh();
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		super.buttonPressed(buttonId);
	}
}
