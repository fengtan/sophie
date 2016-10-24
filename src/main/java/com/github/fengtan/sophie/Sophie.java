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
package com.github.fengtan.sophie;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.sophie.beans.Config;
import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.dialogs.ConnectDialog;
import com.github.fengtan.sophie.dialogs.ExceptionDialog;
import com.github.fengtan.sophie.tabs.TabFolder;

/**
 * Application launcher.
 */
public class Sophie {

    /**
     * Character for columns sorted in ascending order.
     */
    public static final char SIGNIFIER_SORTED_ASC = '\u25B4';

    /**
     * Character for columns sorted in descending order.
     */
    public static final char SIGNIFIER_SORTED_DESC = '\u25BE';

    /**
     * Character for unsortable columns.
     */
    public static final char SIGNIFIER_UNSORTABLE = '\u2205';

    /**
     * Unique Solr client used by all classes.
     */
    public static SolrClient client;

    /**
     * Unique logger used by all classes.
     */
    public static Log log = LogFactory.getLog(Sophie.class);

    /**
     * Prompt user for Solr connection string and instantiate Solr client / SWT
     * display accordingly.
     * 
     * @param args
     *            Arguments.
     */
    public static void main(String[] args) {
        // Create shell.
        Shell shell = new Shell();
        shell.setMaximized(true);
        shell.setLayout(new GridLayout());

        ConnectDialog dialog = new ConnectDialog(shell);
        boolean validClient = false;
        String connectionString;
        do {
            // Prompt user for Solr credentials.
            dialog.open();
            if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
                return;
            }

            // Initialize Solr client and UI.
            client = dialog.getSolrClient();
            connectionString = dialog.getValue();
            try {
                new TabFolder(shell, connectionString, dialog.getConnectionType());
                validClient = true;
            } catch (SophieException e) {
                ExceptionDialog.open(shell, e);
            }
        } while (!validClient);

        // If we have a valid client, then add it to the favorites.
        Config.addFavorite(connectionString);
        shell.setText("Sophie - " + connectionString + " (" + dialog.getConnectionType().getTypeName() + ")");

        // Make the shell display its content.
        shell.open();
        Display display = shell.getDisplay();
        while (!shell.isDisposed()) {
            try {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            } catch (Exception e) {
                ExceptionDialog.open(shell, e);
            }
        }

        // Free resources.
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                log.info("Unable to close Solr client", e);
            }
        }
        display.dispose();
        shell.dispose();
    }

}
