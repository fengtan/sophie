package com.github.fengtan.solrgui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

// TODO too much coupled with SolrGUIDisplay.
public class ColumnAdapter extends SelectionAdapter {

	private Table table;
	private String title;
	private SolrGUIColumnList columns;
	
	public ColumnAdapter(Table table, String title, SolrGUIColumnList columns) {
		this.table = table;
		this.title = title;
		this.columns = columns;
	}
	
    @Override
    public void widgetSelected(SelectionEvent e) {
    	if (columns.isItemDisplayed(title)) {
    		// Drop column.
    		int index = columns.getIndexDisplayed(title);
    		table.getColumn(index).dispose();
    		columns.setItemDisplayed(title, false);
    	} else {
    		// Add column.
    		columns.setItemDisplayed(title, true);
    		int index = columns.getIndexDisplayed(title);
			TableColumn tableColumn = new TableColumn(table, SWT.NONE, index);
		    tableColumn.setText(title);
    	    for (int i = 0; i < table.getColumnCount(); i++) {
    	    	table.getColumn(i).pack();
    		}
    	}
    }
	
}
