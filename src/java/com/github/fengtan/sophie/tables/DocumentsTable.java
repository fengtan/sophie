package com.github.fengtan.sophie.tables;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.sophie.Sophie;
import com.github.fengtan.sophie.beans.Config;
import com.github.fengtan.sophie.beans.SolrUtils;
import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.dialogs.DocumentEditDateValueDialog;
import com.github.fengtan.sophie.dialogs.DocumentEditListValueDialog;
import com.github.fengtan.sophie.dialogs.DocumentEditTextValueDialog;
import com.github.fengtan.sophie.dialogs.DocumentEditValueDialog;
import com.github.fengtan.sophie.dialogs.ErrorDialog;
import com.github.fengtan.sophie.toolbars.ChangeListener;

public class DocumentsTable { // TODO extend Composite ?

	private static final Color YELLOW = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
	private static final Color RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	private static final Color GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	
	private static final char SIGNIFIER_SORTED_ASC = '\u25B4';
	private static final char SIGNIFIER_SORTED_DESC = '\u25BE';
	private static final char SIGNIFIER_UNSORTABLE = '\u2205';
	
	private static final String LABEL_NOT_STORED = "(not stored)";
	private static final String LABEL_EMPTY = "(empty)";
	
	// How many documents do we fetch from Solr at a time.
	private static final int PAGE_SIZE = Config.getDocumentsPageSize();
	
	// How many facets values do we display at most.
	private static final int FACET_LIMIT = Config.getDocumentsFacetsLimit();
	
	private Map<Integer, SolrDocumentList> pages;
	private List<FieldInfo> fields;
	private Map<String, FacetField> facets;
	private Map<String, String> filters = new HashMap<String, String>();
	private String uniqueField; // TODO update when refresh ?()
	private String sortField; // TODO support sort on multiple fields ?
	private ORDER sortOrder = ORDER.asc;

	// List of documents locally updated/deleted/added.
	private List<SolrDocument> documentsUpdated;
	private List<SolrDocument> documentsDeleted;
	private List<SolrDocument> documentsAdded;
	
	// Change listener, allows the 'Upload' button to detect whether there are changes to send to Solr.
	private ChangeListener changeListener;
	
	private Table table;

	public DocumentsTable(Composite parent, SelectionListener selectionListener, ChangeListener changeListener) throws SophieException {
		this.fields = SolrUtils.getRemoteFields();
		this.facets = getRemoteFacets();
		this.uniqueField = SolrUtils.getRemoteUniqueField();
		this.sortField = uniqueField; // By default we sort documents by uniqueKey TODO what if uniqueKey is not sortable ?
		this.table = createTable(parent, selectionListener);
		this.changeListener = changeListener;
		// Initialize cache + row count.
		refresh();
	}
	
