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
     */
    public static void main() {
        // Create shell.
        Shell shell = new Shell();
        shell.setMaximized(true);
        shell.setLayout(new GridLayout());

        // Prompt user for Solr credentials.
        ConnectDialog dialog = new ConnectDialog(shell);
        dialog.open();
        if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
            return;
        }
        client = dialog.getSolrClient();
        String connectionLabel = dialog.getConnectionLabel();

        // Initialize Solr client and UI.
        shell.setText("Sophie - " + connectionLabel);
        new TabFolder(shell, connectionLabel);

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

    // TODO test exotic Solr versions
    // TODO test modifying 2 documents and then commiting
    // TODO test what if server works and then goes down
    // TODO test retest everything

    // TODO meta document all methods
    
    // TODO contrib solrutils.getRemoteSchemaFields() -> LukeResponse.getDynamicFieldInfo() - similar to LukeResponse.getFieldInfo() - see LukeResponse.setResponse()
    // TODO contrib replication handler solrj
    
    // TODO doc screenshot 0.5 + move screenshots/ to gh-pages
    // TODO doc gh-pages
    // TODO doc measure memory footprint
    // TODO doc gif (licecap/silentcast/byzanz)
    // TODO doc how to delete favorites (edit ~/.sophie)
    // TODO doc cannot filter on unindexed fields
    // TODO doc if value is a date then calendar shows up when editing
    // TODO doc assume luke handler + select + admin/cores is available
    // TODO doc https://issues.apache.org/jira/browse/SOLR-20
    // TODO doc log4j.prop can set level to info not to trace http calls
    // TODO doc "(not stored)"
    // TODO doc unsortable fields
    // TODO doc sort by clicking on header
    // TODO doc backup stored on *server*
    // TODO doc mvn clean install
    // TODO doc javadoc gh-pages
    // TODO doc import into eclipse
    // TODO doc typing 'Suppr' deletes a row.
    // TODO doc
    // "virtual i.e. remote documents are fetched as they are displayed for best perf"
    // TODO doc similar luke + javasoze/clue + solarium + projectblacklight.org
    // TODO doc publish javadoc (mvn generate:doc)
    // TODO doc
    // "if need to use http auth, then http://user:pass@localhost:8983/solr/collection1"
    // TODO doc
    // "can configure DEFAULT_DOCUMENTS_PAGE_SIZE + DEFAULT_DOCUMENTS_FACETS_LIMIT"
    // (need to update .sophie manually)
    // TODO doc support zk/cloud
    // TODO doc mvn -P win32/macosx/linux (latter by default)
    // TODO doc filters facet values show up only if < FACETS_LIMIT
    // TODO doc admin/luke, admin/ping, admin/cores must be available
    // TODO doc datepicker if field type contains "date"
    // TODO doc gh-pages slogan consistant

    // TODO obs trayitem - not supported by ubuntu
    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=410217
    // TODO obs allow to select multiple values for each filter
    // TODO obs what if field contains value "(empty)" ?
    // TODO obs sort by field name (fields+documents)
    // TODO obs .deb package
    // TODO - install jar into /opt or /usr/local
    // TODO - export SOPHIE_HOME=/usr/local/sophie-x.y.z
    // TODO - export PATH=$PATH:$SOPHIE_HOME/bin
    // TODO - expose log4j.prop in /etc ; slf4j
    // TODO - logs in /var/log
    // TODO obs Dialogs -> use validators to make sure values are not empty
    // TODO obs allow to revert a specific document
    // TODO obs see what luke and solr native ui provide (replication, load term
    // info, analysis, DIH, files, plugins, logs, dismax, spellcheck, debug,
    // score, shard, elevation etc)/ pull from master
    // TODO obs CoreAdmin split/mergeindexes
    // https://wiki.apache.org/solr/CoreAdmin, CollectionAdminRequest, replicate
    // to slave
    // TODO obs allow not to use the default request handler (select,
    // admin/ping, admin/cores, admin/luke)
    // TODO obs hitting "suppr" or clicking the button (in the toolbar) a second
    // time should remove the deletion.
    // TODO obs schema API (e.g. add/delete fields) + SchemaRequest (provides
    // list of copyField's etc)
    // TODO obs support sort on multiple fields
}
