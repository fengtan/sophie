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

public class NewCoreDialog extends Dialog {
	
	private static final String DEFAULT_NAME = "collectionX";
	private static final String DEFAULT_INSTANCE_DIR = "/path/to/solr/collectionX";
	
	private static NewCoreDialog dialog = null; 
	
	private Text coreName;
	private Text instanceDir;
	
	// TODO allow http auth
	private NewCoreDialog() {
		super(SolrGUI.shell);
	}
	
	/**
	 * Singleton
	 */
	public static NewCoreDialog getDialog() {
		if (dialog == null) {
			dialog = new NewCoreDialog();
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
				SolrGUI.tabFolder.getCoresTabItem().getTable().addCore(coreName.getText(), instanceDir.getText());	
			} catch (Exception e) {
				SolrGUI.showException(e);
			}
		}
		super.buttonPressed(buttonId);
	}
}
