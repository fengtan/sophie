package com.github.fengtan.solrgui.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class SolrGUIToolbar {

    private Image imgAdd;
    private Image imgDelete;
    private Image imgClone;
    private Image imgRefresh;
    private Image imgCommit;

    public SolrGUIToolbar(Shell shell) {
        Device dev = shell.getDisplay();

        imgAdd = new Image(dev, "img/toolbar-add.svg");
        imgDelete = new Image(dev, "img/toolbar-delete.svg");
        imgClone = new Image(dev, "img/toolbar-clone.svg");
        imgRefresh = new Image(dev, "img/toolbar-refresh.svg");
        imgCommit = new Image(dev, "img/toolbar-commit.svg");

        // TODO disabled whole toolbar when no server configured
        ToolBar toolBar = new ToolBar(shell, SWT.BORDER);

        ToolItem itemAdd = new ToolItem(toolBar, SWT.PUSH);
        itemAdd.setImage(imgAdd);
        itemAdd.setToolTipText("Add new document");

        ToolItem itemDelete = new ToolItem(toolBar, SWT.PUSH);
        itemDelete.setImage(imgDelete);
        itemDelete.setToolTipText("Delete document"); // TODO disabled when no document selected 

        ToolItem itemClone = new ToolItem(toolBar, SWT.PUSH);
        itemClone.setImage(imgClone);
        itemClone.setToolTipText("Clone document"); // TODO disabled when no document selected

        new ToolItem(toolBar, SWT.SEPARATOR);

        ToolItem itemRefresh = new ToolItem(toolBar, SWT.PUSH);
        itemRefresh.setImage(imgRefresh);
        itemRefresh.setToolTipText("Refresh from server: this will revert local modifications");
        
        ToolItem itemCommit = new ToolItem(toolBar, SWT.PUSH);
        itemCommit.setImage(imgCommit);
        itemCommit.setToolTipText("Commit local modifications to server"); // TODO disabled when no local modifications
        
        toolBar.pack();
    }
    
    @Override
    public void finalize() {
        imgAdd.dispose();
        imgDelete.dispose();
        imgClone.dispose();
        imgRefresh.dispose();
        imgCommit.dispose();
    }	
	
}
