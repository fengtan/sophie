package com.github.fengtan.solrgui.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.github.fengtan.solrgui.SolrGUI;
import com.github.fengtan.solrgui.tabs.SolrGUITabItem;

public class SolrGUIToolbar {

    private Image imgAdd;
    private Image imgDelete;
    private Image imgClone;
    private Image imgRefresh;
    private Image imgCommit;
    private Image imgSettings;
    
    private SolrGUI solrGUI;

    public SolrGUIToolbar(Shell shell, SolrGUI solrGUI) {
    	initToolbar(shell);
    	this.solrGUI = solrGUI;
    }
    
    protected void initToolbar(Shell shell) {
        Display display = shell.getDisplay();

        imgAdd = new Image(display, "img/toolbar-add.svg");
        imgDelete = new Image(display, "img/toolbar-delete.svg");
        imgClone = new Image(display, "img/toolbar-clone.svg");
        imgRefresh = new Image(display, "img/toolbar-refresh.svg");
        imgCommit = new Image(display, "img/toolbar-commit.svg"); // TODO find a better icon ?
        imgSettings = new Image(display, "img/toolbar-settings.svg");

        // TODO disabled whole toolbar when no server configured
        ToolBar toolBar = new ToolBar(shell, SWT.BORDER);

        ToolItem itemAdd = new ToolItem(toolBar, SWT.PUSH);
        itemAdd.setImage(imgAdd);
        itemAdd.setToolTipText("Add new document");
        itemAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.addNewDocument();
			}
		});

        ToolItem itemDelete = new ToolItem(toolBar, SWT.PUSH);
        itemDelete.setImage(imgDelete);
        itemDelete.setToolTipText("Delete document"); // TODO disabled when no document selected
        itemDelete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.deleteCurrentDocument();
			}
		});

        ToolItem itemClone = new ToolItem(toolBar, SWT.PUSH);
        itemClone.setImage(imgClone);
        itemClone.setToolTipText("Clone document"); // TODO disabled when no document selected
        itemClone.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.cloneCurrentDocument();
			}
		});

        new ToolItem(toolBar, SWT.SEPARATOR);

        ToolItem itemRefresh = new ToolItem(toolBar, SWT.PUSH);
        itemRefresh.setImage(imgRefresh);
        itemRefresh.setToolTipText("Refresh from server: this will revert local modifications");
        itemRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.refresh();
			}
		});
        
        ToolItem itemCommit = new ToolItem(toolBar, SWT.PUSH);
        itemCommit.setImage(imgCommit);
        itemCommit.setToolTipText("Commit local modifications to server"); // TODO disabled when no local modifications
        itemCommit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.commit();
			}
		});

        new ToolItem(toolBar, SWT.SEPARATOR);
        
        ToolItem itemSettings = new ToolItem(toolBar, SWT.PUSH);
        itemSettings.setImage(imgSettings);
        itemSettings.setToolTipText("Settings");
        // TODO listener
        
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
    
    // TODO form criteria 'filter field XX and filter field XX'
	
}
