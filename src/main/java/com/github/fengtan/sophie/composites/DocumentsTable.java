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
package com.github.fengtan.sophie.composites;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.luke.FieldFlag;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.sophie.Sophie;
import com.github.fengtan.sophie.beans.Config;
import com.github.fengtan.sophie.beans.SolrUtils;
import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.dialogs.EditDateValueDialog;
import com.github.fengtan.sophie.dialogs.EditListValueDialog;
import com.github.fengtan.sophie.dialogs.EditTextValueDialog;
import com.github.fengtan.sophie.dialogs.EditValueDialog;
import com.github.fengtan.sophie.dialogs.ExceptionDialog;
import com.github.fengtan.sophie.toolbars.ChangeListener;

/**
 * Table listing Solr documents.
 */
public class DocumentsTable {

    /**
     * Yellow - color of modified documents.
     */
    private static final Color YELLOW = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);

    /**
     * Red - color of deleted documents.
     */
    private static final Color RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);

    /**
     * Green - color of added documents.
     */
    private static final Color GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);

    /**
     * Label displayed in lieu of the value when the field is not stored.
     */
    private static final String LABEL_NOT_STORED = "(not stored)";

    /**
     * Label displayed then the document has no value for a specific field.
     */
    private static final String LABEL_EMPTY = "(empty)";

    /**
     * Page size - how many documents do we fetch from Solr at a time.
     */
    private static final int PAGE_SIZE = Config.getDocumentsPageSize();

    /**
     * Facet limit - how many facets values do we display at most in each
     * filter.
     */
    private static final int FACET_LIMIT = Config.getDocumentsFacetsLimit();

    /**
     * Boost assigned to new field values.
     */
    private static final float BOOST = 1.0f;

    /**
     * Cached documents fetched from the server, keyed by page ID.
     */
    private Map<Integer, SolrDocumentList> pages;

    /**
     * Current active filters (filter values keyed by filter name).
     */
    private Map<String, String> filters = new HashMap<String, String>();

    /**
     * Unique key field name.
     */
    private String uniqueField;

    /**
     * Name of the column currently sorted.
     */
    private String sortField;

    /**
     * Whether the table is currently sorted in ascending or descending order.
     */
    private ORDER sortOrder = ORDER.asc;

    /**
     * List of documents locally updated.
     */
    private List<SolrDocument> documentsUpdated;

    /**
     * List of documents locally deleted.
     */
    private List<SolrDocument> documentsDeleted;

    /**
     * List of documents locally added.
     */
    private List<SolrDocument> documentsAdded;

    /**
     * Change listener - allows the 'Upload' button to detect whether there are
     * local changes to send to Solr.
     */
    private ChangeListener changeListener;

    /**
     * Table.
     */
    private Table table;

    /**
     * Table editor.
     */
    private TableEditor editor;

    /**
     * Parent composite.
     */
    private Composite composite;

    /**
     * Create a new table listing Solr documents.
     * 
     * @param composite
     *            Parent composite.
     * @param selectionListener
     *            Selection listener - allows certain buttons to be enabled only
     *            when a document is selected.
     * @param changeListener
     *            Change listener - allows the 'Upload' button to detect whether
     *            there are local changes to send to Solr.
     * @throws SophieException
     *             If the table could not be created.
     */
    public DocumentsTable(Composite composite, SelectionListener selectionListener, ChangeListener changeListener) throws SophieException {
        // Initialize unique key and sort field.
        // Rows always need to be sorted so locally updated documents do not end
        // up at the end of the list after the user upload them. We sort by
        // unique field by default.
        uniqueField = SolrUtils.getRemoteUniqueField();
        sortField = uniqueField;

        // Instantiate table.
        this.composite = composite;
        createTable();
        this.editor = new TableEditor(table);
        this.changeListener = changeListener;
        table.addSelectionListener(selectionListener);

        // Add first column (row #).
        TableColumn columnNumber = new TableColumn(table, SWT.LEFT);
        columnNumber.setText("#");
        columnNumber.pack();

        // Get facet values.
        List<FieldInfo> fields = SolrUtils.getRemoteFields();
        Map<String, FacetField> facets;
        try {
            facets = getRemoteFacets(fields);
        } catch (SophieException e) {
            Sophie.log.warn("Unable to refresh filters", e);
            facets = Collections.emptyMap();
        }

        // Sort fields by field names.
        Collections.sort(fields, new Comparator<FieldInfo>() {
            @Override
            public int compare(FieldInfo field1, FieldInfo field2) {
                String name1 = (field1 == null) ? StringUtils.EMPTY : field1.getName();
                String name2 = (field2 == null) ? StringUtils.EMPTY : field2.getName();
                return name1.compareTo(name2);
            }
        });

        // Add subsequent columns (fields).
        for (int i = 0; i < fields.size(); i++) {
            FieldInfo field = fields.get(i);
            FacetField facet = facets.get(field.getName());
            addField(field, facet, i + 1); // First row is used for row #.
        }

        // Initialize cache + row count.
        refresh();
    }

    /**
     * Create the Table.
     */
    private void createTable() {
        // Instantiate the table.
        int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL;
        table = new Table(composite, style);

        // Set layout.
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.grabExcessVerticalSpace = true;
        table.setLayoutData(gridData);

        // Set styles.
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        // Add KeyListener to delete documents.
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.DEL) {
                    deleteSelectedDocument();
                }
            }
        });

        // Initialize item count to 1 so we can populate the first row with
        // filters.
        table.setItemCount(1);

        // Populate subsequent rows with remote documents (virtual table).
        table.addListener(SWT.SetData, new Listener() {
            @Override
            public void handleEvent(Event event) {
                TableItem item = (TableItem) event.item;
                int rowIndex = table.indexOf(item);

                // The first line is populated by filters.
                if (rowIndex == 0) {
                    return;
                }

                SolrDocument document;
                // The last lines are populated by local additions.
                if (rowIndex >= table.getItemCount() - documentsAdded.size()) {
                    document = documentsAdded.get(documentsAdded.size() - table.getItemCount() + rowIndex);
                    item.setBackground(GREEN);
                } else {
                    try {
                        // rowIndex - 1 since the first line is used for
                        // filters.
                        document = getRemoteDocument(rowIndex - 1);
                    } catch (SophieException e) {
                        ExceptionDialog.open(composite.getShell(), new SophieException("Unable to populate table", e));
                        return;
                    }
                }

                // First column is used to show the row ID.
                item.setText(0, Integer.toString(rowIndex));

                // Subsequent columns are used to show field values.
                for (int index = 1; index < table.getColumnCount(); index++) {
                    TableColumn column = table.getColumn(index);
                    String fieldName = (String) column.getData("fieldName");
                    FieldInfo field = (FieldInfo) column.getData("field");
                    // If field is not stored, display message.
                    if (!SolrUtils.getFlags(field).contains(FieldFlag.STORED)) {
                        item.setText(index, LABEL_NOT_STORED);
                    } else {
                        Object value = document.getFieldValue(fieldName);
                        item.setText(index, value == null ? StringUtils.EMPTY : value.toString());
                    }
                }

                // Store document in item.
                item.setData("document", document);
            }
        });

        // Add doubleclick listener to edit values.
        table.addListener(SWT.MouseDoubleClick, new Listener() {
            public void handleEvent(Event event) {
                Point point = new Point(event.x, event.y);
                TableItem item = table.getItem(point);
                if (item == null) {
                    return;
                }
                // The first row is used for filters.
                if (table.indexOf(item) == 0) {
                    return;
                }
                // We add 1 since the first column is used for row ID's.
                for (int i = 1; i < table.getColumnCount(); i++) {
                    Rectangle rect = item.getBounds(i);
                    if (rect.contains(point)) {
                        SolrDocument document = (SolrDocument) item.getData("document");
                        String fieldName = (String) table.getColumn(i).getData("fieldName");
                        FieldInfo field = (FieldInfo) table.getColumn(i).getData("field");
                        Object defaultValue = document.getFieldValue(fieldName);
                        // Add editor dialog:
                        // - list widget if we are dealing with a multi-valued
                        // field.
                        // - datepicker if we field type contains "date".
                        // - text if we are dealing with any other field type.
                        EditValueDialog dialog;
                        if (SolrUtils.getFlags(field).contains(FieldFlag.MULTI_VALUED)) {
                            dialog = new EditListValueDialog(composite.getShell(), (AbstractList<?>) defaultValue);
                        } else if (StringUtils.containsIgnoreCase(field.getType(), "date")) {
                            dialog = new EditDateValueDialog(composite.getShell(), (Date) defaultValue);
                        } else {
                            String oldValueString = Objects.toString(defaultValue, StringUtils.EMPTY);
                            dialog = new EditTextValueDialog(composite.getShell(), oldValueString);
                        }
                        dialog.open();
                        if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
                            return;
                        }
                        Object value = dialog.getValue();
                        if (!Objects.equals(defaultValue, value)) {
                            updateDocument(item, i, value);
                        }
                    }
                }
            }
        });
    }

    /**
     * Get field names.
     * 
     * @return List of field names.
     */
    public List<String> getFieldNames() {
        List<String> fieldNames = new ArrayList<String>();
        for (int i = 0; i < table.getColumnCount(); i++) {
            String fieldName = (String) table.getColumn(i).getData("fieldName");
            // Field name is empty in the first column (row #).
            if (StringUtils.isNotEmpty(fieldName)) {
                fieldNames.add(fieldName);
            }
        }
        return fieldNames;
    }

    /**
     * Add (if value is not empty) or remove (if value is empty) a filter.
     * 
     * @param fieldName
     *            Field name.
     * @param value
     *            Filter value.
     */
    private void updateFilters(String fieldName, String value) {
        if (StringUtils.isEmpty(value)) {
            filters.remove(fieldName);
        } else {
            filters.put(fieldName, value);
        }
    }

    /**
     * Get how many documents are in the remote index.
     * 
     * @return How many documents are in the remote index.
     * @throws SophieException
     *             If the number of documents could not be determined.
     */
    private int getRemoteCount() throws SophieException {
        SolrQuery query = getBaseQuery(0, 0);
        try {
            // Solr returns a long, table expects an int.
            long count = Sophie.client.query(query).getResults().getNumFound();
            return Integer.parseInt(String.valueOf(count));
        } catch (SolrServerException | IOException | SolrException e) {
            throw new SophieException("Unable to count remote documents", e);
        }
    }

    /**
     * Get a list of remote facets keyed by field name.
     * 
     * @param fields
     *            Fields.
     * @return List of facets keyed by field name.
     * @throws SophieException
     *             If facets could not be fetched.
     */
    private Map<String, FacetField> getRemoteFacets(List<FieldInfo> fields) throws SophieException {
        // Prepare query.
        SolrQuery query = getBaseQuery(0, 0);
        query.setFacet(true);
        query.setFacetSort("index");
        query.setFacetLimit(FACET_LIMIT);
        query.setFacetMissing(true);

        // For each field, determine whether Solr can generate a facet (fq works
        // only on indexed fields). If yes, then list that field in the query.
        for (FieldInfo field : fields) {
            if (SolrUtils.getFlags(field).contains(FieldFlag.INDEXED)) {
                query.addFacetField(field.getName());
            }
        }

        // Send query.
        Map<String, FacetField> facets = new HashMap<String, FacetField>();
        try {
            for (FacetField facet : Sophie.client.query(query).getFacetFields()) {
                facets.put(facet.getName(), facet);
            }
        } catch (SolrServerException | IOException | SolrException e) {
            throw new SophieException("Unable to fetch remote facets", e);
        }

        // Return facets keyed by field name.
        return facets;
    }

    /**
     * Get document to display at a specific row index. The document is
     * retrieved from the cache. If it is not in the cache, then we fetch a new
     * page of results and we populate the cache.
     * 
     * @param rowIndex
     *            Row index.
     * @return Solr document.
     * @throws SophieException
     *             If the document could not be fetched from Solr.
     */
    private SolrDocument getRemoteDocument(int rowIndex) throws SophieException {
        // Compute page ID.
        int page = rowIndex / PAGE_SIZE;

        // If page is not in the cache, then fetch it from Solr and populate the
        // cache.
        if (!pages.containsKey(page)) {
            SolrQuery query = getBaseQuery(page * PAGE_SIZE, PAGE_SIZE);
            query.setSort(sortField, sortOrder);
            try {
                pages.put(page, Sophie.client.query(query).getResults());
            } catch (SolrServerException | IOException | SolrException e) {
                throw new SophieException("Unable to fetch remote document " + rowIndex, e);
            }
        }

        // Return Solr document from the cache.
        return pages.get(page).get(rowIndex % PAGE_SIZE);
    }

    /**
     * Get select query and populate the start/rows/fq parameters.
     * 
     * @param start
     *            Offset at which Solr should being returning documents.
     * @param rows
     *            How many rows Solr should return.
     * @return Solr query.
     */
    private SolrQuery getBaseQuery(int start, int rows) {
        SolrQuery query = new SolrQuery("*:*");
        query.setStart(start);
        query.setRows(rows);
        // Add filters.
        for (Entry<String, String> filter : filters.entrySet()) {
            if (StringUtils.equals(filter.getValue(), LABEL_EMPTY)) {
                // Empty value needs a special syntax.
                query.addFilterQuery("-" + filter.getKey() + ":[* TO *]");
            } else {
                // Colons in value need to be escaped to avoid a syntax error.
                query.addFilterQuery(filter.getKey() + ":" + filter.getValue().replace(":", "\\:"));
            }
        }
        return query;
    }

    /**
     * Get the currently selected document.
     * 
     * @return Currently selected document, or null if not row is selected.
     */
    public SolrDocument getSelectedDocument() {
        TableItem[] items = table.getSelection();
        if (items.length == 0) {
            return null;
        }
        return (SolrDocument) items[0].getData("document");
    }

    /**
     * Clear table and flush internal cache. This causes the table to be
     * re-populated with remote documents.
     * 
     * @throws SophieException
     *             If the documents could not be fetched from Solr.
     */
    public void refresh() throws SophieException {
        // Flush cache.
        documentsUpdated = new ArrayList<SolrDocument>();
        documentsDeleted = new ArrayList<SolrDocument>();
        documentsAdded = new ArrayList<SolrDocument>();
        pages = new HashMap<Integer, SolrDocumentList>();
        changeListener.unchanged();

        // Clear table.
        try {
            // First row is for filters, the rest is for documents (remote +
            // locally added - though no local addition since we have just
            // refreshed documents).
            table.setItemCount(1 + getRemoteCount());
            table.clearAll();
        } catch (SophieException e) {
            table.setItemCount(1);
            table.clearAll();
            throw new SophieException("Unable to refresh documents from Solr server", e);
        }
    }

    /**
     * Export documents into CSV file.
     * 
     * @throws SophieException
     *             If the documents could not be exported into a CSV file.
     */
    public void export() throws SophieException {
        // Open dialog to let the user select where the file will be dumped.
        FileDialog dialog = new FileDialog(table.getShell(), SWT.SAVE);
        dialog.setFilterNames(new String[] { "CSV Files (*.csv)", "All Files (*.*)" });
        dialog.setFilterExtensions(new String[] { "*.csv", "*.*" });
        String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm").format(new Date());
        dialog.setFileName("documents_" + date + ".csv");
        String path = dialog.open();

        // User did not selected any location.
        if (path == null) {
            return;
        }

        // Open file for writing.
        Writer writer = null;
        try {
            writer = new PrintWriter(path, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            IOUtils.closeQuietly(writer);
            throw new SophieException("Unable to open file for writing: " + path, e);
        }

        // We buffer rows using pages to avoid filling up the RAM.
        for (int page = 0; page <= table.getItemCount() / PAGE_SIZE; page++) {
            // Get page from Solr.
            SolrQuery query = getBaseQuery(page * PAGE_SIZE, PAGE_SIZE);
            QueryRequest request = new QueryRequest(query);
            request.setResponseParser(new NoOpResponseParser("csv"));
            NamedList<Object> response;
            try {
                response = Sophie.client.request(request);
            } catch (SolrServerException | IOException | RemoteSolrException e) {
                IOUtils.closeQuietly(writer);
                throw new SophieException("Unable to get CSV documents from Solr", e);
            }

            // Write page into file.
            String csv = (String) response.get("response");
            try {
                IOUtils.write(csv, writer);
            } catch (IOException e) {
                IOUtils.closeQuietly(writer);
                throw new SophieException("Unable to write into file " + path, e);
            }
        }

        // Close file.
        try {
            writer.close();
        } catch (IOException e) {
            throw new SophieException("Unable to close file " + path, e);
        }
    }

    /**
     * Upload local changes to the Solr server.
     * 
     * @throws SophieException
     *             If the local changes could not be uploaded to the Solr
     *             server.
     */
    public void upload() throws SophieException {
        // Upload local updates.
        for (SolrDocument document : documentsUpdated) {
            SolrInputDocument input = new SolrInputDocument();
            for (String name : document.getFieldNames()) {
                input.addField(name, document.getFieldValue(name), 1.0f);
            }
            try {
                // Returned object seems to have no relevant information.
                Sophie.client.add(input);
            } catch (SolrServerException | IOException | SolrException e) {
                throw new SophieException("Unable to update document in index: " + input.toString(), e);
            }
        }
        // Upload local deletions.
        for (SolrDocument document : documentsDeleted) {
            String id = Objects.toString(document.getFieldValue(uniqueField), StringUtils.EMPTY);
            try {
                Sophie.client.deleteById(id);
            } catch (SolrServerException | IOException | SolrException e) {
                throw new SophieException("Unable to delete document from index: " + id, e);
            }
        }
        // Upload local additions.
        for (SolrDocument document : documentsAdded) {
            SolrInputDocument input = new SolrInputDocument();
            for (String name : document.getFieldNames()) {
                input.addField(name, document.getFieldValue(name), BOOST);
            }
            try {
                // Returned object seems to have no relevant information.
                Sophie.client.add(input);
            } catch (SolrServerException | IOException | SolrException e) {
                throw new SophieException("Unable to add document to index: " + input.toString(), e);
            }
        }
        // Commit the index.
        try {
            Sophie.client.commit();
        } catch (SolrServerException | IOException | SolrException e) {
            throw new SophieException("Unable to commit index", e);
        }

        // Refresh so user can see the new state of the server.
        refresh();
    }

    /**
     * Restore index from a backup
     * 
     * @param backupName
     *            Backup file name.
     * @throws SophieException
     *             If the backup could not be restored.
     */
    public void restore(String backupName) throws SophieException {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("command", "restore");
        params.set("name", backupName);
        QueryRequest request = new QueryRequest(params);
        request.setPath("/replication");
        try {
            Sophie.client.request(request);
            refresh();
        } catch (SolrServerException | IOException | SolrException e) {
            throw new SophieException("Unable to restore backup \"" + backupName + "\"", e);
        }
        refresh();
    }

    /**
     * Update a document locally.
     * 
     * @param item
     *            Row containing the document to update.
     * @param columnIndex
     *            Column index of the field to update.
     * @param newValue
     *            New value.
     */
    private void updateDocument(TableItem item, int columnIndex, Object newValue) {
        SolrDocument document = (SolrDocument) item.getData("document");
        // The row may not contain any document (e.g. the first row, which
        // contains the filters).
        if (document == null) {
            return;
        }

        String fieldName = (String) table.getColumn(columnIndex).getData("fieldName");
        // We reduce by 1 since the first column is used for row ID.
        document.setField(fieldName, newValue);
        item.setText(columnIndex, Objects.toString(newValue, StringUtils.EMPTY));
        changeListener.changed();

        // If document was locally added, then leave it in documentsAdded and
        // return so it remains green.
        if (documentsAdded.contains(document)) {
            return;
        }

        // If document was locally deleted, then remove it from documentsDeleted
        // and let it go into documentsUpdated.
        if (documentsDeleted.contains(document)) {
            documentsDeleted.remove(document);
        }

        // Add document to documentsUpdated if it is not already there.
        if (!documentsUpdated.contains(document)) {
            documentsUpdated.add(document);
        }
        item.setBackground(YELLOW);
    }

    /**
     * Delete the selected document locally.
     * 
     * @param item
     *            Row containing the document to delete.
     */
    public void deleteSelectedDocument() {
        SolrDocument document = getSelectedDocument();
        // The row may not contain any document (e.g. the first row, which
        // contains the filters).
        if (document == null) {
            return;
        }

        // If document is already deleted locally, then do nothing.
        if (documentsDeleted.contains(document)) {
            return;
        }

        int rowIndex = table.getSelectionIndex();
        // If document was locally added, then just remove it from local
        // additions.
        if (documentsAdded.contains(document)) {
            documentsAdded.remove(document);
            table.remove(rowIndex);
            changeListener.changed();
            return;
        }

        // If document was locally updated, then remove it from documentsUpdated
        // and let it go into documentsDeleted.
        if (documentsUpdated.contains(document)) {
            documentsUpdated.remove(document);
        }

        // Remove document.
        documentsDeleted.add(document);
        table.getItem(rowIndex).setBackground(RED);
        changeListener.changed();
    }

    /**
     * Add a document locally.
     * 
     * @param document
     *            The new document.
     */
    public void addDocument(SolrDocument document) {
        documentsAdded.add(document);
        table.setItemCount(table.getItemCount() + 1);
        // Scroll to the bottom of the table so we reveal the new document.
        table.setTopIndex(table.getItemCount() - 1);
        changeListener.changed();
    }

    /**
     * Add a new (custom) column to the table.
     * 
     * This is used for dynamic fields, where field name may be "ss_*" but we
     * want the column name to be "ss_foobar".
     * 
     * @param fieldName
     *            New field name.
     * @param fieldInfo
     *            New field definition.
     */
    private void addColumn(final String fieldName, FieldInfo fieldInfo) {
        final TableColumn column = new TableColumn(table, SWT.LEFT);
        // Add space padding so we can see the sort signifier.
        final boolean isFieldSortable = SolrUtils.isFieldSortable(fieldInfo);
        column.setText(fieldName + (isFieldSortable ? "     " : " " + Sophie.SIGNIFIER_UNSORTABLE));
        column.setData("field", fieldInfo);
        column.setData("fieldName", fieldName);
        if (!isFieldSortable) {
            column.setToolTipText("Cannot sort on a field that is not indexed, is multivalued or has doc values");
        }
        // Sort column when click on the header
        column.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                if (!isFieldSortable) {
                    return;
                }
                // Clicking on the current sort field toggles the direction.
                // Clicking on a new field changes the sort field.
                if (StringUtils.equals(sortField, fieldName)) {
                    sortOrder = ORDER.asc.equals(sortOrder) ? ORDER.desc : ORDER.asc;
                } else {
                    sortField = fieldName;
                }
                // Clear signifier on all columns, add signifier on sorted
                // column.
                char signifier = ORDER.asc.equals(sortOrder) ? Sophie.SIGNIFIER_SORTED_ASC : Sophie.SIGNIFIER_SORTED_DESC;
                for (TableColumn c : table.getColumns()) {
                    String columnFieldName = (String) c.getData("fieldName");
                    FieldInfo columnField = (FieldInfo) c.getData("field");
                    if (columnFieldName != null && columnField != null) {
                        if (!SolrUtils.isFieldSortable(columnField)) {
                            c.setText(columnFieldName + " " + Sophie.SIGNIFIER_UNSORTABLE);
                        } else {
                            c.setText(columnFieldName + ((column == c) ? " " + signifier : StringUtils.EMPTY));
                        }
                    }

                }
                // Re-populate table.
                try {
                    refresh();
                } catch (SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to refresh documents from Solr server", e));
                }
            }
        });
        column.pack();
    }

    /**
     * Add a filter (combo).
     * 
     * @param fieldName
     *            Field name.
     * @param field
     *            Field definition.
     * @param facet
     *            Facet - the values will be used to populate the values of the
     *            combo.
     * @param index
     *            Column index.
     */
    private void addCombo(String fieldName, FieldInfo field, final FacetField facet, int index) {
        final CCombo combo = new CCombo(table, SWT.BORDER);
        combo.add(StringUtils.EMPTY);
        // If the number of facet values is the max, then the list of facet
        // values might not be complete. Hence we use a free text field instead
        // of populating the combo.
        if (facet.getValueCount() < FACET_LIMIT) {
            for (Count count : facet.getValues()) {
                combo.add(Objects.toString(count.getName(), LABEL_EMPTY) + " (" + count.getCount() + ")");
            }
            combo.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    // Extract original value (e.g. "Foobar") from widget value
                    // (e.g. "Foobar (3)")
                    Pattern pattern = Pattern.compile("^(.*) \\([0-9]+\\)$");
                    Matcher matcher = pattern.matcher(combo.getText());
                    // If we found the original value, then populate the combo.
                    // Otherwise, log a warning message.
                    if (matcher.find()) {
                        combo.setText(matcher.group(1));
                    } else {
                        Sophie.log.warn("Unable to extract original value from \"" + combo.getText() + "\"");
                    }

                };
            });
        } else {
            combo.add(LABEL_EMPTY);
        }

        // Fire filters + refresh when user selects a value.
        combo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                String filterName = facet.getName();
                String filterNewValue = combo.getText();
                String filterOldValue = filters.get(filterName);
                // No need to re-send a request to Solr and the user selected
                // the same filter as the current filter.
                if (StringUtils.equals(filterOldValue, filterNewValue) || (StringUtils.isBlank(filterOldValue) && StringUtils.isBlank(filterNewValue))) {
                    return;
                }
                updateFilters(filterName, filterNewValue);
                try {
                    refresh();
                } catch (SophieException e) {
                    ExceptionDialog.open(composite.getShell(), new SophieException("Unable to refresh documents from Solr server", e));
                }
            }
        });

        // Filter (refresh) results when user hits "Enter" while editing one of
        // the combos.
        combo.addListener(SWT.KeyUp, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.character == SWT.CR) {
                    updateFilters(facet.getName(), combo.getText());
                    try {
                        refresh();
                    } catch (SophieException e) {
                        ExceptionDialog.open(composite.getShell(), new SophieException("Unable to refresh documents from Solr server", e));
                    }
                }
            }
        });
        if (combo != null) {
            editor.grabHorizontal = true;
            editor.setEditor(combo, table.getItem(0), index);
            editor = new TableEditor(table);
        }
    }

    /**
     * Add a new field to the table.
     * 
     * @param fieldName
     *            Field name.
     * @param field
     *            Field definition.
     * @param facet
     *            Facet - the values will be used to populate the values of the
     *            combo.
     * @param index
     *            Column index.
     */
    private void addField(String fieldName, FieldInfo field, FacetField facet, int index) {
        addColumn(fieldName, field);
        // If field or facet is null then we cannot filter on this field (e.g.
        // the field is not indexed).
        if (fieldName != null && field != null && facet != null) {
            addCombo(fieldName, field, facet, index);
        }
    }

    /**
     * Add a new field to the table. The field name is deduced from the field
     * definition.
     * 
     * @param field
     *            Field definition.
     * @param facet
     *            Facet - the values will be used to populate the values of the
     *            combo.
     * @param index
     *            Column index.
     */
    private void addField(FieldInfo field, FacetField facet, int index) {
        addField(field.getName(), field, facet, index);
    }

    /**
     * Add new field to the table. No values are provided to the filter.
     * 
     * @param fieldName
     *            Field name.
     * @param field
     *            Field definition.
     */
    public void addField(String fieldName, FieldInfo field) {
        FacetField facet = new FacetField(fieldName);
        int index = table.getColumnCount();
        addField(fieldName, field, facet, index);
    }

}
