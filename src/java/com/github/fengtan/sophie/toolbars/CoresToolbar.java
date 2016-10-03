package com.github.fengtan.sophie.toolbars;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
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

import com.github.fengtan.sophie.Sophie;
import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.dialogs.CoreAddDialog;
import com.github.fengtan.sophie.dialogs.CoreSwapDialog;
import com.github.fengtan.sophie.dialogs.ErrorDialog;
import com.github.fengtan.sophie.tables.CoresTable;

public class CoresToolbar implements SelectionListener {

    private Image imgRefresh;
    private Image imgAdd;
    private Image imgDelete;
    private Image imgRename;
    private Image imgSwap;
    private Image imgReload;
    
    private ToolItem itemRefresh;
    private ToolItem itemAdd;
    private ToolItem itemDelete;
    private ToolItem itemRename;
    private ToolItem itemSwap;
    private ToolItem itemReload;
    
    private CoresTable table;
    
    public CoresToolbar(Composite composite) {
    	initToolbar(composite);
    }
    
    public void setTable(CoresTable table) {
    	this.table = table;
    }
    
    protected void initToolbar(final Composite composite) {
        Display display = composite.getDisplay();
        ClassLoader loader = getClass().getClassLoader();

        imgRefresh = new Image(display, loader.getResourceAsStream("toolbar/refresh.png"));
        imgAdd = new Image(display, loader.getResourceAsStream("toolbar/add.png"));
        imgDelete = new Image(display, loader.getResourceAsStream("toolbar/delete.png"));
        imgRename = new Image(display, loader.getResourceAsStream("toolbar/clone.png")); // TODO find a better icon?
        imgSwap = new Image(display, loader.getResourceAsStream("toolbar/upload.png")); // TODO find a better icon?
        imgReload = new Image(display, loader.getResourceAsStream("toolbar/restore.png")); // TODO find a better icon?

        ToolBar toolBar = new ToolBar(composite, SWT.BORDER);

        itemRefresh = new ToolItem(toolBar, SWT.PUSH);
        itemRefresh.setImage(imgRefresh);
        itemRefresh.setText("Refresh");
        itemRefresh.setToolTipText("Refresh list of cores");
        itemRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					table.refresh();	
				} catch (SophieException e) {
					new ErrorDialog(composite.getShell(), new SophieException("Unable to refresh list of cores", e)).open();	
				}
			}
		});
        
        new ToolItem(toolBar, SWT.SEPARATOR);
        
        itemAdd = new ToolItem(toolBar, SWT.PUSH);
        itemAdd.setImage(imgAdd);
        itemAdd.setText("Add");
        itemAdd.setToolTipText("Add new core");
        itemAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				CoreAddDialog dialog = new CoreAddDialog(composite.getShell());
				dialog.open();
				if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
					return;
				}
				try {
					// TODO createCore is overloaded with additional params (schema file etc).
					CoreAdminRequest.createCore(dialog.getValueCoreName(), dialog.getValueInstanceDir(), Sophie.client);
					table.refresh();
				} catch (SolrServerException|IOException|SolrException|SophieException e) {
					new ErrorDialog(composite.getShell(), new SophieException("Unable to add new core \""+dialog.getValueCoreName()+"\" with instance dir \""+dialog.getValueInstanceDir()+"\"", e)).open();
				}					
			}
		});

        itemDelete = new ToolItem(toolBar, SWT.PUSH);
        itemDelete.setImage(imgDelete);
        itemDelete.setText("Delete");
        itemDelete.setToolTipText("Delete core");
        itemDelete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String coreName = table.getSelectedCore();
		        MessageBox messageBox = new MessageBox(composite.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		        messageBox.setText("Delete core");
		        messageBox.setMessage("Do you really want to delete this core (\""+coreName+"\") ?");
		        int response = messageBox.open();
		        if (response == SWT.YES) {
					try {
						CoreAdminRequest.unloadCore(coreName, Sophie.client);
						table.refresh();
					} catch (SolrServerException|IOException|SolrException|SophieException e) {
						new ErrorDialog(composite.getShell(), new SophieException("Unable to delete core \""+coreName+"\"", e)).open();
					}
		        }
			}
		});
        itemDelete.setEnabled(false);

        itemRename = new ToolItem(toolBar, SWT.PUSH);
        itemRename.setImage(imgRename);
        itemRename.setText("Rename");
        itemRename.setToolTipText("Rename core");
        itemRename.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String oldCoreName = table.getSelectedCore();
				InputDialog newCoreName = new InputDialog(composite.getShell(), "Rename core", "New name (\""+oldCoreName+"\"):", oldCoreName, null);
	    		newCoreName.open();
	    		if (newCoreName.getReturnCode() != IDialogConstants.OK_ID) {
	    			return;
	    		}
    			try {
    				CoreAdminRequest.renameCore(oldCoreName, newCoreName.getValue(), Sophie.client);
    				table.refresh();
				} catch (SolrServerException|IOException|SolrException|SophieException e) {
					new ErrorDialog(composite.getShell(), new SophieException("Unable to rename core \""+oldCoreName+"\" into \""+newCoreName+"\"", e)).open();
    			}
			}
		});
        itemRename.setEnabled(false);
        
        itemSwap = new ToolItem(toolBar, SWT.PUSH);
        itemSwap.setImage(imgSwap);
        itemSwap.setText("Swap");
        itemSwap.setToolTipText("Swap cores");
        itemSwap.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String coreName = table.getSelectedCore();
				CoreSwapDialog dialog = new CoreSwapDialog(composite.getShell(), coreName);
				dialog.open();
				if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
					return;
				}
				// TODO contrib CoreAdminRequest.swapCores() - similar to CoreAdminRequest.renameCore().
				CoreAdminRequest request = new CoreAdminRequest();
				request.setCoreName(coreName);
				request.setOtherCoreName(dialog.getValue());
				request.setAction(CoreAdminAction.SWAP);
				try {
					request.process(Sophie.client);
					table.refresh();
				} catch (SolrServerException|IOException|SolrException|SophieException e) {
					new ErrorDialog(composite.getShell(), new SophieException("Unable to swap cores \""+coreName+"\" and \""+dialog.getValue()+"\"", e)).open();
				}
			}
		});
        itemSwap.setEnabled(false);
        
        itemReload = new ToolItem(toolBar, SWT.PUSH);
        itemReload.setImage(imgReload);
        itemReload.setText("Reload");
        itemReload.setToolTipText("Reload core - this will load any configuration changes you may have made to solrconfig.xml or schema.xml"); //TODO disable when no core selected
        itemReload.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String coreName = table.getSelectedCore();
				try {
					CoreAdminRequest.reloadCore(coreName, Sophie.client);
					table.refresh();
				} catch (SolrServerException|IOException|SolrException|SophieException e) {
					new ErrorDialog(composite.getShell(), new SophieException("Unable to reload core \""+coreName+"\"", e)).open();
				}
			}
		});
        itemReload.setEnabled(false);
        
        toolBar.pack();
    }
    
    @Override
    public void finalize() {
        imgRefresh.dispose();
    	imgAdd.dispose();
    	imgDelete.dispose();
    	imgRename.dispose();
    	imgSwap.dispose();
    	imgReload.dispose();
    }

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// Do nothing.
	}

	/**
	 * Toggle buttons that require a core to be selected.
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		itemDelete.setEnabled(true);
		itemRename.setEnabled(true);
		itemSwap.setEnabled(true);
		itemReload.setEnabled(true);
		// TODO click 'Reload' then no row selected but buttons are still enabled
	}
	
}
