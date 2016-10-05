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
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.ModifiableSolrParams;
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
import com.github.fengtan.sophie.dialogs.ExceptionDialog;
import com.github.fengtan.sophie.tables.DocumentsTable;
import com.github.fengtan.sophie.validators.FieldValidator;

/**
 * Toolbar to make operations on documents.
 */
public class DocumentsToolbar implements SelectionListener, ChangeListener {

    /**
     * Refresh operation - image.
     */
    private Image imgRefresh;

    /**
     * Add document operation - image.
     */
    private Image imgAdd;

    /**
     * Delete document operation - image.
     */
    private Image imgDelete;

    /**
     * Clone document operation - image.
     */
    private Image imgClone;

    /**
     * Upload local modifications operation - image.
     */
    private Image imgUpload;

    /**
     * Add field operation - image.
     */
    private Image imgAddField;
    
    /**
     * Clear index operation - image.
     */
    private Image imgClear;

    /**
     * Commit index operation - image.
     */
    private Image imgCommit;

    /**
     * Optimize index operation - image.
     */
    private Image imgOptimize;

    /**
     * Export operation - image.
     */
    private Image imgExport;

    /**
     * Backup operation - image.
     */
    private Image imgBackup;

    /**
     * Restore operation - image.
     */
    private Image imgRestore;

    /**
     * Refresh operation - button.
     */
    private ToolItem itemRefresh;

    /**
     * Add document operation - button.
     */
    private ToolItem itemAdd;

    /**
     * Delete document operation - button.
     */
    private ToolItem itemDelete;

    /**
     * Clone document operation - button.
     */
    private ToolItem itemClone;

    /**
     * Upload local modifications operation - button.
     */
    private ToolItem itemUpload;

    /**
     * Add field operation - button.
     */
    private ToolItem itemAddField;
    
    /**
     * Clear index operation - button.
     */
    private ToolItem itemClear;

    /**
     * Commit index operation - button.
     */
    private ToolItem itemCommit;

    /**
     * Optimize index operation - button.
     */
    private ToolItem itemOptimize;

    /**
     * Export operation - button.
     */
    private ToolItem itemExport;

    /**
     * Backup operation - button.
     */
    private ToolItem itemBackup;

    /**
     * Restore operation - button.
     */
    private ToolItem itemRestore;

    /**
     * Table listing the documents.
     */
    private DocumentsTable table;

    /**
     * Create a new toolbar to make operations on documents.
     * 
     * @param composite
     *            Parent composite.
     */
    public DocumentsToolbar(Composite composite) {
        initToolbar(composite);
    }

