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
	private List<SolrGUIColumn> columns = new ArrayList<SolrGUIColumn>();
	private Table table;
	private Shell shell;
	
	public SolrGUIDisplay(SolrDocumentList docs) {
	    this.docs = docs;
	    
	    for (SolrDocument document:docs) {
	    	for (String title:document.keySet()) {
	    		SolrGUIColumn column = new SolrGUIColumn(title); // TODO might be inefficient
	    		if (!columns.contains(column)) {
	    			columns.add(column);
	    		}
	    	}
	    }
		
	    Display display = new Display();
	    shell = new Shell(display);
	    	    
	    updateTable();
	    updateMenus();
                
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
		
	public void updateTable() { // TODO might be worth to move 'new Table()' in the constructor
	    table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION); // TODO
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);

		for (SolrGUIColumn column:columns) {
			if (column.isDisplayed()) {
				TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		      	tableColumn.setText(column.getTitle());	
			};
		}

		for(SolrDocument doc:docs) {
	    	TableItem item = new TableItem(table, SWT.NONE);
	    	for (Map.Entry<String, Object> field:doc.entrySet()) {
	    		int index = columns.indexOf(new SolrGUIColumn(field.getKey()));  // TODO not efficient
	    		if (columns.get(index).isDisplayed()) {
		    		item.setText(index, field.getValue().toString());	
	    		}
	    	}
		}

	    for (int i = 0; i < table.getColumnCount(); i++) {
	      table.getColumn(i).pack();
	    }
	    
	    table.setSize(table.computeSize(SWT.DEFAULT, 200)); // TODO
	}
	
	private void updateMenus() {
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

        for (SolrGUIColumn column:columns) {
            MenuItem columnItem = new MenuItem(columnsMenu, SWT.CHECK);
            columnItem.setText(column.getTitle());
            columnItem.setSelection(true);
            shell.setMenuBar(menuBar);
            
            int index = columns.indexOf(new SolrGUIColumn(column.getTitle()));  // TODO not efficient
            columnItem.addSelectionListener(new ColumnAdapter(table, index, column, this));
        }
        
	}
	
}
