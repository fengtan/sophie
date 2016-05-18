package com.github.fengtan.solrgui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.solrgui.tabs.ISolrGUITabFolderListener;
import com.github.fengtan.solrgui.tabs.SolrGUITabFolder;
import com.github.fengtan.solrgui.toolbar.SolrGUIToolbar;

public class SolrGUI {
	
	private SolrGUIToolbar toolbar;
	private SolrGUITabFolder tabFolder;

	public static void main(String[] args) {
		new SolrGUI().run();
	}

	public void run() {
		Shell shell = new Shell();
		shell.setText("Solr GUI");

		// Set layout for shell.
		GridLayout layout = new GridLayout();
		shell.setLayout(layout);
		
		// Add toolbar + tab folder.
		toolbar = new SolrGUIToolbar(shell, this); // TODO passing the SolrGUI object is ugly
		Set<ISolrGUITabFolderListener> listeners = new HashSet<ISolrGUITabFolderListener>();
		listeners.add(toolbar);
		tabFolder = new SolrGUITabFolder(shell, listeners); // TODO could refactor and not use a Set.
		
		// Make the shell to display its content.
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();	
				}	
			} catch (RuntimeException e) { // TODO is this the right way to handle runtime exceptions ?
		    	e.printStackTrace(); // TODO log stack trace somewhere
				MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
			    box.setText("An error happened");
			    box.setMessage(e.getMessage());
			    box.open();
			}
		}
		toolbar.finalize();
		display.dispose();
		shell.dispose();
	}
	
	public CTabFolder getTabFolder() { // TODO drop ? could use a static tabfolder
		return tabFolder;
	}
	
	// TODO keyboard shortcuts
	// TODO test Solr 5
	// TODO elasticsearch
	// TODO about page + github.io
	// TODO document all methods
	// TODO README mvn clean install; java -jar target/solr-gui-{version}-SNAPSHOT-jar-with-dependencies.jar
	// TODO doc import into eclipse
	// TODO README showing/hiding a column will refresh the table (and wipe out local modifications)
	// TODO rename solrgui
	// TODO test Solr < 4
	// TODO allow to create documents with new fields
	// TODO measure memory footprint
	// TODO see luke ui
	// TODO measure table items http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html
	// TODO allow to export in xls (may require org.eclipse.nebula.widgets.nattable.extension.poi*.jar)
	// TODO document in readme: typing 'Suppr' deletes a row.
	// TODO sort using setSortDirection ?
	// TODO allow to clone a document
	// TODO are all jars required ?
	// TODO allow to revert a specific document
	// TODO allow not to use the default request handler
	// TODO status line + possibly show last solr query
	// TODO measure table items http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html
	// TODO allow to export in xls (may require org.eclipse.nebula.widgets.nattable.extension.poi*.jar)
	// TODO icon in ubuntu launcher
	// TODO license
	// TODO sort by ID field by default ? so rows remain the same when modify one
	// TODO if server empty and click on table viewer -> seems to crash
	// TODO dismax, spellcheck, debug etc
	// TODO support HttpSolrServer/CloudSolrServer
	// TODO doc "virtual i.e. remote documents are fetched as they are displayed for best perf"
	// TODO adapt edit dialog to support multi fields
	// TODO add calendar to edit dialog / filters
	// TODO allow to select multiple values for each filter
	// TODO test modifying 2 documents and then commiting
	// TODO interface to crud cores (if multicores is turned on)
	// TODO doc cannot filter on unindexed fields
	// TODO travis
	// TODO doc assume luke handler + select is available
	// TODO control level of logging of solrj
	// TODO log solr requests
    // TODO doc https://issues.apache.org/jira/browse/SOLR-20
    // TODO doc log4j.prop + ubuntu package should expose it in /etc
	// TODO doc "(not stored)"
	
}
