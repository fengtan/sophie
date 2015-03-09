package com.github.fengtan.solrgui;

import java.net.MalformedURLException;
import java.net.URL;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.beans.SolrGUIConfig;
import com.github.fengtan.solrgui.beans.SolrGUIServer;
import com.github.fengtan.solrgui.viewers.SolrGUITab;

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
		Composite composite = createComposite(shell);
		createTabFolder(composite, shell);
		createToolbar(composite);

		// Make the shell to display its content.
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		shell.dispose();
	}
	
	private Composite createComposite(Shell shell) {
		shell.setLayout(new GridLayout(1, true));
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new RowLayout());
		return composite;
	}
	
	private void createTabFolder(Composite composite, final Shell shell) {
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
		
		// Initialize tabs from config file.
		for (SolrGUIServer server: SolrGUIConfig.getServers()) {
			new SolrGUITab(tabFolder, server);
		}
		
	}
	
	private void createToolbar(Composite composite) {
		// 'Add server' UI.
    	// TODO validate connection before saving
    	// TODO onfocus, drop default values
        // TODO size of text fields
		// TODO possibility to reduce/expand toolbar
        new Label(composite, SWT.NULL).setText("Name");
        final Text name = new Text(composite, SWT.BORDER); // TODO border
        name.setText("collection1@localhost");
        new Label(composite, SWT.NULL).setText("URL");
        final Text url = new Text(composite, SWT.BORDER); // TODO border
        url.setText("http://localhost:8983/solr/collection1");
        
		Button button = new Button(composite, SWT.PUSH);
		button.setText("Add Server");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// TODO do not create if server already exists.
				try {
					SolrGUIServer server = new SolrGUIServer(new URL(url.getText()), name.getText());
	                SolrGUIConfig.addServer(server);
	                new SolrGUITab(tabFolder, server);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/* TODO
				getControl().addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						solrGUI.dispose();
					}
				});
				*/
		    }
		});

	}

}