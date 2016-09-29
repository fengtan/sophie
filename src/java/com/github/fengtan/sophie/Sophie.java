package com.github.fengtan.sophie;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.sophie.dialogs.ConnectDialog;
import com.github.fengtan.sophie.tabs.TabFolder;

public class Sophie {
	
	public static SolrClient client;
	
	// TODO load url from .properties
	public static void main(String[] args) {
		// Create shell.
		Shell shell = new Shell();
		shell.setMaximized(true);
		shell.setLayout(new GridLayout());
		
		ConnectDialog dialog = new ConnectDialog(shell);
		dialog.open();
		if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
			return;
		}
		client = dialog.getSolrClient();
		String connectionLabel = dialog.getConnectionLabel();
		
		// Initialize Solr client and UI.
		shell.setText("Sophie - "+connectionLabel);
		new TabFolder(shell, connectionLabel);
		// TODO what if server works and then goes down
		
		// Make the shell display its content.
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();	
				}	
			} catch (Exception e) {
				showException(shell, e);
			}
		}
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		display.dispose();
		shell.dispose();
	}
	
	public static void showException(Shell shell, Exception e) {
		// TODO use ErrorDialog instead of MessageBox - provides button "see details" to display exception stack trace
    	e.printStackTrace(); // TODO log stack trace slf4j
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
	    box.setText("An error happened");
	    box.setMessage(e.getMessage());
	    box.open();
	}
	
	// TODO test exotic Solr versions
	// TODO test elasticsearch
	// TODO test modifying 2 documents and then commiting
	
	// TODO meta screenshot 0.5
	// TODO meta about page + github.io
	// TODO meta document all methods
	// TODO meta measure memory footprint
	// TODO meta license (depends on swt, solrj, icons, other dependencies)
	// TODO meta wording server/index/collection/core
	// TODO meta travis
    // TODO meta .deb package
    // TODO - install jar into /opt or /usr/local
    // TODO - export SOPHIE_HOME=/usr/local/sophie-x.y.z
    // TODO - export PATH=$PATH:$SOPHIE_HOME/bin
    // TODO - expose log4j.prop in /etc
    // TODO - logs in /var/log
	// TODO meta github sophie
	// TODO meta contribute convenience methods for replication handler (backup/restore/polling) https://issues.apache.org/jira/browse/SOLR-5640
	// TODO meta gif (licecap/silentcast/byzanz)
	// TODO meta how to delete favorites (edit ~/.sophie)
	
	// TODO feat sort by field name (fields+documents)
	// TODO feat all constants overridable using .properties file or -Dpage.size=20 + provide a default .properties
	// TODO feat allow to create documents with new fields
	// TODO feat show "(empty)" at the top
	// TODO feat what if field contains value "(empty)" ?
	// TODO feat fire filter only when hit enter
	// TODO feat allow to revert a specific document
	// TODO feat allow not to use the default request handler
	// TODO feat selecting filter "foo (1)" generates "foo (1)" in textfield
	// TODO feat allow to select multiple values for each filter
	// TODO feat support empty facets on free text fields (workaround: add "(empty) (1)" in free text 
	// TODO feat allow to reload config on all cores
    // TODO feat see what luke and solr native ui provide (dismax, spellcheck, debug, score, shard, elevation etc)
	// TODO feat CoreAdmin split/mergeindexes https://wiki.apache.org/solr/CoreAdmin, CollectionAdminRequest, replicate to slave / pull from master
	// TODO feat "favorites/recently opened servers"
    // TODO feat CoreAddDialog/CoreSwapDialog -> re-use SelectionDialog/ListSelectionDialog/ListDialog/ElementListSelectionDialog
	// TODO feat Dialogs -> use validators to make sure values are not empty ?
	
	// TODO clean look for unused/obsolete methods
	// TODO clean retest everything
	// TODO clean are all jars required ?
	
	// TODO doc cannot filter on unindexed fields
	// TODO doc if value is a date then calendar shows up when editing
	// TODO doc assume luke handler + select + admin/cores is available
    // TODO doc https://issues.apache.org/jira/browse/SOLR-20
    // TODO doc log4j.prop
	// TODO doc "(not stored)"
	// TODO doc unsortable fields
	// TODO doc sort by clicking on header
	// TODO doc backup stored on *server*
	// TODO dov mvn
	// TODO doc import into eclipse
	// TODO doc typing 'Suppr' deletes a row.
	// TODO doc "virtual i.e. remote documents are fetched as they are displayed for best perf"
	// TODO doc luke + javasoze/clue + solarium + projectblacklight.org
    // TODO doc publish javadoc
	// TODO doc "if need to use http auth, then http://user:pass@localhost:8983/solr/collection1"
	// TODO doc "can configure DEFAULT_DOCUMENTS_PAGE_SIZE + DEFAULT_DOCUMENTS_FACETS_LIMIT" (need to update .sophie manually)
	
	// TODO obs trayitem - not supported by ubuntu https://bugs.eclipse.org/bugs/show_bug.cgi?id=410217
}
