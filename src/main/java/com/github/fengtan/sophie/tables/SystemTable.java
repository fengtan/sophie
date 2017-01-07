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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.sophie.beans.SophieException;

/**
 * Table listing Solr system information.
 */
public class SystemTable extends AbstractSortableTable {

    /**
     * Create a new table listing Solr system information.
     * 
     * @param composite
     *            Parent composite.
     * @throws SophieException
     *             If the table could not be initialized.
     */
    public SystemTable(Composite composite) throws SophieException {
        super(composite);

        // TODO
        addColumn("Foo");
        addColumn("Bar");

        // Add rows.
        populate();
    }

    @Override
    protected void populate() throws SophieException {
        // TODO
        Map<String, String> values = new HashMap<String, String>();
        values.put("Foo", "AA");
        values.put("Bar", "BB");
        addRow(values);
    }

}
