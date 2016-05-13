package com.github.fengtan.solrgui.tables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.luke.FieldFlag;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.dialogs.SolrGUIEditValueDialog;

public class SolrGUITable { // TODO extend Composite ?

	private static final Color YELLOW = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
	private static final Color RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	private static final Color GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	
	// Fetch 50 documents at a time. TODO make this configurable ?
	private static final int PAGE_SIZE = 25;
	
	// Display 50 facet values at most. TODO make this configurable ?
	private static final int FACET_LIMIT = 50;
	
	private Map<Integer, SolrDocumentList> pages;
	private List<FieldInfo> fields;
	private Map<String, FacetField> facets;
	private Map<String, String> filters = new HashMap<String, String>();

	// List of documents locally updated/deleted/added.
	// TODO use List<SolrDocument> instead of List<TableItem> ?
	private List<TableItem> documentsUpdated;
	private List<TableItem> documentsDeleted;
	private List<SolrDocument> documentsAdded;
	
	private Table table;
	private SolrServer server;

	public SolrGUITable(Composite parent, SolrServer server) {
		this.server = server;
		this.fields = getRemoteFields(); // TODO what if new fields get created ? refresh ?
		this.facets = getRemoteFacets();
		this.table = createTable(parent);
		// Initialize cache + row count.
		refresh();
	}
	
	/**
	 * Create the Table
	 */
	private Table createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL;