    /**
     * Set table.
     * 
     * @param table
     *            Table.
     */
    public void setTable(DocumentsTable table) {
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
        imgClone = new Image(display, loader.getResourceAsStream("toolbar/clone.png"));
        imgUpload = new Image(display, loader.getResourceAsStream("toolbar/upload.png"));
        imgAddField = new Image(display, loader.getResourceAsStream("toolbar/add_field.png"));
        imgClear = new Image(display, loader.getResourceAsStream("toolbar/clear.png"));
        imgCommit = new Image(display, loader.getResourceAsStream("toolbar/commit.png"));
        imgOptimize = new Image(display, loader.getResourceAsStream("toolbar/optimize.png"));
        imgExport = new Image(display, loader.getResourceAsStream("toolbar/export.png"));
        imgBackup = new Image(display, loader.getResourceAsStream("toolbar/backup.png"));
        imgRestore = new Image(display, loader.getResourceAsStream("toolbar/restore.png"));

        // Instantiate buttons.
        itemRefresh = new ToolItem(toolBar, SWT.PUSH);
        itemRefresh.setImage(imgRefresh);
        itemRefresh.setText("Refresh");
        itemRefresh.setToolTipText("Refresh from Solr: this will wipe out local modifications");
        itemRefresh.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                try {
                    table.refresh();
                } catch (SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to refresh documents from Solr server", e));
                }
            }
        });

        new ToolItem(toolBar, SWT.SEPARATOR);

        itemAdd = new ToolItem(toolBar, SWT.PUSH);
        itemAdd.setImage(imgAdd);
        itemAdd.setText("Add");
        itemAdd.setToolTipText("Add new document");
        itemAdd.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                table.addDocument(new SolrDocument());
            }
        });

        itemDelete = new ToolItem(toolBar, SWT.PUSH);
        itemDelete.setImage(imgDelete);
        itemDelete.setText("Delete");
        itemDelete.setToolTipText("Delete document");
        itemDelete.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                table.deleteSelectedDocument();
            }
        });
        itemDelete.setEnabled(false);

        itemClone = new ToolItem(toolBar, SWT.PUSH);
        itemClone.setImage(imgClone);
        itemClone.setText("Clone");
        itemClone.setToolTipText("Clone document");
        itemClone.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                SolrDocument document = table.getSelectedDocument();
                if (document == null) {
                    return;
                }
                // Unset the unique key field so we don't have two rows describing the
                // same Solr document.
                // TODO what if field "id" does not exist ?+ should use uniqueKey
                document.removeFields("id");
                table.addDocument(document);
            }
        });
        itemClone.setEnabled(false);

        itemUpload = new ToolItem(toolBar, SWT.PUSH);
        itemUpload.setImage(imgUpload);
        itemUpload.setText("Upload");
        itemUpload.setToolTipText("Upload local modifications to Solr");
        itemUpload.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                try {
                    table.upload();
                } catch (SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to upload local modifications to Solr", e));
                }
            }
        });
        itemUpload.setEnabled(false);

        new ToolItem(toolBar, SWT.SEPARATOR);

        itemAddField = new ToolItem(toolBar, SWT.PUSH);
        itemAddField.setImage(imgAddField);
        itemAddField.setText("Add field");
        itemAddField.setToolTipText("Add new field");
        itemAddField.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                // Get field schema fields.
                Map<String, FieldInfo> fields;
                try {
                    fields = SolrUtils.getRemoteSchemaFields();
                } catch (SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to fetch schema fields", e));
                    return;
                }
                // Remove universal pattern which is not useful and will match
                // any field name.
                fields.remove("*");
                // Remove fields already displayed in the table.
                for (String existingFieldName : table.getFieldNames()) {
                    fields.remove(existingFieldName);
                }
                // Extract and sort field names.
                Set<String> fieldNames = fields.keySet();
                String[] fieldNamesArray = new String[fieldNames.size()];
                fieldNames.toArray(fieldNamesArray);
                Arrays.sort(fieldNamesArray);
                // Prompt user for new field name.
                FieldValidator validator = new FieldValidator(fields);
                // TODO validator ensure user does not add a fields already
                // displayed in table.
                CComboDialog dialog = new CComboDialog(composite.getShell(), "Add new field", "Field name:", fieldNamesArray, validator);
                dialog.open();
                if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
                    return;
                }
                // Add new field.
                String fieldName = dialog.getValue();
                FieldInfo field = validator.getMatchingField(fieldName);
                table.addField(fieldName, field);
            }
        });
        
        itemClear = new ToolItem(toolBar, SWT.PUSH);
        itemClear.setImage(imgClear);
        itemClear.setText("Clear");
        itemClear.setToolTipText("Clear index");
        itemClear.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                MessageBox messageBox = new MessageBox(composite.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Clear index");
                messageBox.setMessage("Do you really want to clear the index? This will remove all documents from the index.");
                int response = messageBox.open();
                if (response == SWT.YES) {
                    try {
                        Sophie.client.deleteByQuery("*:*");
                        Sophie.client.commit();
                        table.refresh();
                    } catch (SolrServerException | IOException | SolrException | SophieException e) {
                        ExceptionDialog.open(composite.getShell(), new SophieException("Unable to clear index", e));
                    }
                }
            }
        });

        itemCommit = new ToolItem(toolBar, SWT.PUSH);
        itemCommit.setImage(imgCommit);
        itemCommit.setText("Commit");
        itemCommit.setToolTipText("Commit index");
        itemCommit.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                try {
                    Sophie.client.commit();
                    table.refresh();
                } catch (SolrServerException | IOException | SolrException | SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to commit index", e));
                }
            }
        });

        itemOptimize = new ToolItem(toolBar, SWT.PUSH);
        itemOptimize.setImage(imgOptimize);
        itemOptimize.setText("Optimize");
        itemOptimize.setToolTipText("Optimize index");
        itemOptimize.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                MessageBox messageBox = new MessageBox(composite.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messageBox.setText("Optimize index");
                messageBox.setMessage("Do you really want to optimize the index? If the index is highly segmented, this may take several hours and will slow down requests.");
                int response = messageBox.open();
                if (response == SWT.YES) {
                    try {
                        Sophie.client.optimize();
                        // Optimizing drops obsolete documents, obsolete facet
                        // values, etc so we need to refresh the table.
                        table.refresh();
                    } catch (SolrServerException | IOException | SolrException | SophieException e) {
                        ExceptionDialog.open(composite.getShell(), new SophieException("Unable to optimize index", e));
                    }
                }
            }
        });

        new ToolItem(toolBar, SWT.SEPARATOR);

        itemExport = new ToolItem(toolBar, SWT.PUSH);
        itemExport.setImage(imgExport);
        itemExport.setText("Export");
        itemExport.setToolTipText("Export as CSV file");
        itemExport.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                try {
                    table.export();
                } catch (SophieException e) {
                    ExceptionDialog.open(composite.getShell(), e);
                }
            }
        });

        itemBackup = new ToolItem(toolBar, SWT.PUSH);
        itemBackup.setImage(imgBackup);
        itemBackup.setText("Backup");
        itemBackup.setToolTipText("Make a backup of the index");
        itemBackup.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                // TODO
                // "Leave empty to use the default format (yyyyMMddHHmmssSSS)."
                InputDialog dialog = new InputDialog(composite.getShell(), "Make a backup of the index", "Backup name:", null, null);
                dialog.open();
                if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
                    return;
                }
                String backupName = dialog.getValue();
                ModifiableSolrParams params = new ModifiableSolrParams();
                params.set("command", "backup");
                params.set("name", backupName);
                QueryRequest request = new QueryRequest(params);
                request.setPath("/replication");
                try {
                    NamedList<Object> response = Sophie.client.request(request);
                    // TODO progress bar with /replication?command=details ?
                    // TODO "OK" solrj constant ?
                    if (StringUtils.equals(response.get("status").toString(), "OK")) {
                        // TODO notify user OK + name/place of backup file
                    } else {
                        // TODO notify user NOK + exception details
                    }
                } catch (SolrServerException | IOException | SolrException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to create backup \"" + backupName + "\"", e));
                }
            }
        });

        itemRestore = new ToolItem(toolBar, SWT.PUSH);
        itemRestore.setImage(imgRestore);
        itemRestore.setText("Restore");
        itemRestore.setToolTipText("Restore index from a backup");
        itemRestore.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                // TODO "Available backup names"
                // TODO "leave empty for xxx"
                InputDialog dialog = new InputDialog(composite.getShell(), "Restore index from a backup", "Backup name:", null, null);
                dialog.open();
                if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
                    return;
                }
                try {
                    table.restore(dialog.getValue());
                } catch (SophieException e) {
                    ExceptionDialog.open(composite.getShell(), e);
                }
            }
        });

        // Pack.
        toolBar.pack();
    }

    @Override
    public void finalize() {
        imgRefresh.dispose();
        imgAdd.dispose();
        imgDelete.dispose();
        imgClone.dispose();
        imgUpload.dispose();
        imgAddField.dispose();
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

    @Override
    public void widgetSelected(SelectionEvent e) {
        // These buttons require a document to be selected.
        itemDelete.setEnabled(true);
        itemClone.setEnabled(true);
    }

    @Override
    public void changed() {
        // Enable the 'Upload' button if there is at least one local
        // modification to send to Solr.
        itemUpload.setEnabled(true);
    }

    @Override
    public void unchanged() {
        // Disable the 'Upload' button if there is no local modification to send
        // to Solr.
        itemUpload.setEnabled(false);
    }

}
