package com.github.fengtan.sophie.toolbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

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
			public void widgetSelected(SelectionEvent e) {
				table.refresh();
			}
		});
        
        toolBar.pack();
    }
    
    @Override
    public void finalize() {
        imgRefresh.dispose();
    }
	
}
