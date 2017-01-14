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
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.composites.SystemTree;
import com.github.fengtan.sophie.toolbars.SystemToolbar;

/**
 * Tab displaying a system toolbar and a system table.
 */
public class SystemTabItem extends CTabItem {

    /**
     * System toolbar.
     */
    private SystemToolbar toolbar;

    /**
     * System table.
     */
    private SystemTree table;

    /**
     * Create a new tab displaying a system toolbar and a system table.
     * 
     * @param tabFolder
     *            Parent tab folder.
     * @throws SophieException
     *             If the tab could not be initalized.
     */
    public SystemTabItem(CTabFolder tabFolder) throws SophieException {
        super(tabFolder, SWT.NONE, tabFolder.getItemCount());
        setText("System");

        // Prepare layout.
        Composite composite = new Composite(getParent(), SWT.NULL);
        composite.setLayout(new GridLayout());
        setControl(composite);

        // Add toolbar and table.
        toolbar = new SystemToolbar(composite);
        table = new SystemTree(composite);
        toolbar.setTable(table);

        // Pack.
        composite.pack();
    }

    @Override
    public void dispose() {
        toolbar.finalize();
        super.dispose();
    }

}
