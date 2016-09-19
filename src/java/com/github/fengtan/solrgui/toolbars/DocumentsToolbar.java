package com.github.fengtan.solrgui.toolbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.github.fengtan.solrgui.SolrGUI;
import com.github.fengtan.solrgui.dialogs.IndexBackupDialog;
import com.github.fengtan.solrgui.dialogs.IndexRestoreDialog;

public class DocumentsToolbar implements SelectionListener {

    private Image imgRefresh;
    private Image imgAdd;
    private Image imgDelete;
    private Image imgClone;
    private Image imgUpload;
    private Image imgClear;
    private Image imgCommit;
    private Image imgOptimize;
    private Image imgExport;
    private Image imgBackup;
    private Image imgRestore;
    
    private ToolItem itemRefresh;
    private ToolItem itemAdd;
    private ToolItem itemDelete;
    private ToolItem itemClone;
    private ToolItem itemUpload;
    private ToolItem itemClear;
    private ToolItem itemCommit;
    private ToolItem itemOptimize;
    private ToolItem itemExport;
    private ToolItem itemBackup;
    private ToolItem itemRestore;
    
    public DocumentsToolbar(Composite composite) {
    	initToolbar(composite);
    }
    
    protected void initToolbar(final Composite composite) {
        Display display = composite.getDisplay();
        ClassLoader loader = getClass().getClassLoader();
        imgRefresh = new Image(display, loader.getResourceAsStream("toolbar/refresh.png"));
        imgAdd = new Image(display, loader.getResourceAsStream("toolbar/add.png"));
        imgDelete = new Image(display, loader.getResourceAsStream("toolbar/delete.png"));
        imgClone = new Image(display, loader.getResourceAsStream("toolbar/clone.png"));
        imgUpload = new Image(display, loader.getResourceAsStream("toolbar/upload.png")); // TODO find a better icon ?
        imgClear = new Image(display, loader.getResourceAsStream("toolbar/clear.png"));
        imgCommit = new Image(display, loader.getResourceAsStream("toolbar/commit.png"));
        imgOptimize = new Image(display, loader.getResourceAsStream("toolbar/optimize.png"));
        imgExport = new Image(display, loader.getResourceAsStream("toolbar/export.png")); // TODO find better icon ?
        imgBackup = new Image(display, loader.getResourceAsStream("toolbar/backup.png")); // TODO find better icon
        imgRestore = new Image(display, loader.getResourceAsStream("toolbar/restore.png")); // TODO find better icon

        ToolBar toolBar = new ToolBar(composite, SWT.BORDER);

        itemRefresh = new ToolItem(toolBar, SWT.PUSH);
        itemRefresh.setImage(imgRefresh);
        itemRefresh.setText("Refresh");
        itemRefresh.setToolTipText("Refresh from Solr: this will wipe out local modifications");
        itemRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUI.tabFolder.getDocumentsTabItem().getTable().refresh();
			}
		});
        
        new ToolItem(toolBar, SWT.SEPARATOR);
        
        // TODO allow to create documents with new fields
        itemAdd = new ToolItem(toolBar, SWT.PUSH);
        itemAdd.setImage(imgAdd);
        itemAdd.setText("Add");
        itemAdd.setToolTipText("Add new document");
        itemAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUI.tabFolder.getDocumentsTabItem().getTable().addEmptyDocument(); // TODO is table the right place to put upload(), clear(), etc ?
			}
		});

        itemDelete = new ToolItem(toolBar, SWT.PUSH);
        itemDelete.setImage(imgDelete);
        itemDelete.setText("Delete");
        itemDelete.setToolTipText("Delete document");
        itemDelete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUI.tabFolder.getDocumentsTabItem().getTable().deleteSelectedDocument();
			}
		});
        itemDelete.setEnabled(false);

        itemClone = new ToolItem(toolBar, SWT.PUSH);
        itemClone.setImage(imgClone);
        itemClone.setText("Clone");
        itemClone.setToolTipText("Clone document");
        itemClone.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUI.tabFolder.getDocumentsTabItem().getTable().cloneSelectedDocument();
			}
		});
        itemClone.setEnabled(false);
        
        itemUpload = new ToolItem(toolBar, SWT.PUSH);
        itemUpload.setImage(imgUpload);
        itemUpload.setText("Upload");
        itemUpload.setToolTipText("Upload local modifications to Solr");
        itemUpload.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUI.tabFolder.getDocumentsTabItem().getTable().upload();
			}
		});
        
        new ToolItem(toolBar, SWT.SEPARATOR);
                
        itemClear = new ToolItem(toolBar, SWT.PUSH);
        itemClear.setImage(imgClear);
        itemClear.setText("Clear");
        itemClear.setToolTipText("Clear index");
        itemClear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
		        MessageBox messageBox = new MessageBox(SolrGUI.shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		        messageBox.setText("Clear index");
		        messageBox.setMessage("Do you really want to clear the index? This will remove all documents from the index.");
		        int response = messageBox.open();
		        if (response == SWT.YES) {
		        	SolrGUI.tabFolder.getDocumentsTabItem().getTable().clear();
		        }
			}
		});

        itemCommit = new ToolItem(toolBar, SWT.PUSH);
        itemCommit.setImage(imgCommit);
        itemCommit.setText("Commit");
        itemCommit.setToolTipText("Commit index");
        itemCommit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUI.tabFolder.getDocumentsTabItem().getTable().commit();
			}
		});
        
        itemOptimize = new ToolItem(toolBar, SWT.PUSH);
        itemOptimize.setImage(imgOptimize);
        itemOptimize.setText("Optimize");
        itemOptimize.setToolTipText("Optimize index");
        itemOptimize.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
		        MessageBox messageBox = new MessageBox(SolrGUI.shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		        messageBox.setText("Optimize index");
		        messageBox.setMessage("Do you really want to optimize the index? If the index is highly segmented, this may take several hours and will slow down requests.");
		        int response = messageBox.open();
		        if (response == SWT.YES) {
		        	SolrGUI.tabFolder.getDocumentsTabItem().getTable().optimize();
		        }
			}
		});
        
        new ToolItem(toolBar, SWT.SEPARATOR);
        
        itemExport = new ToolItem(toolBar, SWT.PUSH);
        itemExport.setImage(imgExport);
        itemExport.setText("Export");
        itemExport.setToolTipText("Export as CSV file");
        itemExport.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUI.tabFolder.getDocumentsTabItem().getTable().export();
			}
		});
        
        itemBackup = new ToolItem(toolBar, SWT.PUSH);
        itemBackup.setImage(imgBackup);
        itemBackup.setText("Backup");
        itemBackup.setToolTipText("Make a backup of the index");
        itemBackup.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
	        	new  IndexBackupDialog(SolrGUI.shell).open();
			}
		});
        
        itemRestore = new ToolItem(toolBar, SWT.PUSH);
        itemRestore.setImage(imgRestore);
        itemRestore.setText("Restore");
        itemRestore.setToolTipText("Restore index from a backup");
        itemRestore.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
	        	new IndexRestoreDialog(SolrGUI.shell).open();
			}
		});
        
        toolBar.pack();
    }
    
    @Override
    public void finalize() {
        imgRefresh.dispose();
        imgAdd.dispose();
        imgDelete.dispose();
        imgClone.dispose();
        imgUpload.dispose();
        imgClear.dispose();
        imgCommit.dispose();
        imgOptimize.dispose();
        imgExport.dispose();
        imgBackup.dispose();
        imgRestore.dispose();
    }

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// Do nothing.
	}

	/**
	 * Toggle buttons that require a document to be selected.
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		itemDelete.setEnabled(true);
		itemClone.setEnabled(true);
	}
    
}
