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
package com.github.fengtan.sophie.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.github.fengtan.sophie.Sophie;
import com.github.fengtan.sophie.beans.SolrConnectionType;
import com.github.fengtan.sophie.beans.SolrUtils;
import com.github.fengtan.sophie.beans.SophieException;

/**
 * Tab folder containing a documents tab item, a fields tab item and a cores tab
 * item.
 */
public class TabFolder extends CTabFolder {

    /**
     * Documents tab item.
     */
    private DocumentsTabItem documentsTabItem;

    /**
     * Fields tab item.
     */
    private FieldsTabItem fieldsTabItem;

    /**
     * Cores tab item.
     */
    private CoresTabItem coresTabItem;

    /**
     * System tab item
     */
    private SystemTabItem systemTabItem;

    /**
     * Create a new tab folder containing a documents tab item, a fields tab
     * item and a cores tab item.
     * 
     * @param shell
     *            Shell.
     * @param connectionString
     *            Connection string displayed on the top right corner of the tab
     *            folder.
     * @param connectionType
     *            Connection type displayed on the top right corner of the tab
     *            folder.
     * @throws SophieException
     *             If no documents, fields or core could not retrieved from
     *             Solr.
     */
    public TabFolder(Shell shell, String connectionString, SolrConnectionType connectionType) throws SophieException {
        // Create tabs.
        super(shell, SWT.TOP | SWT.BORDER);

        // Configure tab folder.
        setBorderVisible(true);
        setLayoutData(new GridData(GridData.FILL_BOTH));
        setSimple(false);
        setTabHeight(25);

        // Add tabs if the associated endpoints are available.
        Exception exception = null;
        try {
            SolrUtils.getRemoteFields();
            documentsTabItem = new DocumentsTabItem(this);
            fieldsTabItem = new FieldsTabItem(this);
        } catch (SophieException e) {
            Sophie.log.info("Documents and fields tabs will not be displayed", e);
            exception = e;
        }
        try {
            SolrUtils.getCores();
            coresTabItem = new CoresTabItem(this);
        } catch (SophieException e) {
            Sophie.log.info("Cores tab will not be displayed", e);
            exception = e;
        }

        // TODO Add tab only if endpoint is available
        systemTabItem = new SystemTabItem(this);

        // If the Solr client allows to display no tab, then we have an invalid
        // client and we need to notify the user.
        if (documentsTabItem == null && fieldsTabItem == null && coresTabItem == null && systemTabItem == null) {
            throw new SophieException("Invalid connection \"" + connectionString + "\".", exception);
        }

        // Set focus on first tab.
        setSelection(getItem(0));
        setFocus();

        // Set up a gradient background for the selected tab
        Display display = shell.getDisplay();
        Color titleForeColor = display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
        Color titleBackColor1 = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
        Color titleBackColor2 = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
        setSelectionForeground(titleForeColor);
        setSelectionBackground(new Color[] { titleBackColor1, titleBackColor2 }, new int[] { 100 }, true);

        // Add title.
        ToolBar toolbar = new ToolBar(this, SWT.NULL);
        ToolItem item = new ToolItem(toolbar, SWT.NULL);
        item.setText(connectionString + " (" + connectionType.getTypeName() + ")");
        item.setEnabled(false);
        setTopRight(toolbar);
    }

    /**
     * Get documents tab item.
     * 
     * @return Documents tab item.
     */
    public DocumentsTabItem getDocumentsTabItem() {
        return documentsTabItem;
    }

    /**
     * Get fields tab item.
     * 
     * @return Fields tab item.
     */
    public FieldsTabItem getFieldsTabItem() {
        return fieldsTabItem;
    }

    /**
     * Get cores tab item.
     * 
     * @return Cores tab item.
     */
    public CoresTabItem getCoresTabItem() {
        return coresTabItem;
    }

}
