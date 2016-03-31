package com.github.fengtan.solrgui.tables;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;

import com.github.fengtan.solrgui.solr.SolrGUIServer;

public class SolrGUIDataProvider implements IDataProvider {

	private SolrGUIServer server;
	// TODO virtual => show only rows visible to user
	// TODO check when / how often a solr query is made
	List<SolrDocument> documents;
	
	public SolrGUIDataProvider(SolrGUIServer server) {
		this.server = server;
		
		// TODO is this the right place to build the query ?
		SolrQuery query = new SolrQuery();
		// TODO when should this be called ?
		documents = server.query(query);
	}
	
	public int getColumnCount() {
		return server.getFields().length;
	}

	public int getRowCount() {
		return documents.size();
	}

	public Object getDataValue(int columnIndex, int rowIndex) {
		String fieldName = server.getFields()[columnIndex];
		return documents.get(rowIndex).getFieldValue(fieldName);
	}

	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		// Do nothing
		// TODO ?
	}
	
}
