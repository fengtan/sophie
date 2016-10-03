package com.github.fengtan.sophie.tables;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.luke.FieldFlag;
import org.eclipse.swt.widgets.Composite;

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
			addColumn(columnName);
		}
		for (FieldFlag flag:FieldFlag.values()) {
			addColumn(flag.getDisplay());			
		}
		
		// Add rows.
		try {
			populate();	
		} catch (SophieException e) {
			ExceptionDialog.open(parent.getShell(), new SophieException("Unable to populate fields table", e));
		}
	}
	
	protected void populate() throws SophieException {
		// TODO cache uniqueKey ? 2 identical requests (fields+tables), should remove 1 of the 2 and invalidate when hit refresh
		String uniqueField = SolrUtils.getRemoteUniqueField();
		List<FieldInfo> fields = SolrUtils.getRemoteFields();	
		for (FieldInfo field:fields) {
			Map<String, String> values = new HashMap<String, String>();
			values.put("Name", field.getName());
			values.put("Type", field.getType());
			values.put("Unique", Boolean.toString(StringUtils.equals(field.getName(), uniqueField)));
			values.put("Distinct", Integer.toString(field.getDistinct()));
			values.put("Schema", field.getSchema());
			for (FieldFlag flag:FieldFlag.values()) {
				EnumSet<FieldFlag> flags = SolrUtils.getFlags(field);
				values.put(flag.getDisplay(), Boolean.toString(flags.contains(flag)));
			}
			addRow(values);
		}
	}

}
