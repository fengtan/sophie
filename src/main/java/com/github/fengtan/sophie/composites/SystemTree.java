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
package com.github.fengtan.sophie.trees;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.util.NamedList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.github.fengtan.sophie.beans.SolrUtils;
import com.github.fengtan.sophie.beans.SophieException;

/**
 * Tree listing Solr system information.
 */
public class SystemTree {

	/**
	 * Tree.
	 */
	private Tree tree;

	/**
	 * Column 'Name'.
	 */
	private TreeColumn columnName;

	/**
	 * Column 'Value'.
	 */
	private TreeColumn columnValue;

	/**
	 * Create a new tree listing Solr system information.
	 * 
	 * @param composite
	 *            Parent composite.
	 */
	public SystemTree(Composite composite) throws SophieException {
		// Instantiate Tree.
		tree = new Tree(composite, SWT.BORDER);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Create name/value columns.
		columnName = new TreeColumn(tree, SWT.LEFT);
		columnName.setText("Name");
		columnValue = new TreeColumn(tree, SWT.LEFT);
		columnValue.setText("Value");

		// Populate tree with data from Solr.
		populate();

		// Pack columns.
		columnName.pack();
		columnValue.pack();
	}

	/**
	 * Clear the tree and re-populate it.
	 * 
	 * @throws SophieException
	 *             If the tree could not be populated.
	 */
	public void refresh() throws SophieException {
		tree.removeAll();
		populate();
	}

	/**
	 * Populate the tree.
	 * 
	 * @throws SophieException
	 *             If the tree could not be populated.
	 */
	private void populate() throws SophieException {
		// Get system info from Solr and populate table.
		NamedList<Object> namedListInfo = SolrUtils.getSystemInfo();
		populate(namedListInfo, null);
		// Get system properties from Solr and populate table.
		NamedList<Object> namedListProperties = SolrUtils.getSystemProperties();
		populate(namedListProperties, null);
	}

	/**
	 * Add a set of values to a TreeItem.
	 * 
	 * @param parent
	 *            Parent TreeItem, or null if values should be added to the top
	 *            of the Tree.
	 * 
	 * @throws SophieException
	 *             If the tree could not be populated.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void populate(Iterable<Map.Entry<String, Object>> iterable, TreeItem parent) {
		for (Map.Entry<String, Object> entry : iterable) {
			// No need to display header information.
			if (StringUtils.equals(entry.getKey(), "responseHeader")) {
				continue;
			}
			// If value is a map, use iterable entry set.
			Object value = entry.getValue();
			if (value instanceof Map) {
				value = ((Map) value).entrySet();
			}
			// Create TreeItem for each entry.
			TreeItem item = (parent == null) ? new TreeItem(tree, SWT.NONE) : new TreeItem(parent, SWT.NONE);
			if (value instanceof Iterable) {
				// Value is iterable: iterate over its entries.
				item.setText(new String[] { entry.getKey(), "" });
				populate((Iterable) value, item);
			} else {
				// Value is not iterable: add regular TreeItem with value.
				item.setText(new String[] { entry.getKey(), Objects.toString(value, "") });
			}
		}
	}

}
