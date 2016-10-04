/**
 * Sophie - A Solr browser and administration tool
 * Copyright (C) 2016 fengtan<https://github.com/fengtan>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.fengtan.sophie.toolbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.dialogs.ExceptionDialog;
import com.github.fengtan.sophie.tables.FieldsTable;

public class FieldsToolbar {

    private Image imgRefresh;
    
    private ToolItem itemRefresh;
    
    private FieldsTable table;
    
    public FieldsToolbar(Composite composite) {
    	initToolbar(composite);
    }
    
    public void setTable(FieldsTable table) {
    	this.table = table;
    }
    
    protected void initToolbar(final Composite composite) {
        Display display = composite.getDisplay();
        ClassLoader loader = getClass().getClassLoader();

        imgRefresh = new Image(display, loader.getResourceAsStream("toolbar/refresh.png"));

        ToolBar toolBar = new ToolBar(composite, SWT.BORDER);
        
        itemRefresh = new ToolItem(toolBar, SWT.PUSH);
        itemRefresh.setImage(imgRefresh);
        itemRefresh.setText("Refresh");
        itemRefresh.setToolTipText("Refresh list of fields");
        itemRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					table.refresh();
				} catch (SophieException e) {
					ExceptionDialog.open(composite.getShell(), new SophieException("Unable to refresh fields table", e));
				}
			}
		});
        
        toolBar.pack();
    }
    
    @Override
    public void finalize() {
        imgRefresh.dispose();
    }
	
}
