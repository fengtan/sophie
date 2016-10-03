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


public class CoresTable extends SortableTable {
	
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
		for (NamedList<Object> core: cores.values()) {
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
