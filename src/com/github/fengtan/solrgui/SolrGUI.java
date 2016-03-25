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
	
	public CTabFolder getTabFolder() { // TODO drop ?
		return tabFolder;
	}
	
	// TODO status line showing last solr query
	// TODO keyboard shortcuts
	// TODO test Solr 5
	// TODO elasticsearch
	// TODO about page + github.io
	// TODO see what features luke provides
	// TODO document all methods
	// TODO README mvn clean install; java -jar target/solr-gui-{version}-SNAPSHOT-jar-with-dependencies.jar
	// TODO drop .settings, .classpath etc
	// TODO README showing/hiding a column will refresh the table (and wipe out local modifications)
	// TODO rename solrgui
	
}
