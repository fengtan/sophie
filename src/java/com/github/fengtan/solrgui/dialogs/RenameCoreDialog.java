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

public class RenameCoreDialog extends Dialog {
	
	private String oldCoreName;
	private Text newCoreName;

	public RenameCoreDialog(String oldCoreName) {
		super(SolrGUI.shell);
		this.oldCoreName = oldCoreName;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		new Label(composite, SWT.NULL).setText("New name ("+oldCoreName+")");
		newCoreName = new Text(composite, SWT.BORDER);
		newCoreName.setText(oldCoreName);

	    return composite;
	}

	/**
	 * Set title of the custom dialog.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Rename core");
	}

	@Override
	protected void buttonPressed(int buttonId) {
		// button "OK' has ID "0".
		if (buttonId == 0) {
			try {
				SolrGUI.tabFolder.getCoresTabItem().getTable().renameCore(oldCoreName, newCoreName.getText());	
			} catch (Exception e) {
				SolrGUI.showException(e);
			}
		}
		super.buttonPressed(buttonId);
	}
}
