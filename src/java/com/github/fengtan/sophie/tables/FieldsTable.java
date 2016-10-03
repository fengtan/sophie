package com.github.fengtan.sophie.tables;

import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.luke.FieldFlag;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.sophie.beans.SolrUtils;
import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.dialogs.ExceptionDialog;

public class FieldsTable extends SortableTable {
	
	private static final String[] columnNames = new String[]{
		"Name",
		"Type",
		"Unique",
		"Distinct",
		"Schema"
	};
	
	public FieldsTable(Composite parent) {
		super(parent);

		// Add columns
		for (String columnName:columnNames) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(columnName);
		}
		for (FieldFlag flag:FieldFlag.values()) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(flag.getDisplay());			
		}
		
		// Add data.
		try {
			populate();	
		} catch (SophieException e) {
			ExceptionDialog.open(parent.getShell(), new SophieException("Unable to populate fields table", e));
		}
		
		// Pack.
		for(TableColumn column:table.getColumns()) {
			column.pack();// TODO needed ? might be worth to setLayout() to get rid of this
		}
	}
	
	public void refresh() throws SophieException {
		table.removeAll();
		populate();
	}
	
	protected void populate() throws SophieException {
		// TODO cache uniqueKey ? 2 identical requests (fields+tables), should remove 1 of the 2 and invalidate when hit refresh
		String uniqueField;
		List<FieldInfo> fields;
		uniqueField = SolrUtils.getRemoteUniqueField();
		fields = SolrUtils.getRemoteFields();	
		for (FieldInfo field:fields) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(0, field.getName());
			item.setText(1, field.getType());
			item.setText(2, Boolean.toString(StringUtils.equals(field.getName(), uniqueField)));
			item.setText(3, Integer.toString(field.getDistinct()));
			item.setText(4, field.getSchema());
			EnumSet<FieldFlag> flags = SolrUtils.getFlags(field);
			int i = columnNames.length;
			for (FieldFlag flag:FieldFlag.values()) {
				item.setText(i, Boolean.toString(flags.contains(flag)));
				i++;
			}
		}
	}

}
