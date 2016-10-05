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
package com.github.fengtan.sophie.toolbars;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;
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
import com.github.fengtan.sophie.beans.SolrUtils;
import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.dialogs.CComboDialog;
import com.github.fengtan.sophie.dialogs.DoubleInputDialog;
import com.github.fengtan.sophie.dialogs.ExceptionDialog;
import com.github.fengtan.sophie.tables.CoresTable;

/**
 * Toolbar to make operations on cores.
 */
public class CoresToolbar implements SelectionListener {

    /**
     * Refresh operation - image.
     */
    private Image imgRefresh;

    /**
     * Add core operation - image.
     */
    private Image imgAdd;

    /**
     * Delete core operation - image.
     */
    private Image imgDelete;

    /**
     * Rename core operation - image.
     */
    private Image imgRename;

    /**
     * Swap cores operation - image.
     */
    private Image imgSwap;

    /**
     * Reload core operation - image.
     */
    private Image imgReload;

    /**
     * Reload all cores operation - image.
     */
    private Image imgReloadAll;

    /**
     * Refresh operation - button.
     */
    private ToolItem itemRefresh;

    /**
     * Add core operation - button.
     */
    private ToolItem itemAdd;

    /**
     * Delete core operation - button.
     */
    private ToolItem itemDelete;

    /**
     * Rename core operation - button.
     */
    private ToolItem itemRename;

    /**
     * Swap cores operation - button.
     */
    private ToolItem itemSwap;

    /**
     * Reload core operation - button.
     */
    private ToolItem itemReload;

    /**
     * Reload all cores operation - button.
     */
    private ToolItem itemReloadAll;

    /**
     * Table listing the cores.
     */
    private CoresTable table;

    /**
     * Create a new toolbar to make operations on cores.
     * 
     * @param composite
     *            Parent composite.
     */
    public CoresToolbar(Composite composite) {
        initToolbar(composite);
    }

    /**
     * Set table.
     * 
     * @param table
     *            Table.
     */
    public void setTable(CoresTable table) {
        this.table = table;
    }

