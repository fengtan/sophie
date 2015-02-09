package com.github.fengtan.solrgui.ui;

import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.solr.Server;

public class ServerTab {

    private SolrDocumentList docs;
    private ColumnList columns = new ColumnList();
    private Table table;
    private TabItem tabItem;
    
    public ServerTab(Server server, TabFolder tabFolder) {
    	docs = server.getAllDocuments();
    	
	    for (SolrDocument document:docs) {
	    	for (String title:document.keySet()) {
	    		if (!columns.contains(title)) {
	    			columns.add(title);
	    		}
	    	}
	    }
	    
	    tabItem = new TabItem(tabFolder, SWT.NULL);
	    tabItem.setText(server.getName());
    }
    
	public void updateTable(Composite parent) {
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
	    
	    tabItem.setControl(table);
	    table.setSize(table.computeSize(SWT.DEFAULT, 200)); // TODO
	    
	    
	    
        final TableEditor editor = new TableEditor(table);
        //The editor must have the same size as the cell and must
        //not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        final int EDITABLECOLUMN = 1;
        table.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	// Clean up any previous editor control
                Control oldEditor = editor.getEditor();
                if (oldEditor != null) oldEditor.dispose();
    
                // Identify the selected row
                TableItem item = (TableItem)e.item;
                if (item == null) return;
    
                // The control that will be the editor must be a child of the Table
                Text newEditor = new Text(table, SWT.NONE);
                newEditor.setText(item.getText(EDITABLECOLUMN));
                newEditor.addModifyListener(new ModifyListener() {
                	public void modifyText(ModifyEvent e) {
                		Text text = (Text)editor.getEditor();
                        editor.getItem().setText(EDITABLECOLUMN, text.getText());
                    }
                });
                newEditor.selectAll();
                newEditor.setFocus();
                editor.setEditor(newEditor, item, EDITABLECOLUMN);
            }
        });
	    
	}

	
	public void updateMenu(Menu menu, final Shell shell) { // TODO drop shell argument ?
        // File menu.
        MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
        fileMenuItem.setText("&File");
        
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuItem.setMenu(fileMenu);

        MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
        newItem.setText("Add &server");
        newItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new NewServerDialog(shell).open();
              }
		});
        
        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("&Exit");

        exitItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
        
        // Columns menu.
        MenuItem columnsMenuItem = new MenuItem(menu, SWT.CASCADE);
        columnsMenuItem.setText("&Columns");

        Menu columnsMenu = new Menu(shell, SWT.DROP_DOWN);
        columnsMenuItem.setMenu(columnsMenu);

        for (String title:columns) {
            MenuItem columnItem = new MenuItem(columnsMenu, SWT.CHECK);
            columnItem.setText(title);
            columnItem.setSelection(true);
            shell.setMenuBar(menu);            
            columnItem.addSelectionListener(new ColumnAdapter(table, title, columns));
        }
        
	}
}
