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
import com.github.fengtan.solrgui.dialogs.RenameCoreDialog;
import com.github.fengtan.solrgui.tables.CoresTable;

public class CoresToolbar {

    private Image imgAdd;
    private Image imgDelete;
    private Image imgRename;
    private Image imgRefresh;
    
    private ToolItem itemAdd;
    private ToolItem itemDelete;
    private ToolItem itemRename;
    private ToolItem itemRefresh;
    
    public CoresToolbar(Composite composite) {
    	initToolbar(composite);
    }
    
    protected void initToolbar(final Composite composite) {
        Display display = composite.getDisplay();
        ClassLoader loader = getClass().getClassLoader();

        imgAdd = new Image(display, loader.getResourceAsStream("toolbar/add.png"));
        imgDelete = new Image(display, loader.getResourceAsStream("toolbar/delete.png"));
        imgRename = new Image(display, loader.getResourceAsStream("toolbar/clone.png")); // TODO find a better icon?
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
				CoresTable table = SolrGUI.tabFolder.getCoresTabItem().getTable();
				String coreName = table.getSelectedCore();
				table.deleteCore(coreName);
			}
		});
        
        itemRename = new ToolItem(toolBar, SWT.PUSH);
        itemRename.setImage(imgRename);
        itemRename.setToolTipText("Rename core"); //TODO disable when no core selected
        itemRename.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				CoresTable table = SolrGUI.tabFolder.getCoresTabItem().getTable();
				String coreName = table.getSelectedCore();
				new RenameCoreDialog(coreName).open();
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
