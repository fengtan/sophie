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

/**
 * Table listing Solr fields.
 */
public class FieldsTable extends AbstractSortableTable {

    /**
     * Static column names.
     */
    private static final String[] columnNames = new String[] { "Name", "Type", "Unique", "Distinct", "Schema" };

    /**
     * Create a new table listing Solr fields.
     * 
     * @param composite
     *            Parent composite.
     */
    public FieldsTable(Composite composite) {
        super(composite);

        // Add columns (static + flags).
        for (String columnName : columnNames) {
            addColumn(columnName);
        }
        for (FieldFlag flag : FieldFlag.values()) {
            addColumn(flag.getDisplay());
        }

        // Add rows.
        try {
            populate();
        } catch (SophieException e) {
            ExceptionDialog.open(composite.getShell(), new SophieException("Unable to populate fields table", e));
        }
    }

    @Override
    protected void populate() throws SophieException {
        // Get remote fields + unique key.
        String uniqueField = SolrUtils.getRemoteUniqueField();
        List<FieldInfo> fields = SolrUtils.getRemoteFields();
        
        // Populate table.
        for (FieldInfo field : fields) {
            Map<String, String> values = new HashMap<String, String>();
            values.put("Name", field.getName());
            values.put("Type", field.getType());
            values.put("Unique", Boolean.toString(StringUtils.equals(field.getName(), uniqueField)));
            values.put("Distinct", Integer.toString(field.getDistinct()));
            values.put("Schema", field.getSchema());
            for (FieldFlag flag : FieldFlag.values()) {
                EnumSet<FieldFlag> flags = SolrUtils.getFlags(field);
                values.put(flag.getDisplay(), Boolean.toString(flags.contains(flag)));
            }
            addRow(values);
        }
    }

}