		final Table table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		// Add KeyListener to delete documents.
		// TODO hitting "suppr" or clicking the button a second time should remove the deletion.
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
		            document = getRemoteDocument(rowIndex - 1);
	            }
		        for(int i=0; i<fields.size(); i++) {
		        	String fieldName = fields.get(i).getName();
		            item.setText(i, Objects.toString(document.getFieldValue(fieldName), ""));
		        }
		        // Store document in item.
		        item.setData("document", document);
		        // TODO use item.setText(String[] values) ?
		        // TODO store status insert/update/delete using item.setData() ?
			}
		});
		
		// Add columns
		for (FieldInfo field:fields) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(field.getName());
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
				combo.add("");
				for(Count count:facet.getValues()) {
					combo.add(count.getName()); // TODO use count.getCount() too ?
				}
				widget = combo;
				// Filter results when user selects a facet value.
				combo.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent event) {
						String filterName = widget.getData("field").toString();
						String filterValue = combo.getText();
						if (StringUtils.isEmpty(filterValue)) {
							filters.remove(filterName);
						} else {
							filters.put(filterName, filterValue);
						}
						refresh();
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
						refresh();
					}
				});
			}
			widget.setData("field", facet.getName());
		    editor.grabHorizontal = true;
		    editor.setEditor(widget, items[0], i);
		    editor = new TableEditor(table);
		}
		
		// Add editor dialog.
		final SolrGUIEditValueDialog dialog = new SolrGUIEditValueDialog(table.getShell());
		final SolrGUITable tmp = this; // TODO not very elegant
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
		    	for (int i=0; i<fields.size(); i++) {
		    		Rectangle rect = item.getBounds(i);
		    		if (rect.contains(point)) {
		    			dialog.open(item.getText(i), item, i, tmp);
		    		}
		    	}
			}
		});
		
		return table;
	}
	
	private List<FieldInfo> getRemoteFields() {
		LukeRequest request = new LukeRequest();
		request.setShowSchema(true);
		try {
			LukeResponse response = request.process(server);
			return new ArrayList<FieldInfo>(response.getFieldInfo().values());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<FieldInfo>(); // TODO Collections.EMPTY_LIST
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<FieldInfo>(); // TODO Collections.EMPTY_LIST
		}
		/* TODO provide option to use this in case Luke handler is not available? requires at least 1 document in the server
		Collection<String> fields = getAllDocuments().get(0).getFieldNames();
		return fields.toArray(new String[fields.size()]);
		*/
	}

	private int getRemoteCount() {
		SolrQuery query = getBaseQuery(0, 0);
		try {
			// Solr returns a long, table expects an int.
			long count = server.query(query).getResults().getNumFound();
			return Integer.parseInt(String.valueOf(count));
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	/*
	 * Map facet name => facet field
	 */
	private Map<String, FacetField> getRemoteFacets() {
		SolrQuery query = getBaseQuery(0, 0);
		query.setFacet(true);
		query.setFacetSort("index");
		query.setFacetLimit(FACET_LIMIT);
		for(FieldInfo field:fields) {
			// fq works only on indexed fields. 
			if (field.getFlags().contains(FieldFlag.INDEXED)) {
				query.addFacetField(field.getName());	
			}	
		}
		Map<String, FacetField> facets = new HashMap<String, FacetField>();
		try {
			for(FacetField facet:server.query(query).getFacetFields()) {
				facets.put(facet.getName(), facet);
			}
		} catch(SolrServerException e) {
			e.printStackTrace();
		}
		return facets;
	}
	
	/**
	 * Not null-safe
	 */
	private SolrDocument getRemoteDocument(int rowIndex) {
		int page = rowIndex / PAGE_SIZE;
		// If page has not be fetched yet, then fetch it.
		if (!pages.containsKey(page)) {
			SolrQuery query = getBaseQuery(page * PAGE_SIZE, PAGE_SIZE);
			// TODO "id" should be fetched using query.getInt("uniqueKey")
			// TODO user should be able to change sort column. 
			query.setSort("id", ORDER.asc);
			try {
				pages.put(page, server.query(query).getResults());	
			} catch(SolrServerException e) {
				// TODO handle exception
				e.printStackTrace();
			}
		}
		return pages.get(page).get(rowIndex % PAGE_SIZE);
	}
	
	private SolrQuery getBaseQuery(int start, int rows) {
		SolrQuery query = new SolrQuery("*:*");
		query.setStart(start);
		query.setRows(rows);
		// Add filters. TODO move filters outside of this function ? no need to set fq for facets
		// We escape colons in the field value to avoid a syntax error from Solr.
		for (Entry<String, String> filter:filters.entrySet()) {
			query.addFilterQuery(filter.getKey()+":"+filter.getValue().replace(":", "\\:"));
		}
		return query;
	}
	
	/*
	 * Re-populate table with remote data.
	 */
	public void refresh() {
		// TODO re-populate columns/filters ?
		documentsUpdated = new ArrayList<TableItem>();
		documentsDeleted = new ArrayList<TableItem>();
		documentsAdded = new ArrayList<SolrDocument>();
		pages = new HashMap<Integer, SolrDocumentList>();
		// First row is for filters, the rest is for documents (remote + locally added - though no local addition since we have just refreshed documents).
		table.setItemCount(1 + getRemoteCount());
		table.clearAll();
	}
	
	/*
	 * Commit local changes to the Solr server.
	 */
	public void commit() {
		// Commit local updates.
		// TODO does not seem to be possible to update multiple documents.
		for (TableItem item:documentsUpdated) {
			SolrDocument document = (SolrDocument) item.getData("document");
			SolrInputDocument input = ClientUtils.toSolrInputDocument(document);
			try {
				server.add(input); // Returned object seems to have no relevant information.
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Commit local deletions.
		for (TableItem item:documentsDeleted) {
			// TODO
			SolrDocument document = (SolrDocument) item.getData("document");
			String id = document.getFieldValue("id").toString(); // TODO what if no field named "id"
			try {
				server.deleteById(id);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Commit local additions.
		for (SolrDocument document:documentsAdded) {
			SolrInputDocument input = ClientUtils.toSolrInputDocument(document);
			try {
				server.add(input); // Returned object seems to have no relevant information.
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Commit on server.
		try {
			server.commit();
			// TODO allow to revert a specific document
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Refresh so user can see what the new state of the server.
		refresh();
	}
	
	/*
	 * Delete all documents on server.
	 */
	public void clear() {
		try {
			server.deleteByQuery("*:*");
			server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refresh();
	}

	public void updateDocument(TableItem item) {
		// TODO if new record, then leave green
		if (!documentsUpdated.contains(item)) {
			documentsUpdated.add(item);	
		}
		item.setBackground(YELLOW);
	}
	
	private void deleteDocument(TableItem item) {
		// TODO if local item (i.e. does not exist on server), then just drop the row + update rowcount.
		if (!documentsDeleted.contains(item)) {
			documentsDeleted.add(item);
		}
		item.setBackground(RED);
	}
	
	/**
	 * If a document is selected, then delete it.
	 */
	public void deleteSelectedDocument() {
		TableItem[] items = table.getSelection();
		if (items.length > 0) {
			deleteDocument(items[0]);
		}
	}
	
	// TODO deleting a local document should decrease setItemCount + drop from this.documentsAdded.
	private void addDocument(SolrDocument document) {
		documentsAdded.add(document);
		table.setItemCount(table.getItemCount() + 1);
		// Scroll to the bottom of the table so we reveal the new document.
		table.setTopIndex(table.getItemCount() - 1);
	}
	
	public void addEmptyDocument() {
		addDocument(new SolrDocument());
	}
	
	/**
	 * If a document is selected, then clone it.
	 * 
	 * The ID field is unset so we don't have 2 rows describing the same Solr document.
	 * TODO "ID" field could be labeled something else 
	 */
	public void cloneSelectedDocument() {
		TableItem[] items = table.getSelection();
		if (items.length > 0) {
			SolrDocument document = (SolrDocument) items[0].getData("document");
			document.removeFields("id"); // TODO what if field "id" does not exist
			addDocument(document);
		}
	}
	
	// TODO not ideal
	public List<FieldInfo> getFields() {
		return fields;
	}
	
	// TODO allow to filter value on empty value (e.g. value not set)
	
}
