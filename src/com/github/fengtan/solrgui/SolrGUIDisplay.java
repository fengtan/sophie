package com.github.fengtan.solrgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class SolrGUIDisplay {
	
	private SolrDocumentList docs;
	private List<String> titles = new ArrayList<String>(); // TODO hashed ? use of indexOf
	private Table table;
	private Shell shell;
	
	public SolrGUIDisplay(SolrDocumentList docs) {
	    this.docs = docs;
	    
	    for (SolrDocument document:docs) {
	    	for (String title:document.keySet()) {
	    		if (!titles.contains(title)) {
	    			titles.add(title);
	    		}
	    	}
	    }
		
	    Display display = new Display();
	    shell = new Shell(display);
	    	    
	    setTable();
	    setMenus();
                
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
		
	private void setTable() {
	    table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION); // TODO
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);
	    
		for (String title:titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
	      	column.setText(title);
		}

		for(SolrDocument doc:docs) {
	    	TableItem item = new TableItem(table, SWT.NONE);
	    	for (Map.Entry<String, Object> field:doc.entrySet()) {
	    		item.setText(titles.indexOf(field.getKey()), field.getValue().toString());	
	    	}
		}

	    for (int i = 0; i < titles.size(); i++) {
	      table.getColumn(i).pack();
	    }
	    
	    table.setSize(table.computeSize(SWT.DEFAULT, 200)); // TODO
	}
	
	private void setMenus() {
        Menu menuBar = new Menu(shell, SWT.BAR);
        
        // File menu.
        MenuItem fileMenuItem = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuItem.setText("&File");
        
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuItem.setMenu(fileMenu);

        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("&Exit");
        shell.setMenuBar(menuBar);

        exitItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
        
        // Columns menu.
        MenuItem columnsMenuItem = new MenuItem(menuBar, SWT.CASCADE);
        columnsMenuItem.setText("&Columns");
        
        Menu columnsMenu = new Menu(shell, SWT.DROP_DOWN);
        columnsMenuItem.setMenu(columnsMenu);

        for (String title:titles) {
            MenuItem columnItem = new MenuItem(columnsMenu, SWT.PUSH);
            columnItem.setText(title);
            shell.setMenuBar(menuBar);	
        }
        
	}
	
}
