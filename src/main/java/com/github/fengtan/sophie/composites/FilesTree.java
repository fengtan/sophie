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
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.util.NamedList;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.github.fengtan.sophie.Sophie;
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
     * Viewer.
     */
    private StyledText styledText;

    /**
     * Create a new tree listing Solr config files.
     * 
     * @param composite
     *            Parent composite.
     */
    public FilesTree(Composite composite) throws SophieException {
        Composite parent = new Composite(composite, SWT.NULL);
        parent.setLayout(new GridLayout(2, true));
        parent.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Instantiate Tree.
        tree = new Tree(parent, SWT.BORDER);
        tree.setHeaderVisible(true);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Display file in viewer when user double clicks tree item.
        tree.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Point point = new Point(event.x, event.y);
                TreeItem item = tree.getItem(point);
                if (item != null) {
                    String fullPath = Objects.toString(item.getData(), "");
                    String content;
                    if (StringUtils.isEmpty(fullPath)) {
                        content = "This is a directory.";
                    } else {
                        try {
                            content = SolrUtils.getFileContent(fullPath);
                        } catch (SophieException e) {
                            content = "Unable to fetch content for " + fullPath + ": " + e.getMessage();
                            Sophie.log.error("Unable to fetch content for " + fullPath, e);
                        }
                    }
                    styledText.setText(content);
                }
            }
        });

        // Create column.
        columnName = new TreeColumn(tree, SWT.LEFT);
        columnName.setText("File Name");

        // Instantiate viewer.
        styledText = new StyledText(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        styledText.setText("No file selected.");
        styledText.setEditable(false);
        styledText.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Config files are easier to read with a monospaced font.
        Font monospaced = JFaceResources.getFont(JFaceResources.TEXT_FONT);
        styledText.setFont(monospaced);

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
        styledText.setText("No file selected.");
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
        Map<String, Object> files = SolrUtils.getFilesList(null);
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
            final NamedList<Object> properties = (NamedList<Object>) file.getValue();
            // Add tree item.
            TreeItem item = (parentItem == null) ? new TreeItem(tree, SWT.NONE) : new TreeItem(parentItem, SWT.NONE);
            item.setText(name);
            // If directory, then get the list of files it contains.
            // Otherwise, store full path so MouseDoubleClickListener knows
            // which file to retrieve.
            String fullPath = StringUtils.isEmpty(parentPath) ? name : (parentPath + "/" + name);
            Boolean isDirectory = properties.getBooleanArg("directory");
            if (Boolean.TRUE.equals(isDirectory)) {
                Map<String, Object> filesChild = SolrUtils.getFilesList(fullPath);
                populate(filesChild, item, fullPath);
            } else {
                item.setData(fullPath);
            }
        }
    }

}
