package com.github.fengtan.solrgui.toolbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.github.fengtan.solrgui.SolrGUI;
import com.github.fengtan.solrgui.dialogs.SolrGUIBackupIndexDialog;
import com.github.fengtan.solrgui.dialogs.SolrGUIRestoreIndexDialog;
import com.github.fengtan.solrgui.tabs.ISolrGUITabFolderListener;
import com.github.fengtan.solrgui.tabs.SolrGUITabItem;

public class SolrGUIToolbar implements ISolrGUITabFolderListener {

    private Image imgAdd;
    private Image imgDelete;
    private Image imgClone;
    private Image imgRefresh;
    private Image imgUpload;
    private Image imgClear;
    private Image imgCommit;
    private Image imgOptimize;
    private Image imgBackup;
    private Image imgRestore;
    
    private ToolItem itemAdd;
    private ToolItem itemDelete;
    private ToolItem itemClone;
    private ToolItem itemRefresh;
    private ToolItem itemUpload;
    private ToolItem itemClear;
    private ToolItem itemCommit;
    private ToolItem itemOptimize;
    private ToolItem itemBackup;
    private ToolItem itemRestore;
    
    private SolrGUI solrGUI;
    
    public SolrGUIToolbar(Shell shell, SolrGUI solrGUI) {
    	initToolbar(shell);
    	this.solrGUI = solrGUI;
    }
    
    protected void initToolbar(final Shell shell) {
        Display display = shell.getDisplay();
        ClassLoader loader = getClass().getClassLoader();
        imgAdd = new Image(display, loader.getResourceAsStream("toolbar/add.png"));
        imgDelete = new Image(display, loader.getResourceAsStream("toolbar/delete.png"));
        imgClone = new Image(display, loader.getResourceAsStream("toolbar/clone.png"));
        imgRefresh = new Image(display, loader.getResourceAsStream("toolbar/refresh.png"));
        imgUpload = new Image(display, loader.getResourceAsStream("toolbar/upload.png")); // TODO find a better icon ?
        imgClear = new Image(display, loader.getResourceAsStream("toolbar/clear.png"));
        imgCommit = new Image(display, loader.getResourceAsStream("toolbar/commit.png"));
        imgOptimize = new Image(display, loader.getResourceAsStream("toolbar/optimize.png"));
        imgBackup = new Image(display, loader.getResourceAsStream("toolbar/backup.png")); // TODO find better icon
        imgRestore = new Image(display, loader.getResourceAsStream("toolbar/restore.png")); // TODO find better icon

        ToolBar toolBar = new ToolBar(shell, SWT.BORDER);

        // TODO allow to create documents with new fields
        itemAdd = new ToolItem(toolBar, SWT.PUSH);
        itemAdd.setEnabled(false);
        itemAdd.setImage(imgAdd);
        itemAdd.setToolTipText("Add new document");
        itemAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.getTable().addEmptyDocument();
			}
		});

        itemDelete = new ToolItem(toolBar, SWT.PUSH);
        itemDelete.setEnabled(false);
        itemDelete.setImage(imgDelete);
        itemDelete.setToolTipText("Delete document");
        itemDelete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.getTable().deleteSelectedDocument();
			}
		});

        itemClone = new ToolItem(toolBar, SWT.PUSH);
        itemClone.setEnabled(false);
        itemClone.setImage(imgClone);
        itemClone.setToolTipText("Clone document");
        itemClone.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.getTable().cloneSelectedDocument();
			}
		});

        new ToolItem(toolBar, SWT.SEPARATOR);

        itemRefresh = new ToolItem(toolBar, SWT.PUSH);
        itemRefresh.setEnabled(false);
        itemRefresh.setImage(imgRefresh);
        itemRefresh.setToolTipText("Refresh from server: this will wipe out local modifications");
        itemRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.getTable().refresh();
			}
		});
        
        itemUpload = new ToolItem(toolBar, SWT.PUSH);
        itemUpload.setEnabled(false);
        itemUpload.setImage(imgUpload);
        itemUpload.setToolTipText("Upload local modifications to server");
        itemUpload.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.getTable().upload();
			}
		});
        
        new ToolItem(toolBar, SWT.SEPARATOR);
        
        itemClear = new ToolItem(toolBar, SWT.PUSH);
        itemClear.setEnabled(false);
        itemClear.setImage(imgClear);
        itemClear.setToolTipText("Clear index");
        itemClear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
		        MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		        messageBox.setText("Clear index");
		        messageBox.setMessage("Do you really want to clear the index? This will wipe out all documents on the server.");
		        int response = messageBox.open();
		        if (response == SWT.YES) {
		        	SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
					tabItem.getTable().clear();
		        }
			}
		});

        itemCommit = new ToolItem(toolBar, SWT.PUSH);
        itemCommit.setEnabled(false);
        itemCommit.setImage(imgCommit);
        itemCommit.setToolTipText("Commit index");
        itemCommit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
		        SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
				tabItem.getTable().commit();
			}
		});
        
        itemOptimize = new ToolItem(toolBar, SWT.PUSH);
        itemOptimize.setEnabled(false);
        itemOptimize.setImage(imgOptimize);
        itemOptimize.setToolTipText("Optimize index");
        itemOptimize.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
		        MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		        messageBox.setText("Optimize index");
		        messageBox.setMessage("Do you really want to optimize the index? If the index is highly segmented, this may take several hours and will slow down requests.");
		        int response = messageBox.open();
		        if (response == SWT.YES) {
		        	SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
					tabItem.getTable().optimize();
		        }
			}
		});
        
        new ToolItem(toolBar, SWT.SEPARATOR);
        
        itemBackup = new ToolItem(toolBar, SWT.PUSH);
        itemBackup.setEnabled(false);
        itemBackup.setImage(imgBackup);
        itemBackup.setToolTipText("Make a backup of the index");
        itemBackup.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
	        	SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
	        	new  SolrGUIBackupIndexDialog(shell, tabItem.getTable()).open();
			}
		});
        
        itemRestore = new ToolItem(toolBar, SWT.PUSH);
        itemRestore.setEnabled(false);
        itemRestore.setImage(imgRestore);
        itemRestore.setToolTipText("Restore index from a backup");
        itemRestore.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
	        	SolrGUITabItem tabItem = (SolrGUITabItem) solrGUI.getTabFolder().getSelection();
	        	new  SolrGUIRestoreIndexDialog(shell, tabItem.getTable()).open();
			}
		});
        
        toolBar.pack();
    }
    
    @Override
    public void finalize() {
        imgAdd.dispose();
        imgDelete.dispose();
        imgClone.dispose();
        imgRefresh.dispose();
        imgUpload.dispose();
        imgClear.dispose();
        imgCommit.dispose();
        imgOptimize.dispose();
        imgBackup.dispose();
        imgRestore.dispose();
    }

	// TODO a row should be selected for itemAdd/delete/clone to be enabled
	@Override
	public void tabItemsCountModified(int newCount) {
		boolean enabled = (newCount != 0);
		itemAdd.setEnabled(enabled);
		itemDelete.setEnabled(enabled);
		itemClone.setEnabled(enabled);
		itemRefresh.setEnabled(enabled);
		itemUpload.setEnabled(enabled);
		itemClear.setEnabled(enabled);
		itemCommit.setEnabled(enabled);
		itemOptimize.setEnabled(enabled);
		itemBackup.setEnabled(enabled);
		itemRestore.setEnabled(enabled);
	}

}
