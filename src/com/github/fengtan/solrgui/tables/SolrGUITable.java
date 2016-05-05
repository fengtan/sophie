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
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.solrgui.dialogs.SolrGUIEditValueDialog;

public class SolrGUITable { // TODO extend Composite ?

	private static final Color RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	// Fetch 50 documents at a time. TODO make this configurable ?
	private static final int PAGE_SIZE = 50;
	
	private Map<Integer, SolrDocumentList> pages;
	private List<FieldInfo> fields;
	private Map<String, FacetField> facets;
	private Map<String, String> filters = new HashMap<String, String>();

	private Table table;
	private SolrServer server;

	public SolrGUITable(Composite parent, String url) {
		this.server = new HttpSolrServer(url);
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
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.DEL) {
					Table table = (Table) event.getSource();
					TableItem item = table.getSelection()[0]; // TODO what if [0] does not exist.
					// TODO if local item does not exist on server, then just drop the row.
					item.setBackground(RED);
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
	            // Use rowIndex - 1 since the first line is used for filters.
	            // TODO make sure the last document gets displayed.
	            SolrDocument document = getDocument(rowIndex - 1);
	            for(int i=0; i<fields.size(); i++) {
	            	String fieldName = fields.get(i).getName();
	            	item.setText(i, Objects.toString(document.getFieldValue(fieldName), ""));
	            }
	            // Store document in item so we can prepopulate the edit dialog.
	            // TODO needed ? item.setData("document", document);
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
			final CCombo combo = new CCombo(table, SWT.NONE);
			combo.add("");
			// TODO check if map contains field ?
			// TODO no need to use facets for tm_body for instance
			FacetField facet = facets.get(fields.get(i).getName());
			for(Count count:facet.getValues()) {
				combo.add(count.getName()); // TODO use count.getCount() too ?
			}
			combo.setData("field", facet.getName());
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
			// Filter results when user selects a facet value.
			combo.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent event) {
					String filterName = combo.getData("field").toString();
					String filterValue = combo.getText();
					if (StringUtils.isEmpty(filterValue)) {
						filters.remove(filterName);
					} else {
						filters.put(filterName, filterValue);
					}
					refresh();
				}
			});
		    editor.grabHorizontal = true;
		    editor.setEditor(combo, items[0], i);
		    editor = new TableEditor(table);
		}
		
		// Add editor dialog.
		final SolrGUIEditValueDialog dialog = new SolrGUIEditValueDialog(table.getShell());
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
		    	TableItem item = table.getItem(point);
		    	if (item == null) {
		    		return;
		    	}
		    	for (int i=0; i<fields.size(); i++) {
		    		Rectangle rect = item.getBounds(i);
		    		if (rect.contains(point)) {
		    			dialog.open(item.getText(i), item, i);
		    		}
		    	}
			}
		});
		
		return table;
	}
	
	public void dispose() {
		server.shutdown(); // TODO move server instantiation/shutdown into SolrGUITabItem ?
	}
	
	private List<FieldInfo> getRemoteFields() {
		LukeRequest request = new LukeRequest();
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
			// Solr returns a long, SWT expects an int.
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
		query.setFacetLimit(-1); // TODO or set a limit ? no limit could be bad for perf
		for(FieldInfo field:fields) {
			// TODO we don't want facets on fields with too many values
			query.addFacetField(field.getName());	
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
	private SolrDocument getDocument(int rowIndex) {
		int page = rowIndex / PAGE_SIZE;
		// If page has not be fetched yet, then fetch it.
		if (!pages.containsKey(page)) {
			SolrQuery query = getBaseQuery(page * PAGE_SIZE, PAGE_SIZE);
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
		// Add filters.
		for (Entry<String, String> filter:filters.entrySet()) {
			query.addFilterQuery(filter.getKey()+":"+filter.getValue());
		}
		return query;
	}
	
	/*
	 * Re-populate table with remote data.
	 */
	public void refresh() {
		// TODO re-populate columns/filters ?
		pages = new HashMap<Integer, SolrDocumentList>();
		table.setItemCount(1 + getRemoteCount()); // First row is for filters, the rest is for documents.
		table.clearAll();
	}
	
	// TODO allow to filter value on empty value (e.g. value not set)
	
}
