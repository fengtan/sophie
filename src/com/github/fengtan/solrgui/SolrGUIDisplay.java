package com.github.fengtan.solrgui;

import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;


public class SolrGUIDisplay {
	
	private SolrDocumentList docs;
	private SolrGUIColumnList columns = new SolrGUIColumnList();
	private Table table;
	private Shell shell;
	
	public SolrGUIDisplay(String name, SolrDocumentList docs) {
	    this.docs = docs;
	    
	    for (SolrDocument document:docs) {
	    	for (String title:document.keySet()) {
	    		if (!columns.contains(title)) {
	    			columns.add(title);
	    		}
	    	}
	    }
		
	    Display display = new Display();
	    shell = new Shell(display);
	    	    
	    shell.setText("Solr GUI");
	    shell.setSize(450, 250);

	    final TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);

	    TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
	    tabItem.setText(name);

	    tabFolder.setSize(1200, 300); // TODO set max size of  window
	    
	    updateTable(tabFolder);
	    
	    tabFolder.getItem(0).setControl(table);
	    
	    updateMenus();
	    
	    
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
		
	public void updateTable(Composite parent) { // TODO might be worth to move 'new Table()' in the constructor
	    table = new Table(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION); // TODO
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);

		for (String title:columns.getItemsDisplayed()) {
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		    tableColumn.setText(title);
		}

		for(SolrDocument doc:docs) {
	    	TableItem item = new TableItem(table, SWT.NONE);
	    	for (Map.Entry<String, Object> field:doc.entrySet()) {
	    		if (columns.isItemDisplayed(field.getKey())) {
	    			item.setText(columns.indexOf(field.getKey()), field.getValue().toString());
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

        for (String title:columns) {
            MenuItem columnItem = new MenuItem(columnsMenu, SWT.CHECK);
            columnItem.setText(title);
            columnItem.setSelection(true);
            shell.setMenuBar(menuBar);            
            columnItem.addSelectionListener(new ColumnAdapter(table, title, columns));
        }
        
	}
	
}
