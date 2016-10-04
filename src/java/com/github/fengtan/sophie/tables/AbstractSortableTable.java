/**
 * Sophie - A Solr browser and administration tool
 * Copyright (C) 2016 fengtan<https://github.com/fengtan>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.fengtan.sophie.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.sophie.Sophie;
import com.github.fengtan.sophie.beans.SophieException;

public abstract class AbstractSortableTable {

	private Table table;
	private List<String> columnNames = new ArrayList<String>();
	private List<Map<String, String>> rowValues = new ArrayList<Map<String, String>>();
	
	private boolean sortAsc = true;
	private String sortColumnName;
	
	public AbstractSortableTable(Composite parent, SelectionListener listener) {
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
	
	public AbstractSortableTable(Composite parent) {
		this(parent, null);
	}
	
	protected boolean hasColumn(String columnName) {
		return columnNames.contains(columnName);
	}
	
	protected TableColumn addColumn(final String columnName) {
		columnNames.add(columnName);
		final TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setData("columnName", columnName);
		column.setText(columnName+"     ");
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// Clicking on the current sort field toggles the direction.
				// Clicking on a new field changes the sort field.
				if (StringUtils.equals(sortColumnName, columnName)) {
					sortAsc = !sortAsc;
				} else {
					sortColumnName = columnName;
				}
				Comparator<Map<String, String>> comparator = new Comparator<Map<String, String>>() {
					@Override
					public int compare(Map<String, String> rowValues1, Map<String, String> rowValues2) {
						String value1 = rowValues1.get(columnName);
						String value2 = rowValues2.get(columnName);
						if (sortAsc) {
							return (value1 == null) ? -1 : value1.compareTo(value2);	
						} else {
							return (value2 == null) ? -1 : value2.compareTo(value1);
						}
					}
				};
				Collections.sort(rowValues, comparator);	
				table.removeAll();
				for (Map<String, String> values:rowValues) {
					createRow(values);
				}
				setSortSignifier();
			}
		});
		column.pack();// TODO needed ? might be worth to setLayout() to get rid of this
		return column;
	}
	
	/**
	 * Set sort signifier on sorted column. 
	 */
	private void setSortSignifier() {
		for (TableColumn column:table.getColumns()) {
			String name = (String) column.getData("columnName");
			char signifier = sortAsc ? Sophie.SIGNIFIER_SORTED_ASC : Sophie.SIGNIFIER_SORTED_DESC;
			column.setText(name+(StringUtils.equals(sortColumnName, name) ? " "+signifier : StringUtils.EMPTY));
		}
	}
	
	/**
	 * 
	 * @param values Map<columnName, value>
	 */
	protected TableItem addRow(Map<String, String> values) {
		rowValues.add(values);
		return createRow(values);
	}
	
	private TableItem createRow(Map<String, String> values) {
		TableItem item = new TableItem(table, SWT.NULL);
		for (Map.Entry<String, String> value:values.entrySet()) {
			item.setText(columnNames.indexOf(value.getKey()), value.getValue());
		}
		return item;
	}
	
	public void refresh() throws SophieException {
		rowValues = new ArrayList<Map<String, String>>();
		sortAsc = true;
		sortColumnName = null;
		setSortSignifier();
		table.removeAll();
		populate();
	}
	
	protected abstract void populate() throws SophieException;
	
	protected TableItem[] getTableSelection() {
		return table.getSelection();
	}
	
}
