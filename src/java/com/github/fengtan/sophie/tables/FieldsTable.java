package com.github.fengtan.sophie.tables;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.luke.FieldFlag;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.sophie.utils.SolrUtils;

public class FieldsTable {
	
	private static final String[] columnNames = new String[]{
		"Name",
		"Type",
		"Unique",
		"Distinct",
		"Schema"
	};
	
	private Table table;
	
	public FieldsTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL;

		table = new Table(parent, style); // TODO turn into a FieldsTable class ?

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

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
		populate();
		
		// Pack.
		for(TableColumn column:table.getColumns()) {
			column.pack();// TODO needed ? might be worth to setLayout() to get rid of this
		}
	}
	
	public void refresh() {
		table.removeAll();
		populate();
	}
	
	private void populate() {
		// TODO cache uniqueKey ? 2 identical requests (fields+tables), should remove 1 of the 2 and invalidate when hit refresh
		String uniqueField = SolrUtils.getRemoteUniqueField();
		for (FieldInfo field:SolrUtils.getRemoteFields()) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(0, field.getName());
			item.setText(1, field.getType());
			item.setText(2, Boolean.toString(StringUtils.equals(field.getName(), uniqueField)));
			item.setText(3, Integer.toString(field.getDistinct()));
			item.setText(4, field.getSchema());
			// field.getFlags() is not populated when lukeRequest.setSchema(false) so we parse flags ourselves based on field.getSchema() - TODO update code when SOLR-9205 is closed
			EnumSet<FieldFlag> flags = FieldInfo.parseFlags(field.getSchema());
			int i = columnNames.length;
			for (FieldFlag flag:FieldFlag.values()) {
				item.setText(i, Boolean.toString(flags.contains(flag)));
				i++;
			}
		}
	}

}