	/**
	 * Create the Table
	 */
	private Table createTable(final Composite parent, SelectionListener selectionListener) throws SophieException {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL;

		final Table table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addSelectionListener(selectionListener);
		
		// Add KeyListener to delete documents.
		// TODO hitting "suppr" or clicking the button (in the toolbar) a second time should remove the deletion.
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.DEL) {
					deleteSelectedDocument();
				}
			}
		});

		// Initialize item count to 1 so we can populate the first row with filters.
		table.setItemCount(1);
		table.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event event) {
	            TableItem item = (TableItem) event.item;
	            int rowIndex = table.indexOf(item);
	            // The first line is populated by filters.
	            if (rowIndex == 0) {
	            	// TODO might need to populate if existing filters
	            	return;
	            }
	            SolrDocument document;
	            // The last lines are populated by local additions.
	            if (rowIndex >= table.getItemCount() - documentsAdded.size()) {
	            	document = documentsAdded.get(documentsAdded.size() - table.getItemCount() + rowIndex);
	            	item.setBackground(GREEN);
	            } else {
		            // Use rowIndex - 1 since the first line is used for filters.
		            // TODO make sure the last document gets displayed.
	            	try {
			            document = getRemoteDocument(rowIndex - 1);	            		
	            	} catch (SophieException e) {
						new ErrorDialog(parent.getShell(), new SophieException("Unable to populate table", e)).open();
						return;
	            	}
	            }
	            // First column is used to show the row ID.
	            item.setText(0, Integer.toString(rowIndex));
	            // Subsequent columns are used to show field values.
		        for (int i=0; i<fields.size(); i++) {
		        	String fieldName = fields.get(i).getName();
		        	// If field is not stored, display message.
		        	// TODO disable doubleclick on unstored fields ?
		        	// TODO verify "(not stored)" is not sent to Solr when updating/creating a new document
		        	// TODO prepopulate if create a new document ?
					// field.getFlags() is not populated when lukeRequest.setSchema(false) so we parse flags ourselves based on field.getSchema() TODO open ticket
		        	if (!FieldInfo.parseFlags(fields.get(i).getSchema()).contains(FieldFlag.STORED)) {
		        		item.setText(i+1, LABEL_NOT_STORED);
		        	} else {
		        		Object value = document.getFieldValue(fieldName);
		            	item.setText(i+1, value == null ? StringUtils.EMPTY : value.toString());
		            }
		        }
		        // Store document in item.
		        item.setData("document", document);
		        // TODO use item.setText(String[] values) ?
		        // TODO store status insert/update/delete using item.setData() ?
			}
		});
		
		// Add columns. First one is used to show row IDs.
		TableColumn columnNumber = new TableColumn(table, SWT.LEFT);
		columnNumber.setText("#");
		columnNumber.pack(); // TODO needed ? might be worth to setLayout() to get rid of this
		for (final FieldInfo field:fields) {
			final TableColumn column = new TableColumn(table, SWT.LEFT);
			// Add space padding so we can see the sort signifier.
			// TODO set sort signifier on uniqueKey by default ?
			// TODO refactor with selection listener
			column.setText(field.getName()+(isFieldSortable(field) ? "     " : " "+SIGNIFIER_UNSORTABLE));
			column.setData("field", field);
			if (!isFieldSortable(field)) {
				column.setToolTipText("Cannot sort on a field that is not indexed, is multivalued or has doc values");
			}
			// Sort column when click on the header
			column.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					if (!isFieldSortable(field)) {
						return;
					}
					// Clicking on the current sort field toggles the direction.
					// Clicking on a new field changes the sort field.
					if (StringUtils.equals(sortField, field.getName())) {
						sortOrder = ORDER.asc.equals(sortOrder) ? ORDER.desc : ORDER.asc;
					} else {
						sortField = field.getName();
					}
					// Clear signifier on all columns, add signifier on sorted column.
					char signifier = ORDER.asc.equals(sortOrder) ? SIGNIFIER_SORTED_ASC : SIGNIFIER_SORTED_DESC;
					for (TableColumn c:table.getColumns()) {
						FieldInfo f = (FieldInfo) c.getData("field");
						if (f != null) {
							if (!isFieldSortable(f)) {
								c.setText(f.getName()+" "+SIGNIFIER_UNSORTABLE);
							} else {
								c.setText(f.getName()+((column == c) ? " "+signifier : StringUtils.EMPTY));	
							}
						}

					}
					// Re-populate table.
					try {
						refresh();
					} catch (SophieException e) {
						new ErrorDialog(parent.getShell(), new SophieException("Unable to refresh documents from Solr server", e)).open();
					}
				}
			});
			column.pack(); // TODO needed ? might be worth to setLayout() to get rid of this
		}
		
		// Add filters.
		TableItem[] items = table.getItems(); // TODO do we need to load all items ?
		TableEditor editor = new TableEditor(table);
		for(int i=0; i<fields.size(); i++) {
			// TODO check if map contains field ?
			FacetField facet = facets.get(fields.get(i).getName());
			// If facet is null then we cannot filter on this field (e.g. the field is not indexed).
			if (facet == null) {
				// TODO grey out items[0] columns i
				continue;
			}
			final Control widget;
			/*
			 * TODO could use this instead of storing field name in setData() 
			 * Point point = new Point(event.x, event.y);
		     * TableItem item = table.getItem(point);
		     * if (item == null) {
		     *   return;
		     * }
		     * for (int i=0; i<fields.size(); i++) {
		     *   Rectangle rect = item.getBounds(i);
		     *   if (rect.contains(point)) {
		     *     SolrDocument document = (SolrDocument) item.getData("document");
		     *     dialog.open(item.getText(i), fields.get(i).getName(), document);
		     *   }
		     * }
			 */
			// If the number of facet values is the max, then the list of facet values might not be complete. Hence we use a free text field instead of populating the combo.
			if (facet.getValueCount() < FACET_LIMIT) {
				final CCombo combo = new CCombo(table, SWT.BORDER);
				combo.add(StringUtils.EMPTY);
				for(Count count:facet.getValues()) {
					combo.add(Objects.toString(count.getName(), LABEL_EMPTY)+" ("+count.getCount()+")");
				}
				widget = combo;
				// Filter results when user selects a facet value.
				combo.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent event) {
						String filterName = widget.getData("field").toString();
						// TODO regex is not ideal be seems to be the only way to extract "foo" from "foo (5)".
						Pattern pattern = Pattern.compile("^(.*) \\([0-9]+\\)$");
						Matcher matcher = pattern.matcher(combo.getText());
						// TODO if cannot find pattern, then should log WARNING
						String filterValue = matcher.find() ? matcher.group(1) : StringUtils.EMPTY;
						if (StringUtils.isEmpty(filterValue)) {
							filters.remove(filterName);
						} else {
							filters.put(filterName, filterValue);
						}
						try {
							refresh();
						} catch (SophieException e) {
							new ErrorDialog(parent.getShell(), new SophieException("Unable to refresh documents from Solr server", e)).open();
						}
					}
				});
			} else {
				final Text text = new Text(table, SWT.BORDER);
				widget = text;
				// Filter results when user selects a facet value.
				text.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent event) {
						String filterName = widget.getData("field").toString();
						String filterValue = text.getText();
						if (StringUtils.isEmpty(filterValue)) {
							filters.remove(filterName);
						} else {
							filters.put(filterName, filterValue);
						}
						try {
							refresh();
						} catch (SophieException e) {
							new ErrorDialog(parent.getShell(), new SophieException("Unable to refresh documents from Solr server", e)).open();
						}							
					}
				});
			}
			// TODO switch to final objects and do not use setData() ?
			widget.setData("field", facet.getName());
		    editor.grabHorizontal = true;
		    // We add one since the first column is used for row ID.
		    editor.setEditor(widget, items[0], i+1);
		    editor = new TableEditor(table);
		}
		
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
		    	for (int i=0; i<fields.size(); i++) {
		    		Rectangle rect = item.getBounds(i+1);
		    		if (rect.contains(point)) {
		    			SolrDocument document = (SolrDocument) item.getData("document");
		    			Object defaultValue = document.getFieldValue(fields.get(i).getName());
		    			// Add editor dialog:
		    			// - datepicker if we are dealing with a date field.
		    			// - list widget if we are dealing with a multi-valued field.
		    			// - text if we are dealing with any other field type.
		    			DocumentEditValueDialog dialog;
		    			if (defaultValue instanceof Date) {
		    				dialog = new DocumentEditDateValueDialog(parent.getShell(), (Date) defaultValue);
		    			} else if (defaultValue instanceof AbstractList) {
		    				dialog = new DocumentEditListValueDialog(parent.getShell(), (AbstractList<?>) defaultValue);
		    			} else {
		    				String oldValueString = Objects.toString(defaultValue, StringUtils.EMPTY);
		    				dialog = new DocumentEditTextValueDialog(parent.getShell(), oldValueString);
		    			}
		    			dialog.open();
		    			if (dialog.getReturnCode() != IDialogConstants.OK_ID) {
		    				return;
		    			}
	    				Object value = dialog.getValue();
	    				if (!Objects.equals(defaultValue, value)) {
	    					updateDocument(item, i+1, value);
	    					// TODO table cell not updated
	    				}
		    		}
		    	}
			}
		});
		
		return table;
	}
	
	/**
	 * A field is sortable if:
	 * - it is indexed
	 * - it is not multivalued
	 * - it does not have docvalues
	 */
	private static boolean isFieldSortable(FieldInfo field) {
		// field.getFlags() is not populated when lukeRequest.setSchema(false) so we parse flags ourselves based on field.getSchema() TODO open ticket ->)
		// TODO ^ unless SchemaRequest parses flags ?
		EnumSet<FieldFlag> flags = FieldInfo.parseFlags(field.getSchema());
		return (flags.contains(FieldFlag.INDEXED) && !flags.contains(FieldFlag.DOC_VALUES) && !flags.contains(FieldFlag.MULTI_VALUED));		
	}
	
	private int getRemoteCount() throws SophieException {
		SolrQuery query = getBaseQuery(0, 0);
		try {
			// Solr returns a long, table expects an int.
			long count = Sophie.client.query(query).getResults().getNumFound();
			return Integer.parseInt(String.valueOf(count));
		} catch (SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to count remote documents", e);
		}
	}
	
	/*
	 * Map facet name => facet field
	 */
	private Map<String, FacetField> getRemoteFacets() throws SophieException {
		SolrQuery query = getBaseQuery(0, 0);
		query.setFacet(true);
		query.setFacetSort("index");
		query.setFacetLimit(FACET_LIMIT);
		query.setFacetMissing(true);
		for(FieldInfo field:fields) {
			// fq works only on indexed fields.
			// field.getFlags() is not populated when lukeRequest.setSchema(false) so we parse flags ourselves based on field.getSchema() TODO open ticket
			if (FieldInfo.parseFlags(field.getSchema()).contains(FieldFlag.INDEXED)) {
				query.addFacetField(field.getName());	
			}	
		}
		Map<String, FacetField> facets = new HashMap<String, FacetField>();
		try {
			for(FacetField facet:Sophie.client.query(query).getFacetFields()) {
				facets.put(facet.getName(), facet);
			}
		} catch(SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to fetch remote facets", e);
		}
		return facets;
	}
	
	/**
	 * Not null-safe
	 */
	private SolrDocument getRemoteDocument(int rowIndex) throws SophieException {
		int page = rowIndex / PAGE_SIZE;
		// If page has not be fetched yet, then fetch it.
		if (!pages.containsKey(page)) {
			SolrQuery query = getBaseQuery(page * PAGE_SIZE, PAGE_SIZE);
			// TODO user should be able to change sort column.
			// TODO what if sortField is not valid anymore
			query.setSort(sortField, sortOrder);
			try {
				pages.put(page, Sophie.client.query(query).getResults());	
			} catch(SolrServerException|IOException|SolrException e) {
				throw new SophieException("Unable to fetch remote document "+rowIndex, e);
			}
		}
		return pages.get(page).get(rowIndex % PAGE_SIZE);
	}
	
	private SolrQuery getBaseQuery(int start, int rows) {
		SolrQuery query = new SolrQuery("*:*");
		query.setStart(start);
		query.setRows(rows);
		// Add filters. TODO move filters outside of this function ? no need to set fq for facets
		for (Entry<String, String> filter:filters.entrySet()) {
			if (StringUtils.equals(filter.getValue(), LABEL_EMPTY)) {
				// Empty value needs a special syntax. 
				query.addFilterQuery("-"+filter.getKey()+":[* TO *]");
			} else {
				// Colons in value need to be escaped to avoid a syntax error.
				query.addFilterQuery(filter.getKey()+":"+filter.getValue().replace(":", "\\:"));	
			}
		}
		return query;
	}
	
	/**
	 * Get first selected document
	 * 
	 * TODO support multiple selections?
	 */
	private TableItem getSelection() {
		TableItem[] items = table.getSelection();
		return (items.length > 0) ? items[0] : null;
	}
	
	/*
	 * Re-populate table with remote data.
	 */
	public void refresh() throws SophieException {
		// TODO re-populate columns/filters ?
		// TODO re-populate unique field / sort field ?
		documentsUpdated = new ArrayList<SolrDocument>();
		documentsDeleted = new ArrayList<SolrDocument>();
		documentsAdded = new ArrayList<SolrDocument>();
		pages = new HashMap<Integer, SolrDocumentList>();
		changeListener.unchanged();
		try {
			// First row is for filters, the rest is for documents (remote + locally added - though no local addition since we have just refreshed documents).
			table.setItemCount(1 + getRemoteCount());
			table.clearAll();
		} catch(SophieException e) {
			table.setItemCount(1);
			table.clearAll();
			throw new SophieException("Unable to refresh documents from Solr server", e);
		}
	}
	
	public void deleteSelectedDocument() {
		// If a document is selected, then delete it.
		TableItem item = getSelection();
		if (item != null) {
			deleteDocument(item);
		}
	}
	
	public void cloneSelectedDocument() {
		// If a document is selected, then clone it.
		TableItem item = getSelection();
		if (item != null) {
			cloneDocument(item);
		}
	}
	
	/*
	 * Export documents into CSV file.
	 */
	public void export() throws SophieException {
		FileDialog dialog = new FileDialog(table.getShell(), SWT.SAVE);
	    dialog.setFilterNames(new String[] { "CSV Files (*.csv)", "All Files (*.*)" });
	    dialog.setFilterExtensions(new String[] { "*.csv", "*.*" });
	    String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm").format(new Date());
	    dialog.setFileName("documents_"+date+".csv");
	    String path = dialog.open();
	    // TODO what if the file already exists ?
	    // TODO NPE if user does not select any folder
		SolrQuery query = getBaseQuery(0, table.getItemCount());
		QueryRequest request = new QueryRequest(query);
		request.setResponseParser(new NoOpResponseParser("csv"));
		// TODO notify user export success (export may take time).
		try {
			NamedList<Object> response = Sophie.client.request(request);
			String csv = (String) response.get("response"); // TODO 1M lines into a String will fill the ram - is there a way to buffer solr's response ?
			Writer writer = new PrintWriter(path, "UTF-8");
			writer.write(csv);
			writer.close();
		} catch (SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to export documents to CSV file", e);
		}
	}

	/**
	 * Commit local changes to the Solr server.
	 */
	public void upload() throws SophieException {
		// Commit local updates.
		// TODO does not seem to be possible to update multiple documents.
		for (SolrDocument document:documentsUpdated) {
			SolrInputDocument input = new SolrInputDocument();
		    for (String name:document.getFieldNames()) {
		    	input.addField(name, document.getFieldValue(name), 1.0f);
		    }
			try {
				Sophie.client.add(input); // Returned object seems to have no relevant information.
			} catch (SolrServerException|IOException|SolrException e) {
				throw new SophieException("Unable to update document in index: "+input.toString(), e);
			}
		}
		// Commit local deletions.
		for (SolrDocument document:documentsDeleted) {
			String id = document.getFieldValue(uniqueField).toString(); // TODO what if no uniquekey
			try {
				Sophie.client.deleteById(id);
			} catch (SolrServerException|IOException|SolrException e) {
				throw new SophieException("Unable to delete document from index: "+id, e);
			}
		}
		// Commit local additions.
		for (SolrDocument document:documentsAdded) {
			SolrInputDocument input = new SolrInputDocument();
		    for (String name:document.getFieldNames()) {
		    	input.addField(name, document.getFieldValue(name), 1.0f); // TODO 1.0f move to constant
		    }
			try {
				Sophie.client.add(input); // Returned object seems to have no relevant information.
			} catch (SolrServerException|IOException|SolrException e) {
				throw new SophieException("Unable to add document to index: "+input.toString(), e);
			}
		}
		// Commit on server.
		try {
			Sophie.client.commit();
			// TODO allow to revert a specific document
		} catch (SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to commit index", e);
		}
		// Refresh so user can see what the new state of the server.
		refresh();
	}
	
	/*
	 * Restore index from a backup
	 */
	public void restore(String backupName) throws SophieException {
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("command", "restore");
		params.set("name", backupName);
		QueryRequest request = new QueryRequest(params);
		request.setPath("/replication");
		try {
			Sophie.client.request(request);
			// TODO work only for solr >= 5.2 (mention) => disable button if solr < 5.2 https://issues.apache.org/jira/browse/SOLR-6637
			// TODO get /replication?command=restorestatus and provide feedback to user (asynchronous call), possibly with a progress bar
			// TODO test backup/restore
			refresh();
		} catch (SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to restore backup \""+backupName+"\"", e);
		}
		refresh();
	}

	public void updateDocument(TableItem item, int columnIndex, Object newValue) {
		SolrDocument document = (SolrDocument) item.getData("document");
		// The row may not contain any document (e.g. the first row, which contains the filters).
		if (document == null) {
			return;
		}
		// We reduce by 1 since the first column is used for row ID.
		document.setField(fields.get(columnIndex-1).getName(), newValue);
		item.setText(columnIndex, Objects.toString(newValue, StringUtils.EMPTY));
		// TODO if new record, then leave green
		if (!documentsUpdated.contains(document)) {
			documentsUpdated.add(document);	
		}
		item.setBackground(YELLOW);
		changeListener.changed();
	}
	
	private void deleteDocument(TableItem item) {
		// TODO if local item (i.e. does not exist on server), then just drop the row + update rowcount.
		SolrDocument document = (SolrDocument) item.getData("document");
		// The row may not contain any document (e.g. the first row, which contains the filters).
		if (document == null) {
			return;
		}
		if (!documentsDeleted.contains(document)) {
			documentsDeleted.add(document);
		}
		item.setBackground(RED);
		changeListener.changed(); // Handle this in DocumentsToolbar ?
	}
	
	// TODO deleting a local document should decrease setItemCount + drop from this.documentsAdded.
	public void addDocument(SolrDocument document) {
		documentsAdded.add(document);
		table.setItemCount(table.getItemCount() + 1);
		// Scroll to the bottom of the table so we reveal the new document.
		table.setTopIndex(table.getItemCount() - 1);
		changeListener.changed();
	}

	/**
	 * If a document is selected, then clone it.
	 * 
	 * The ID field is unset so we don't have 2 rows describing the same Solr document.
	 * TODO "ID" field could be labeled something else 
	 */
	private void cloneDocument(TableItem item) {
		SolrDocument document = (SolrDocument) item.getData("document");
		document.removeFields("id"); // TODO what if field "id" does not exist
		addDocument(document);
	}

	// TODO allow to filter value on empty value (e.g. value not set)
	
}
