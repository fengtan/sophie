/**
 * Sophie - A Solr browser and administration tool
 * 
 * Copyright (C) 2016 fengtan<https://github.com/fengtan>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.sophie.Sophie;
import com.github.fengtan.sophie.beans.SophieException;

/**
 * Instantiate a non-virtual table and provide a sortable behavior. Subclasses
 * are expected to use the protected methods in this class to add columns/rows.
 */
public abstract class AbstractSortableTable {

    /**
     * Table.
     */
    private Table table;

    /**
     * List of column names.
     */
    private List<String> columnNames = new ArrayList<String>();

    /**
     * List of rows, each row being represented by a map of values keyed by
     * column name.
     */
    private List<Map<String, String>> rowValues = new ArrayList<Map<String, String>>();

    /**
     * Whether the table is currently sorted in ascending order or not.
     */
    private boolean sortAsc = true;

    /**
     * Name of the column currently sorted (null if the table is not sorted).
     */
    private String sortColumnName;

    /**
     * Instantiate a non-virtual table and provide a sortable behavior.
     * 
     * @param composite
     *            Parent composite.
     * @param listener
     *            Selection listener to attach to the table, or null.
     */
    public AbstractSortableTable(Composite composite, SelectionListener listener) {
        // Instantiate table.
        int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
        table = new Table(composite, style);

        // Set layout.
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.grabExcessVerticalSpace = true;
        table.setLayoutData(gridData);

        // Set style.
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        // Attach selection listener, if one was provided.
        if (listener != null) {
            table.addSelectionListener(listener);
        }
    }

    /**
     * Instantiate a non-virtual table and provide a sortable behavior.
     * 
     * @param composite
     *            Parent composite.
     */
    public AbstractSortableTable(Composite composite) {
        this(composite, null);
    }

    /**
     * Whether the table already contains a column with a specific name.
     * 
     * @param columnName
     *            Column name.
     * @return True if the table already contains a column with this name, false
     *         otherwise.
     */
    protected boolean hasColumn(String columnName) {
        return columnNames.contains(columnName);
    }

    /**
     * Add a new column to the table.
     * 
     * @param columnName
     *            Column name.
     * @return The newly created column.
     */
    protected TableColumn addColumn(final String columnName) {
        // Add column name to our internal list.
        columnNames.add(columnName);

        // Instantiate column.
        final TableColumn column = new TableColumn(table, SWT.LEFT);
        column.setData("columnName", columnName);
        column.setText(columnName + "     ");
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
                        if (value1 == null) {
                            value1 = StringUtils.EMPTY;
                        }
                        if (value2 == null) {
                            value2 = StringUtils.EMPTY;
                        }
                        if (sortAsc) {
                            return value1.compareTo(value2);
                        } else {
                            return value2.compareTo(value1);
                        }
                    }
                };
                Collections.sort(rowValues, comparator);
                table.removeAll();
                for (Map<String, String> values : rowValues) {
                    createRow(values);
                }
                setSortSignifier();
            }
        });

        // Pack.
        column.pack();
        return column;
    }

    /**
     * Set sort signifier on the sorted column. Clear sort signifier on the
     * other columns.
     */
    private void setSortSignifier() {
        for (TableColumn column : table.getColumns()) {
            String name = (String) column.getData("columnName");
            char signifier = sortAsc ? Sophie.SIGNIFIER_SORTED_ASC : Sophie.SIGNIFIER_SORTED_DESC;
            column.setText(name + (StringUtils.equals(sortColumnName, name) ? " " + signifier : StringUtils.EMPTY));
        }
    }

    /**
     * Add a new row to the table.
     * 
     * @param values
     *            Row values keyed by column name.
     * @return The newly created row.
     */
    protected TableItem addRow(Map<String, String> values) {
        // Add row to our internal list.
        rowValues.add(values);

        // Instantiate row.
        return createRow(values);
    }

    /**
     * Instantiate a new row.
     * 
     * @param values
     *            Row values keyed by column name.
     * @return The newly created row.
     */
    private TableItem createRow(Map<String, String> values) {
        TableItem item = new TableItem(table, SWT.NULL);
        for (Map.Entry<String, String> value : values.entrySet()) {
            item.setText(columnNames.indexOf(value.getKey()), value.getValue());
        }
        return item;
    }

    /**
     * Clear the table and re-populate it.
     * 
     * @throws SophieException
     *             If the table could not be populated.
     */
    public void refresh() throws SophieException {
        rowValues = new ArrayList<Map<String, String>>();
        sortAsc = true;
        sortColumnName = null;
        setSortSignifier();
        table.removeAll();
        populate();
        // Let selection listeners know that no row is now selected.
        // This allows toolbar buttons that require a row to be selected to
        // become disabled.
        Event event = new Event();
        event.widget = table;
        event.display = table.getDisplay();
        event.type = SWT.DefaultSelection;
        table.notifyListeners(SWT.DefaultSelection, event);
    }

    /**
     * Populate the table with columns/rows.
     * 
     * @throws SophieException
     *             If the table could not be populated.
     */
    protected abstract void populate() throws SophieException;

    /**
     * Get the currently selected row.
     * 
     * @return Currently selected row.
     */
    protected TableItem[] getTableSelection() {
        return table.getSelection();
    }

}
