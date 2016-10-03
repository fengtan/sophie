package com.github.fengtan.sophie.dialogs;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.sophie.beans.Config;
import com.github.fengtan.sophie.beans.SolrConnectionType;
import com.github.fengtan.sophie.beans.SophieException;

public class ConnectDialog extends Dialog {
	
	private Label label;
	private Combo value; // TODO verify swt renders Combo as free-form text in Mac OS + Windows
	private Button[] buttons = new Button[SolrConnectionType.values().length];

	private SolrConnectionType selectedType = SolrConnectionType.DIRECT_HTTP; // Select 'Direct HTTP' by default.
	private SolrClient client = null;
	private String connectionLabel = null;
	
	public ConnectDialog(Shell shell) {
		super(shell);
	}

	/**
	 * Set title of the custom dialog.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Solr connection");
	}

	public SolrClient getSolrClient() {
		return client;
	}
	
	public String getConnectionLabel() {
		return connectionLabel;
	}
	
    protected void buttonPressed(int buttonId) {
		if (buttonId != IDialogConstants.OK_ID) {
			super.buttonPressed(buttonId);
			return;
		}
		connectionLabel = value.getText() + " ("+selectedType.getTypeName()+")";
		switch(selectedType) {
			case DIRECT_HTTP:
			default:
				client = new HttpSolrClient(value.getText());
				break;
			case SOLR_CLOUD:
				client = new CloudSolrClient(value.getText());
				break;			
		}
		// If we have a valid client, then add it to the favorites.
		// Otherwise, show dialog box.
		try {
			testConnection(client);
			Config.addFavorite(value.getText());
			super.buttonPressed(buttonId);	
		} catch (SophieException e) {
			ExceptionDialog.open(getShell(), e);
		}
    }
    
    // TODO what if admin/ping does not exist ?
    private static void testConnection(SolrClient client) throws SophieException {
    	SolrPingResponse ping = null;
    	try {
    		ping = client.ping();
    	} catch(SolrServerException|IOException|RuntimeException e) {
    		throw new SophieException("Unable to connect to Solr", e);
    	}
    	if (ping != null && ping.getStatus() != 0) {
    		throw new SophieException("Unable to connect to Solr");
    	}
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        
        for (final SolrConnectionType type:SolrConnectionType.values()) {
        	Button button = new Button(composite, SWT.RADIO);
        	button.setText(type.getTypeName());
        	button.addSelectionListener(new SelectionAdapter() {
            	@Override
            	public void widgetSelected(SelectionEvent e) {
            		selectedType = type;
            		label.setText(selectedType.getValueLabel());
            		value.setText(selectedType.getValueDefault());
            		super.widgetSelected(e);
            	}
    		});
        	if (type == selectedType) {
        		button.setSelection(true);
        	}
        	buttons[type.ordinal()] = button;
        }
        
        label = new Label(composite, SWT.WRAP);
        label.setText(selectedType.getValueLabel());
        GridData grid = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        grid.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        label.setLayoutData(grid);
        label.setFont(parent.getFont());
        
        value = new Combo(composite, SWT.SINGLE | SWT.BORDER);
        value.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        String[] favorites = Config.getFavorites();
        String[] items = ArrayUtils.isNotEmpty(favorites) ? favorites : new String[]{selectedType.getValueDefault()};
        value.setItems(items);
        value.setText(items[0]);
        
        applyDialogFont(composite);
        return composite;
    }

}
