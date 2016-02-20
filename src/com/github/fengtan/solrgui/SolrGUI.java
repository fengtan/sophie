package com.github.fengtan.solrgui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.solrgui.beans.SolrGUIConfig;
import com.github.fengtan.solrgui.beans.SolrGUIServer;
import com.github.fengtan.solrgui.dialogs.SolrGUIAddServerDialog;
import com.github.fengtan.solrgui.tabs.SolrGUITab;
import com.github.fengtan.solrgui.toolbar.SolrGUIToolbar;

public class SolrGUI {
	
	private CTabFolder tabFolder;

	public static void main(String[] args) {
		new SolrGUI().run();
	}
	
	public void run() {
		Shell shell = new Shell();
		shell.setText("Solr GUI");

		// Add toolbar
		SolrGUIToolbar toolbar = new SolrGUIToolbar(shell);
		
		// Set layout for shell.
		GridLayout layout = new GridLayout();
		shell.setLayout(layout);

		// Add tab folder.
		createTabFolder(shell);
		
		// Make the shell to display its content.
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		toolbar.finalize();
		display.dispose();
		shell.dispose();
	}

	// TODO move into separate class ?
	private void createTabFolder(final Shell shell) {
		// Create the tabs
		tabFolder = new CTabFolder(shell, SWT.TOP | SWT.CLOSE | SWT.BORDER);
		tabFolder.setBorderVisible(true);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setSimple(false);
		tabFolder.setTabHeight(25);

		// Create the 'Add server button
		// TODO button 'clear/empty index'
    	// TODO validate connection before saving
		// TODO what if user enters garbage (e.g. not a number)
		Button button = new Button(tabFolder, SWT.PUSH | SWT.CENTER);
		button.setText("Add &new server");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				new SolrGUIAddServerDialog(shell, tabFolder).open();
			}
	    });
		tabFolder.setTopRight(button);

		// Set up a gradient background for the selected tab
		Display display = shell.getDisplay();
		Color titleForeColor = display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
		Color titleBackColor1 = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
		Color titleBackColor2 = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
		tabFolder.setSelectionForeground(titleForeColor);
		tabFolder.setSelectionBackground(
		    new Color[] {titleBackColor1, titleBackColor2},
			new int[] {100},
			true
		);
		
		// Initialize tabs from config file.
		for (SolrGUIServer server: SolrGUIConfig.getServers()) {
			new SolrGUITab(tabFolder, server);
		}
		
	}
	
	// TODO right click 'Show Solr query'
	// TODO catch all exception
	// TODO keyboard shortcuts
	// TODO test Solr 5
	// TODO elasticsearch

}
