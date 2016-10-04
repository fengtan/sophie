/**
 * Sophie - A Solr browser and administration tool
 * 
 * Copyright (C) 2016 fengtan<https://github.com/fengtan>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

/**
 * Dialog to collect a Solr connection string / connection type.
 */
public class ConnectDialog extends Dialog {

    /**
     * Label explaining what kind of value we expect from the user.
     */
    private Label label;

    /**
     * Radio buttons to select the Solr connection type.
     */
    private Button[] buttons = new Button[SolrConnectionType.values().length];

    /**
     * Combo to let the user enter the Solr connection string.
     */
    private Combo value;

    /**
     * Connection type selected by default in the radio buttons.
     */
    private SolrConnectionType selectedType = SolrConnectionType.DIRECT_HTTP;

    /**
     * Solr client instantiated from the user-provided connection type and
     * connection string.
     */
    private SolrClient client = null;

    /**
     * Connection label (concatenation of the connection string and the
     * connection type). Used in the window title and the tab folder title.
     */
    private String connectionLabel = null;

    /**
     * Create a new dialog to collect a Solr connection string / connection
     * type.
     */
    public ConnectDialog(Shell shell) {
        super(shell);
    }

    /**
     * Get Solr client instantiated from the user-provided connection type and
     * connection string.
     * 
     * @return Solr client.
     */
    public SolrClient getSolrClient() {
        return client;
    }

    /**
     * Return the connection label (concatenation of the connection string and
     * the connection type).
     * 
     * @return Connection label.
     */
    public String getConnectionLabel() {
        return connectionLabel;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Sophie - New connection");
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId != IDialogConstants.OK_ID) {
            super.buttonPressed(buttonId);
            return;
        }
        
        // Populate connection label.
        connectionLabel = value.getText() + " (" + selectedType.getTypeName() + ")";
        
        // Instantiate Solr client based on what the user provided.
        switch (selectedType) {
        case DIRECT_HTTP:
        default:
            client = new HttpSolrClient(value.getText());
            break;
        case SOLR_CLOUD:
            client = new CloudSolrClient(value.getText());
            break;
        }
        
        // Test if we have a valid client.
        // If we do, then add it to the favorites.
        // If we do not, then open a dialog box and let the user enter a new
        // value.
        try {
            testConnection(client);
            Config.addFavorite(value.getText());
            super.buttonPressed(buttonId);
        } catch (SophieException e) {
            ExceptionDialog.open(getShell(), e);
        }
    }

    /**
     * Test if the remote Solr server can be reached.
     * 
     * TODO what if admin/ping does not exist
     * 
     * @param client
     *            Solr client.
     * @throws SophieException
     *             If the remote Solr server cannot be reached.
     */
    private static void testConnection(SolrClient client) throws SophieException {
        SolrPingResponse ping = null;
        try {
            ping = client.ping();
        } catch (SolrServerException | IOException | RuntimeException e) {
            throw new SophieException("Unable to connect to Solr", e);
        }
        if (ping != null && ping.getStatus() != 0) {
            throw new SophieException("Unable to connect to Solr");
        }
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        // Create radio buttons from the connection types enum.
        for (final SolrConnectionType type : SolrConnectionType.values()) {
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

        // Create label.
        label = new Label(composite, SWT.WRAP);
        label.setText(selectedType.getValueLabel());
        GridData grid = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        grid.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        label.setLayoutData(grid);
        label.setFont(parent.getFont());

        // Create combo. 
        value = new Combo(composite, SWT.SINGLE | SWT.BORDER);
        value.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        String[] favorites = Config.getFavorites();
        String[] items = ArrayUtils.isNotEmpty(favorites) ? favorites : new String[] { selectedType.getValueDefault() };
        value.setItems(items);
        value.setText(items[0]);

        applyDialogFont(composite);
        return composite;
    }

}