    /**
     * Populate toolbar with buttons.
     * 
     * @param composite
     *            Parent composite.
     */
    private void initToolbar(final Composite composite) {
        Display display = composite.getDisplay();
        ClassLoader loader = getClass().getClassLoader();

        // Instantiate toolbar.
        ToolBar toolBar = new ToolBar(composite, SWT.BORDER);
        
        // Instantiate images.
        imgRefresh = new Image(display, loader.getResourceAsStream("toolbar/refresh.png"));
        imgAdd = new Image(display, loader.getResourceAsStream("toolbar/add.png"));
        imgDelete = new Image(display, loader.getResourceAsStream("toolbar/delete.png"));
        imgRename = new Image(display, loader.getResourceAsStream("toolbar/rename.png"));
        imgSwap = new Image(display, loader.getResourceAsStream("toolbar/swap.png"));
        imgReload = new Image(display, loader.getResourceAsStream("toolbar/restore.png"));
        imgReloadAll = new Image(display, loader.getResourceAsStream("toolbar/restore.png"));

        // Instantiate buttons.
        itemRefresh = new ToolItem(toolBar, SWT.PUSH);
        itemRefresh.setImage(imgRefresh);
        itemRefresh.setText("Refresh");
        itemRefresh.setToolTipText("Refresh list of cores");
        itemRefresh.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                try {
                    table.refresh();
                } catch (SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to refresh list of cores", e));
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
                DoubleInputDialog dialog = new DoubleInputDialog(composite.getShell(), "Add new core", "Name:", "collectionX", "Instance Directory:", "/path/to/solr/collectionX");
                dialog.open();
                if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
                    return;
                }
                try {
                    // TODO createCore is overloaded with additional params
                    // (schema file etc).
                    CoreAdminRequest.createCore(dialog.getValue1(), dialog.getValue2(), Sophie.client);
                    table.refresh();
                } catch (SolrServerException | IOException | SolrException | SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to add new core \"" + dialog.getValue1() + "\" with instance dir \"" + dialog.getValue2() + "\"", e));
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
                messageBox.setMessage("Do you really want to delete this core (\"" + coreName + "\") ?");
                int response = messageBox.open();
                if (response == SWT.YES) {
                    try {
                        CoreAdminRequest.unloadCore(coreName, Sophie.client);
                        table.refresh();
                    } catch (SolrServerException | IOException | SolrException | SophieException e) {
                        ExceptionDialog.open(composite.getShell(), new SophieException("Unable to delete core \"" + coreName + "\"", e));
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
                InputDialog newCoreName = new InputDialog(composite.getShell(), "Rename core", "New name (\"" + oldCoreName + "\"):", oldCoreName, null);
                newCoreName.open();
                if (newCoreName.getReturnCode() != IDialogConstants.OK_ID) {
                    return;
                }
                try {
                    CoreAdminRequest.renameCore(oldCoreName, newCoreName.getValue(), Sophie.client);
                    table.refresh();
                } catch (SolrServerException | IOException | SolrException | SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to rename core \"" + oldCoreName + "\" into \"" + newCoreName + "\"", e));
                }
            }
        });
        itemRename.setEnabled(false);

        new ToolItem(toolBar, SWT.SEPARATOR);
        
        itemSwap = new ToolItem(toolBar, SWT.PUSH);
        itemSwap.setImage(imgSwap);
        itemSwap.setText("Swap");
        itemSwap.setToolTipText("Swap cores");
        itemSwap.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                String coreName = table.getSelectedCore();
                Map<String, NamedList<Object>> cores;
                try {
                    cores = SolrUtils.getCores();
                } catch (SophieException e) {
                    Sophie.log.error("Unable to suggest list of cores", e);
                    cores = Collections.emptyMap();
                }
                Object[] coreObjects = cores.keySet().toArray();
                String[] coreStrings = Arrays.copyOf(coreObjects, coreObjects.length, String[].class);
                CComboDialog dialog = new CComboDialog(composite.getShell(), "Swap cores", "Swap core \"" + coreName + "\" with:", coreStrings, null);
                dialog.open();
                if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
                    return;
                }
                CoreAdminRequest request = new CoreAdminRequest();
                request.setCoreName(coreName);
                request.setOtherCoreName(dialog.getValue());
                request.setAction(CoreAdminAction.SWAP);
                try {
                    request.process(Sophie.client);
                    table.refresh();
                } catch (SolrServerException | IOException | SolrException | SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to swap cores \"" + coreName + "\" and \"" + dialog.getValue() + "\"", e));
                }
            }
        });
        itemSwap.setEnabled(false);

        // TODO disable when no core selected
        itemReload = new ToolItem(toolBar, SWT.PUSH);
        itemReload.setImage(imgReload);
        itemReload.setText("Reload");
        itemReload.setToolTipText("Reload core - this will reload any configuration changes you may have made to solrconfig.xml or schema.xml");
        itemReload.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                String coreName = table.getSelectedCore();
                try {
                    CoreAdminRequest.reloadCore(coreName, Sophie.client);
                    table.refresh();
                } catch (SolrServerException | IOException | SolrException | SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to reload core \"" + coreName + "\"", e));
                }
            }
        });
        itemReload.setEnabled(false);

        itemReloadAll = new ToolItem(toolBar, SWT.PUSH);
        itemReloadAll.setImage(imgReloadAll);
        itemReloadAll.setText("Reload all");
        itemReloadAll.setToolTipText("Reload all cores");
        itemReloadAll.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                try {
                    for (String coreName : SolrUtils.getCores().keySet()) {
                        CoreAdminRequest.reloadCore(coreName, Sophie.client);
                    }
                    table.refresh();
                } catch (SolrServerException | IOException | SolrException | SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to reload all cores", e));
                }
            }
        });

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
        imgReloadAll.dispose();
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        // Do nothing.
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        // These buttons require a core to be selected.
        itemDelete.setEnabled(true);
        itemRename.setEnabled(true);
        itemSwap.setEnabled(true);
        itemReload.setEnabled(true);
        // TODO click 'Reload' then no row selected but buttons are still
        // enabled
    }

}
