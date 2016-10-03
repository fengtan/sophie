package com.github.fengtan.sophie.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.sophie.beans.SophieException;

public abstract class SortableTable {

	private Table table;
	private List<String> columnNames = new ArrayList<String>();
	private List<Map<String, String>> rowValues = new ArrayList<Map<String, String>>();
	
	public SortableTable(Composite parent, SelectionListener listener) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL;

		table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		if (listener != null) {
			table.addSelectionListener(listener);
		}
	}
	
	public SortableTable(Composite parent) {
		this(parent, null);
	}
	
	protected boolean hasColumn(String columnName) {
		return columnNames.contains(columnName);
	}
	
	protected TableColumn addColumn(String columnName) {
		columnNames.add(columnName);
		TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setText(columnName);
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				
			}
		});
		column.pack();// TODO needed ? might be worth to setLayout() to get rid of this
		return column;
	}
	
	/**
	 * 
	 * @param values Map<columnName, value>
	 */
	protected TableItem addRow(Map<String, String> values) {
		rowValues.add(values);
		TableItem item = new TableItem(table, SWT.NULL);
		for (Map.Entry<String, String> value:values.entrySet()) {
			item.setText(columnNames.indexOf(value.getKey()), value.getValue());
		}
		return item;
	}
	
	public void refresh() throws SophieException {
		table.removeAll();
		populate();
	}
	
	protected abstract void populate() throws SophieException;
	
	protected TableItem[] getTableSelection() {
		return table.getSelection();
	}
	
}
