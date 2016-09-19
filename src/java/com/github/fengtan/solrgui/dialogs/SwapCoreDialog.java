package com.github.fengtan.solrgui.dialogs;

import java.util.Map;

import org.apache.solr.common.util.NamedList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.SolrGUI;
import com.github.fengtan.solrgui.utils.SolrUtils;

public class SwapCoreDialog extends Dialog {
	
	private String core1;
	private Combo core2;

	public SwapCoreDialog(String core1) {
		super(SolrGUI.shell);
		this.core1 = core1;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		new Label(composite, SWT.NULL).setText("Swap core \""+core1+"\" with:");
		core2 = new Combo(parent, SWT.DROP_DOWN);
		for (Map.Entry<String, NamedList<Object>> core: SolrUtils.getCores()) {

		}
		// TODO core2.setItems(SolrGUI.client.);

	    return composite;
	}

	/**
	 * Set title of the custom dialog.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Swap cores");
	}

	@Override
	protected void buttonPressed(int buttonId) {
		// button "OK' has ID "0".
		if (buttonId == 0) {
			try {
				// TODO SolrGUI.tabFolder.getCoresTabItem().getTable().renameCore(oldCoreName, newCoreName.getText());	
			} catch (Exception e) {
				SolrGUI.showException(e);
			}
		}
		super.buttonPressed(buttonId);
	}
}
