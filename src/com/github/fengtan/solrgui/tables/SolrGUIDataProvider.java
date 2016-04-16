package com.github.fengtan.solrgui.tables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.SolrDocumentList;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.sort.SortDirectionEnum;

public class SolrGUIDataProvider implements IDataProvider {

	private SolrServer server;
	
	// Fetch 50 documents at a time. TODO make this configurable ?
	private static final int PAGE_SIZE = 50;
	private Map<Integer, SolrDocumentList> pages;
	private int rowCount = 0;
	private List<FieldInfo> fields;
	private SortClause sortClause = null;
	
	// TODO should not pass fields like this
	public SolrGUIDataProvider(SolrServer server, List<FieldInfo> fields) {
		this.server = server;
		this.rowCount = getRemoteRowCount();
		this.fields = fields;
		this.invalidateCache();
	}
	
	public int getColumnCount() {
		return fields.size();
	}
	
	private void invalidateCache() {
		this.pages = new HashMap<Integer, SolrDocumentList>();
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
	
	public void setSort(int fieldIndex, SortDirectionEnum direction) {
		String field = fields.get(fieldIndex).getName();
		switch (direction) {
			case ASC:
				this.sortClause = new SortClause(field, ORDER.asc);
				break;
			case DESC:
				this.sortClause = new SortClause(field, ORDER.desc);
				break;
			case NONE:
				this.sortClause = null;
		}
		this.invalidateCache();
	}

	public Object getDataValue(int columnIndex, int rowIndex) {
		int page = rowIndex / PAGE_SIZE;
		// If page has not be fetched yet, then fetch it.
		if (!pages.containsKey(page)) {
			SolrQuery query = new SolrQuery("*:*");
			query.setStart(page * PAGE_SIZE);
			query.setRows(PAGE_SIZE);
			if (sortClause != null) {
				query.setSort(sortClause);	
			}
			try {
				pages.put(page, server.query(query).getResults());	
			} catch(SolrServerException e) {
				// TODO handle exception
				e.printStackTrace();
			}
		}
		String field = fields.get(columnIndex).getName();
		return pages.get(page).get(rowIndex % PAGE_SIZE).getFieldValue(field);
	}

	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		int page = rowIndex / PAGE_SIZE;
		String field = fields.get(columnIndex).getName();
		pages.get(page).get(rowIndex % PAGE_SIZE).setField(field, newValue);
		// TODO update server ?
	}
	
	
}