package com.github.fengtan.solrgui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewServerDialog {

	private Shell dialog;
	
	public NewServerDialog(Shell parent) {
		dialog = new Shell(parent);
        dialog.setText("Add server");
        dialog.setSize(500, 400);

    	// TODO validate connection before saving
    	// TODO port should be a number
    	// TODO choose protocol from drop down list
    	// TODO onfocus, drop default values
        // TODO size of text fields
        
        dialog.setLayout(new GridLayout(2, false));

        new Label(dialog, SWT.NULL).setText("Name");
        new Text(dialog, SWT.BORDER).setText("My Solr Server");
        
        new Label(dialog, SWT.NULL).setText("Protocol");
        new Text(dialog, SWT.BORDER).setText("http");

        new Label(dialog, SWT.NULL).setText("Host");
        new Text(dialog, SWT.BORDER).setText("localhost");
        
        new Label(dialog, SWT.NULL).setText("Port");
        new Text(dialog, SWT.BORDER).setText("8983");
        
        new Label(dialog, SWT.NULL).setText("Path");
        new Text(dialog, SWT.BORDER).setText("/solr/collection1");
        
        Button button = new Button(dialog, SWT.PUSH);
        button.setText("Add server");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("Called!");
                
            }
		});
        
        dialog.pack();
	}
	
	public void open() {
		dialog.open();
	}
	
}
