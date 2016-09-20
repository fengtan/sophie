package com.github.fengtan.sophie.dialogs;

import java.io.IOException;
import java.util.Arrays;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.sophie.Sophie;
import com.github.fengtan.sophie.utils.SolrUtils;

public class CoreSwapDialog extends Dialog {
	
	private String coreName;
	private Combo otherCoreName;

	public CoreSwapDialog(String core1) {
		super(Sophie.shell);
		this.coreName = core1;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		new Label(composite, SWT.NULL).setText("Swap core \""+coreName+"\" with:");
		otherCoreName = new Combo(parent, SWT.DROP_DOWN);
		Object[] coreObjects = SolrUtils.getCores().keySet().toArray();
		String[] coreStrings = Arrays.copyOf(coreObjects, coreObjects.length, String[].class); 
		otherCoreName.setItems(coreStrings);
		// TODO include some spacing (left-hand side).

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
			// TODO contrib CoreAdminRequest.swapCores() - similar to CoreAdminRequest.renameCore().
			CoreAdminRequest request = new CoreAdminRequest();
			request.setCoreName(coreName);
			request.setOtherCoreName(otherCoreName.getText());
			request.setAction(CoreAdminAction.SWAP);
			try {
				request.process(Sophie.client);
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
