package com.github.fengtan.solrgui;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.solrgui.tabs.SolrGUITabFolder;
import com.github.fengtan.solrgui.toolbar.SolrGUIToolbar;

public class SolrGUI {
	
	public static SolrGUIToolbar toolbar; // TODO put toolbar inside tabFolder ?
	public static SolrGUITabFolder tabFolder;
	public static SolrClient client; // TODO move into SolrGUITable ? so we do not store both this.server and this.url

	public static void main(String[] args) { // TODO convert into static { code } ?
		String url = "http://localhost:8983/solr/collection1"; // TODO get from args[0] or prompt when launch or from .ini/.properties
		
		// Connect to Solr.
		try {
			client = new HttpSolrClient(url);
			client.ping(); // TODO check that response is "OK" ?
		} catch (Throwable t) {
			// TODO add button "retry"
			// TODO what if server works and then goes down
			t.printStackTrace();
			// new Label(shell, SWT.NULL).setText(t.getMessage()); // TODO may require shell.open() to fire before
		}
		
		// Create GUI.
		Shell shell = new Shell();
		shell.setMaximized(true);
		shell.setText("Solr GUI - "+url);

		// Set layout for shell.
		GridLayout layout = new GridLayout();
		shell.setLayout(layout);

		// Add toolbar + tab folder.
		toolbar = new SolrGUIToolbar(shell);
		tabFolder = new SolrGUITabFolder(shell);
		
		// Make the shell to display its content.
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();	
				}	
			} catch (RuntimeException e) { // TODO is this the right way to handle runtime exceptions ?
		    	e.printStackTrace(); // TODO log stack trace slf4j
				MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
			    box.setText("An error happened");
			    box.setMessage(e.getMessage());
			    box.open();
			}
		}
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		toolbar.finalize();
		display.dispose();
		shell.dispose();
	}

	// TODO test exotic Solr versions
	// TODO test elasticsearch
	// TODO test modifying 2 documents and then commiting
	
	// TODO meta screenshot 0.4
	// TODO meta about page + github.io
	// TODO meta document all methods
	// TODO meta rename solrgui
	// TODO meta measure memory footprint
	// TODO meta are all jars required ?
	// TODO meta license (depends on swt, solrj, icons, other dependencies)
	// TODO meta wording server/index/collection/core
	// TODO meta travis
    // TODO meta .deb package
    // TODO - install jar into /opt or /usr/local
    // TODO - export SOLRGUI_HOME=/usr/local/solr-gui-x.y.z
    // TODO - export PATH=$PATH:$SOLRGUI_HOME/bin
    // TODO - expose log4j.prop in /etc
    // TODO - logs in /var/log
	// TODO meta contribute convenience methods for replication handler (backup/restore/polling) https://issues.apache.org/jira/browse/SOLR-5640
	
	// TODO feat all constants overridable using .properties file or -Dpage.size=20 + provide a default .properties
	// TODO feat tab "documents", "stats", "fields"
	// TODO feat opening new tab triggers repetitive requests
	// TODO feat allow to create documents with new fields
	// TODO feat show "(empty)" at the top
	// TODO feat fire filter only when hit enter
	// TODO feat allow to revert a specific document
	// TODO feat allow not to use the default request handler
	// TODO feat selecting filter "foo (1)" genates "foo (1)" in textfield
	// TODO feat dismax, spellcheck, debug, score, shard, elevation etc
	// TODO feat support HttpSolrServer/CloudSolrServer
	// TODO feat adapt edit dialog to support multi fields
	// TODO feat add calendar to edit dialog / filters
	// TODO feat allow to select multiple values for each filter
	// TODO feat opening new server makes virtual handler display all rows
	// TODO feat support empty facets on free text fields (workaround: add "(empty) (1)" in free text 
	// TODO feat what if field contains value "(empty)" ?
	// TODO feat button reload config, CoreAdminRequest, CollectionAdminRequest, replicate to slave / pull from master, etc see admin handler, crud cores (if multicore turned on)
	// TODO feat allow to reload config on all cores
	// TODO feat export/import documents
    // TODO feat see what luke and solr native ui provide
	// TODO feat look for unused/obsolete methods
	// TODO feat drop SolrGUI form class names
	
	// TODO doc cannot filter on unindexed fields
	// TODO doc assume luke handler + select is available
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

	// TODO obs trayitem - not supported by ubuntu https://bugs.eclipse.org/bugs/show_bug.cgi?id=410217
}
