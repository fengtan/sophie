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

public class TabFolder extends CTabFolder {

    private DocumentsTabItem documentsTabItem;
    private FieldsTabItem fieldsTabItem;
    private CoresTabItem coresTabItem;

    public TabFolder(Shell shell, String connectionString) {
        // Create the tabs.
        super(shell, SWT.TOP | SWT.BORDER);

        // Configure tab folder.
        setBorderVisible(true);
        setLayoutData(new GridData(GridData.FILL_BOTH));
        setSimple(false);
        setTabHeight(25);

        // Add tab items.
        documentsTabItem = new DocumentsTabItem(this);
        fieldsTabItem = new FieldsTabItem(this);
        coresTabItem = new CoresTabItem(this);

        // Set focus on documents tab.
        setSelection(documentsTabItem);
        setFocus();

        // Set up a gradient background for the selected tab
        Display display = shell.getDisplay();
        Color titleForeColor = display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
        Color titleBackColor1 = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
        Color titleBackColor2 = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
        setSelectionForeground(titleForeColor);
        setSelectionBackground(new Color[] { titleBackColor1, titleBackColor2 }, new int[] { 100 }, true);

        // Add Solr URL.
        ToolBar toolbar = new ToolBar(this, SWT.NULL);
        ToolItem item = new ToolItem(toolbar, SWT.NULL);
        item.setText(connectionString);
        item.setEnabled(false);
        setTopRight(toolbar);
    }

    public DocumentsTabItem getDocumentsTabItem() {
        return documentsTabItem;
    }

    public FieldsTabItem getFieldsTabItem() {
        return fieldsTabItem;
    }

    public CoresTabItem getCoresTabItem() {
        return coresTabItem;
    }

}
