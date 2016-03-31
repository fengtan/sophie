package com.github.fengtan.solrgui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.solrgui.tabs.SolrGUITabFolder;

public class SolrGUI {
	
	public static void main(String[] args) {
		new SolrGUI().run();
	}

	public void run() {
 		// Set layout for shell.
		Shell shell = new Shell();
		shell.setLayout(new GridLayout()); // TODO needed ?
		
		// Add tab folder.
		new SolrGUITabFolder(shell);
				
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
		display.dispose();
		shell.dispose();
	}
	
	// TODO status line showing last solr query
	// TODO keyboard shortcuts
	// TODO test Solr 5
	// TODO test Solr < 4
	// TODO persist rows, fq, q etc ?
	// TODO dismax, facets, debug etc
	// TODO elasticsearch
	// TODO about page + github.io
	// TODO see what features luke provides
	// TODO document all methods
	// TODO README mvn clean install; java -jar target/solr-gui-{version}-SNAPSHOT-jar-with-dependencies.jar
	// TODO drop .settings, .classpath etc
	// TODO README showing/hiding a column will refresh the table (and wipe out local modifications)
	// TODO rename solrgui
    // TODO allow to create documents with new fields
	// TODO measure memory footprint
	// TODO see luke ui
	// TODO measure table items http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html
	// TODO allow to export in xls (may require org.eclipse.nebula.widgets.nattable.extension.poi*.jar)
	// TODO document in readme: typing 'Suppr' deletes a row.
	// TODO does sorting scale ?
	// TODO sort using setSortDirection ?
	// TODO allow to clone a document
	// TODO graceful degradation if Luke handlers not provided by server
	// TODO are all jars required ?
	// TODO allow to revert a specific document
	// TODO what if Luke handler is not available
	// TODO allow not to use the default request handler
	// TODO status line
	// TODO generate name from URL ? painful to decide a name
	// TODO glazedlists ?
	
	
}
