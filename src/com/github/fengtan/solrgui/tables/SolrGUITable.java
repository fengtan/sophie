package com.github.fengtan.solrgui.tables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.SolrDocumentList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class SolrGUITable { // TODO extend Composite ?

	// Fetch 50 documents at a time. TODO make this configurable ?
	private static final int PAGE_SIZE = 50;
	private Map<Integer, SolrDocumentList> pages = new HashMap<Integer, SolrDocumentList>();
	private List<FieldInfo> fields;
	
	private Table table;
	private TableViewer tableViewer;
	private SolrServer server;

	public SolrGUITable(Composite parent, String url) {
		this.server = new HttpSolrServer(url);
		this.fields = getRemoteFields(); // TODO what if new fields get created ? refresh ?
		this.table = createTable(parent);
		this.tableViewer = createTableViewer();
	}
	
	/**
	 * Create the Table
	 */
	private Table createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL; // TODO HIDE_SELECTION ?

		final Table table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 5; // 5 according to number of buttons.
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		/* TODO implement
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				// TODO document in readme: typing 'Suppr' deletes a row.
				if (event.keyCode == SWT.DEL) {
					Table table = (Table) event.getSource();
					TableItem item = table.getSelection()[0]; // TODO what if [0] does not exist.
					SolrDocument document = (SolrDocument) item.getData();
					server.removeDocument(document);
				}
			}
		});
		*/

		table.setItemCount(getRemoteCount());
		table.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event event) {
	            TableItem item = (TableItem) event.item;
	            int rowIndex = table.indexOf(item);
	            // The first line is used for filters.
	            if (rowIndex == 0) {
	            	return;
	            }
	            // Use rowIndex - 1 since the first line is used for filters.
	            for(int i=0; i<fields.size(); i++) {
	            	Object value = getDocumentValue(rowIndex - 1, fields.get(i));
	            	item.setText(i, Objects.toString(value, ""));
	            }
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
			CCombo combo = new CCombo(table, SWT.NONE);
			combo.add("item 1"); // TODO item1
		    combo.add("item 2"); // TODO item2
		    editor.grabHorizontal = true;
		    editor.setEditor(combo, items[0], i);
		    editor = new TableEditor(table);	
		}
		// TODO re-use editor instead of SorlGUICellModifier ?
		
		return table;
	}
	
	/**
	 * Create the TableViewer 
	 */
	private TableViewer createTableViewer() {
		// TODO use collectionutils to transform List<FieldInfo> into List<String>
		List<String> columnNames = new ArrayList<String>();
		for(FieldInfo field:fields) {
			columnNames.add(field.getName());
		}
		// TODO cols
		String[] cols = new String[columnNames.size()];
		columnNames.toArray(cols);
		
		TableViewer tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(cols);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[tableViewer.getColumnProperties().length];
		TextCellEditor textEditor;

		for (int i=0; i < editors.length; i++) {
			textEditor = new TextCellEditor(table);
			((Text) textEditor.getControl()).setTextLimit(60);
			editors[i] = textEditor;
		}
		
		tableViewer.setCellEditors(editors);
		tableViewer.setCellModifier(new SolrGUICellModifier());
		
		return tableViewer;
	}
	
	public void dispose() {
		tableViewer.getLabelProvider().dispose(); // TODO needed ?
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
		SolrQuery query = new SolrQuery("*");
		query.setRows(0);
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
	
	/**
	 * Not null-safe
	 */
	private Object getDocumentValue(int rowIndex, FieldInfo field) {
		int page = rowIndex / PAGE_SIZE;
		// If page has not be fetched yet, then fetch it.
		if (!pages.containsKey(page)) {
			SolrQuery query = new SolrQuery("*:*");
			query.setStart(page * PAGE_SIZE);
			query.setRows(PAGE_SIZE);
			try {
				pages.put(page, server.query(query).getResults());	
			} catch(SolrServerException e) {
				// TODO handle exception
				e.printStackTrace();
			}
		}
		return pages.get(page).get(rowIndex % PAGE_SIZE).getFieldValue(field.getName());
	}
	
}
