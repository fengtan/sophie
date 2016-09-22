package com.github.fengtan.sophie.dialogs;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.sophie.beans.SolrConnectionType;

public class ConnectDialog extends Dialog {
	
	private Button[] buttons = new Button[SolrConnectionType.values().length];
	private Label label;
	private Text value;

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
		if (buttonId == IDialogConstants.OK_ID) {
			switch(selectedType) {
				case DIRECT_HTTP:
				default:
					client = new HttpSolrClient(value.getText());
					break;
				case SOLR_CLOUD:
					client = new CloudSolrClient(value.getText());
					break;			
			}
			connectionLabel = value.getText() + " ("+selectedType.getTypeName()+")";
		} else {
			client = null;
			connectionLabel = null;
		}
        super.buttonPressed(buttonId);
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
        value = new Text(composite, SWT.SINGLE | SWT.BORDER);
        value.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));        
        value.setText(selectedType.getValueDefault());
        
        applyDialogFont(composite);
        return composite;
    }

}
