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

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.util.NamedList;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.sophie.beans.SolrUtils;
import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.dialogs.ExceptionDialog;

public class CoresTable extends AbstractSortableTable {

    public CoresTable(Composite parent, SelectionListener listener) {
        super(parent, listener);

        try {
            populate();
        } catch (SophieException e) {
            ExceptionDialog.open(parent.getShell(), new SophieException("Unable to initialize cores table", e));
        }
    }

    public String getSelectedCore() {
        TableItem[] items = getTableSelection();
        // Core name is in the first column
        return (items.length > 0) ? items[0].getText(0) : StringUtils.EMPTY;
    }

    /**
     * Populate columns + rows.
     */
    protected void populate() throws SophieException {
        Map<String, NamedList<Object>> cores;
        try {
            cores = SolrUtils.getCores();
        } catch (SophieException e) {
            throw new SophieException("Unable to populate cores table", e);
        }
        for (NamedList<Object> core : cores.values()) {
            Map<String, String> values = linearizeNamedList(core, new HashMap<String, String>());
            addRow(values);
        }
    }

    /**
     * Recursively convert a NamedList into a linear Map.
     */
    private Map<String, String> linearizeNamedList(NamedList<?> namedList, Map<String, String> map) {
        for (int idx = 0; idx < namedList.size(); idx++) {
            Object object = namedList.getVal(idx);
            if (object instanceof NamedList) {
                // NamedList: go through all elements recursively.
                linearizeNamedList((NamedList<?>) object, map);
            } else {
                // Not a NamedList: add element to the map.
                String name = namedList.getName(idx);
                // Create column if it does not exist yet.
                if (!hasColumn(name)) {
                    addColumn(name);
                }
                map.put(name, object.toString());
            }
        }
        return map;
    }

}
