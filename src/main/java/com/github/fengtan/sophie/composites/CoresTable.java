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
package com.github.fengtan.sophie.composites;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.util.NamedList;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.sophie.beans.SolrUtils;
import com.github.fengtan.sophie.beans.SophieException;

/**
 * Table listing Solr cores.
 */
public class CoresTable extends AbstractSortableTable {

    /**
     * Create a new table listing Solr cores.
     * 
     * @param composite
     *            Parent composite.
     * @param listener
     *            Selection listener to attach to the table.
     * @throws SophieException
     *             If the table could not be initialized.
     */
    public CoresTable(Composite composite, SelectionListener listener) throws SophieException {
        super(composite, listener);
        populate();
    }

    /**
     * Get the currently selected core.
     * 
     * @return Currently selected core.
     */
    public String getSelectedCore() {
        TableItem[] items = getTableSelection();
        // Core name is in the first column
        return (items.length > 0) ? items[0].getText(0) : StringUtils.EMPTY;
    }

    @Override
    protected void populate() throws SophieException {
        // Get remote cores.
        Map<String, NamedList<Object>> cores;
        try {
            cores = SolrUtils.getRemoteCores();
        } catch (SophieException e) {
            throw new SophieException("Unable to populate cores table", e);
        }

        // Populate table.
        for (NamedList<Object> core : cores.values()) {
            Map<String, String> values = linearizeNamedList(core, new HashMap<String, String>());
            addRow(values);
        }
    }

    /**
     * Recursively convert a hierarchical NamedList into a linear Map.
     * 
     * @param namedList
     *            Hierarchical NamedList.
     * @param map
     *            Map to be populated.
     * @return Linear Map populated with values in the NamedList.
     */
    private Map<String, String> linearizeNamedList(NamedList<?> namedList, Map<String, String> map) {
        // Inspect all elements in the NamedList.
        for (int idx = 0; idx < namedList.size(); idx++) {
            Object object = namedList.getVal(idx);
            if (object instanceof NamedList) {
                // Element is a NamedList: populate the map recursively.
                linearizeNamedList((NamedList<?>) object, map);
            } else {
                // Element is not a NamedList: add it to the map.
                String name = namedList.getName(idx);
                map.put(name, object.toString());
                // Create column if it does not exist yet.
                if (!hasColumn(name)) {
                    addColumn(name);
                }
            }
        }
        // Return populated map.
        return map;
    }

}
