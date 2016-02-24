package com.github.fengtan.solrgui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

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
	
	// TODO right click 'Show Solr query'
	// TODO catch all exception
	// TODO keyboard shortcuts
	// TODO test Solr 5
	// TODO elasticsearch
	// TODO about page + github.io
	// TODO see what features luke provides
	// TODO ability to show/hide specific columns
	// TODO document all methods

}
