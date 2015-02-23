package com.github.fengtan.solrgui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SolrGUI {

	private CTabFolder tabFolder;

	public static void main(String[] args) {
		new SolrGUI().run();
	}
	
	public void run() {
		Shell shell = new Shell();
		shell.setText("Solr GUI");

		// Set layout for shell.
		GridLayout layout = new GridLayout();
		shell.setLayout(layout);

		// Fill up shell.
		createContents(shell);

		// Make the shell to display its content.
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	// TODO separate Table & Tab in 2 classes
	
	private void createContents(Shell shell) {
		shell.setLayout(new GridLayout(1, true));

		// Create the buttons to create tabs
		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new RowLayout());

		// Add server button
		Button button = new Button(composite, SWT.PUSH);
		button.setText("Add Server");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				new SolrGUITab(tabFolder, SolrGUIConfig.getServers().get(0)); // TODO loop over servers.
				// TODO set focus on new tab
				/* TODO
				getControl().addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						solrGUI.dispose();
					}
				});
				*/
		    }
		});

		// Create the tabs
		tabFolder = new CTabFolder(shell, SWT.TOP | SWT.CLOSE | SWT.BORDER);
		tabFolder.setBorderVisible(true);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setSimple(false);
		tabFolder.setTabHeight(25);

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
	}

	/*
	 * Close the window and dispose of resources
	 */
	public void close() {
		Shell shell = tabFolder.getShell();
		if (shell != null && !shell.isDisposed()) {
			shell.dispose();	
		}
	}

}