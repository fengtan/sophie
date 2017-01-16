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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
 * Tree listing Solr config files.
 */
public class FilesTree {

    /**
     * Tree.
     */
    private Tree tree;

    /**
     * Column 'File Name'.
     */
    private TreeColumn columnName;

    /**
     * Create a new tree listing Solr config files.
     * 
     * @param composite
     *            Parent composite.
     */
    public FilesTree(Composite composite) throws SophieException {
        // Instantiate Tree.
        tree = new Tree(composite, SWT.BORDER);
        tree.setHeaderVisible(true);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Create column.
        columnName = new TreeColumn(tree, SWT.LEFT);
        columnName.setText("File Name");

        // Populate tree with data from Solr.
        populate();

        // Pack columns.
        columnName.pack();
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
        // Get system info from Solr and populate tree.
        Map<String, Object> files = SolrUtils.getFiles(null);
        populate(files, null, StringUtils.EMPTY);
    }

    /**
     * Add a set of values to a TreeItem.
     * 
     * @param files
     *            Values to populate.
     * @param parentItem
     *            Parent TreeItem, or null if values should be added to the top
     *            of the Tree.
     * @param parentPath
     *            Path of the parent item (e.g. dir1/dir2).
     * 
     * @throws SophieException
     *             If the tree could not be populated.
     */
    @SuppressWarnings("unchecked")
    private void populate(Map<String, Object> files, TreeItem parentItem, String parentPath) throws SophieException {
        for (Map.Entry<String, Object> file : files.entrySet()) {
            String name = file.getKey();
            TreeItem item = (parentItem == null) ? new TreeItem(tree, SWT.NONE) : new TreeItem(parentItem, SWT.NONE);
            item.setText(name);
            NamedList<Object> properties = (NamedList<Object>) file.getValue();
            Boolean isDirectory = properties.getBooleanArg("directory");
            if (Boolean.TRUE.equals(isDirectory)) {
                String fullPath = StringUtils.isEmpty(parentPath) ? name : (parentPath + "/" + name);
                Map<String, Object> filesChild = SolrUtils.getFiles(fullPath);
                populate(filesChild, item, fullPath);
            }
        }
    }

}
