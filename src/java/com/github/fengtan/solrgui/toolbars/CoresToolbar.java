package com.github.fengtan.solrgui.toolbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.github.fengtan.solrgui.SolrGUI;
import com.github.fengtan.solrgui.dialogs.NewCoreDialog;

public class CoresToolbar {

    private Image imgAdd;
    private Image imgDelete;
    private Image imgRefresh;
    
    private ToolItem itemAdd;
    private ToolItem itemDelete;
    private ToolItem itemRefresh;
    
    public CoresToolbar(Composite composite) {
    	initToolbar(composite);
    }
    protected void initToolbar(final Composite composite) {
        Display display = composite.getDisplay();
        ClassLoader loader = getClass().getClassLoader();

        imgAdd = new Image(display, loader.getResourceAsStream("toolbar/add.png"));
        imgDelete = new Image(display, loader.getResourceAsStream("toolbar/delete.png"));
        imgRefresh = new Image(display, loader.getResourceAsStream("toolbar/refresh.png"));

        ToolBar toolBar = new ToolBar(composite, SWT.BORDER);

        itemAdd = new ToolItem(toolBar, SWT.PUSH);
        itemAdd.setImage(imgAdd);
        itemAdd.setToolTipText("Add new core");
        itemAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				NewCoreDialog.getDialog().open();
			}
		});

        itemDelete = new ToolItem(toolBar, SWT.PUSH);
        itemDelete.setImage(imgDelete);
        itemDelete.setToolTipText("Delete core"); //TODO disable when no core selected
        itemDelete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO prompt confirmation ?
				SolrGUI.tabFolder.getCoresTabItem().getTable().deleteSelectedCore();
			}
		});
        
        new ToolItem(toolBar, SWT.SEPARATOR);
        
        itemRefresh = new ToolItem(toolBar, SWT.PUSH);
        itemRefresh.setImage(imgRefresh);
        itemRefresh.setToolTipText("Refresh list of cores");
        itemRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUI.tabFolder.getCoresTabItem().getTable().refresh();
			}
		});
        
        toolBar.pack();
    }
    
    @Override
    public void finalize() {
    	imgAdd.dispose();
    	imgDelete.dispose();
        imgRefresh.dispose();
    }
	
}
