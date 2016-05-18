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
	
	// TODO test Solr 5
	// TODO test Solr < 4
	// TODO elasticsearch
	// TODO about page + github.io
	// TODO document all methods
	// TODO dov mvn
	// TODO doc import into eclipse
	// TODO rename solrgui
	// TODO allow to create documents with new fields
	// TODO measure memory footprint
	// TODO measure table items http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html
	// TODO doc typing 'Suppr' deletes a row.
	// TODO are all jars required ?
	// TODO allow to revert a specific document
	// TODO allow not to use the default request handler
	// TODO show last solr query
	// TODO allow to export in xls (may require org.eclipse.nebula.widgets.nattable.extension.poi*.jar)
	// TODO icon in ubuntu launcher
	// TODO license
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
    // TODO doc https://issues.apache.org/jira/browse/SOLR-20
    // TODO doc log4j.prop + ubuntu package should expose it in /etc
	// TODO doc "(not stored)"
	// TODO doc unsortable fields
	// TODO doc sort by clicking on header
	// TODO toolbar buttons commit/optimize
	// TODO selecting filter "foo (1)" genates "foo (1)" in textfield 
	// TODO support empty facet values
	// TODO doc add sop(rowIndex) in getDocument() seems to fix table fetching all rows
	// TODO confirm before commiting ?
	// TODO create button just to commit() ?
	
}
