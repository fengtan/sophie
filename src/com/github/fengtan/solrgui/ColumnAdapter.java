package com.github.fengtan.solrgui;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;

// TODO too much coupled with SolrGUIDisplay.
public class ColumnAdapter extends SelectionAdapter {

	private Table table;
	private int index;
	private SolrGUIColumn column;
	private SolrGUIDisplay display;
	
	public ColumnAdapter(Table table, int index, SolrGUIColumn column, SolrGUIDisplay display) {
		this.table = table;
		this.index = index;
		this.column = column;
		this.display = display;
	}
	
    @Override
    public void widgetSelected(SelectionEvent e) {
    	// Toggle column.
    	if (column.isDisplayed()) {
    		table.getColumn(index).dispose();
    		column.setDisplayed(false);
    	} else {
    		column.setDisplayed(true);
    	}
    	display.updateTable();
    }
	
}
