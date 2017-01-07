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

import java.io.IOException;
import java.util.Objects;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.util.NamedList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.github.fengtan.sophie.Sophie;
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
	public SystemTree(Composite composite) {

		composite.setLayout(new FillLayout());
		// TODO expand window's width
		int style = SWT.BORDER;
		tree = new Tree(composite, style);
		tree.setHeaderVisible(true);

		columnName = new TreeColumn(tree, SWT.LEFT);
		columnName.setText("Name");

		columnValue = new TreeColumn(tree, SWT.LEFT);
		columnValue.setText("Value");

		// TODO comments

		QueryRequest request = new QueryRequest();
		request.setPath("/admin/system");
		try {
			NamedList<Object> namedList = Sophie.client.request(request);
			populate(namedList, tree.getTopItem());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		columnName.pack();
		columnValue.pack();
	}

	private void populate(NamedList<?> namedList, TreeItem parent) {
		for (int idx = 0; idx < namedList.size(); idx++) {
			TreeItem item = (parent == null) ? new TreeItem(tree, SWT.NONE) : new TreeItem(parent, SWT.NONE);
			String name = namedList.getName(idx);
			Object object = namedList.getVal(idx);
			if (object instanceof NamedList) {
				item.setText(new String[] { name, "" });
				populate((NamedList<?>) object, item);
			} else {
				String value = Objects.toString(object, "");
				item.setText(new String[] { name, value });
			}
		}
	}

	public void refresh() throws SophieException {
		// TODO implement
		// TODO throw SophieException ?
	}

}
