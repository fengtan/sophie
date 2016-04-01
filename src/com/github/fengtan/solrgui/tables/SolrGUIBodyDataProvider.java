package com.github.fengtan.solrgui.tables;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;

import com.github.fengtan.solrgui.solr.SolrGUIServer;

public class SolrGUIBodyDataProvider implements IDataProvider {

	private SolrGUIServer server;
	
	// Fetch 50 documents at a time. TODO make this configurable ?
	private static final int PAGE_SIZE = 50;	
	private Map<Integer, SolrDocumentList> pages = new HashMap<Integer, SolrDocumentList>();
	private int rowCount = 0;
	
	public SolrGUIBodyDataProvider(SolrGUIServer server) {
		this.server = server;
		this.rowCount = getRemoteRowCount();
	}
	
	public int getColumnCount() {
		return server.getFields().length;
	}
	
	// TODO should we refresh rowCount every time we fetch a new page ?
	private int getRemoteRowCount() {
		SolrQuery query = new SolrQuery("*:*");
		query.setRows(0);
		try {
			// Solr returns a long, NatTable expects an int.
			long count = server.query(query).getResults().getNumFound();
			return Integer.parseInt(String.valueOf(count));
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public int getRowCount() {
		return rowCount;
	}

	public Object getDataValue(int columnIndex, int rowIndex) {
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
			}
		}
		String fieldName = server.getFields()[columnIndex];
		return pages.get(page).get(rowIndex % PAGE_SIZE).getFieldValue(fieldName);
	}

	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		// Do nothing
		// TODO ?
	}
	
}
