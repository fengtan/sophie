package com.github.fengtan.solrgui.beans;

import org.apache.solr.client.solrj.SolrQuery;

public class SolrGUIQuery extends SolrQuery {
	
	// TODO use SolrQuery
	
	private static final long serialVersionUID = 1L;
	
	private String q = "*:*"; // TODO allow user to alter ?
	private int rows = 500; // TODO load 500 rows by default + add button "load more" if necessary
	
	public static SolrGUIQuery ALL_DOCUMENTS = new SolrGUIQuery(); // TODO makes no sense if virtual table
	
	private SolrGUIQuery() {
		super(); // TODO needed ?
		setQuery(q);
		setRows(rows);
	}
	
}
