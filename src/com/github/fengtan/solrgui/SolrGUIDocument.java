package com.github.fengtan.solrgui;

import java.util.Collection;

import org.apache.solr.common.SolrDocument;

public class SolrGUIDocument {

	private SolrGUIChange changes = SolrGUIChange.NONE;
	private SolrDocument document;
	
	// TODO drop ?
	public SolrGUIDocument(SolrDocument document) {
		this.document = document;
	}
	
	public SolrGUIDocument(String[] fields) {
		document = new SolrDocument();
		for (String field:fields) {
			document.setField(field, "");
		}
	}
	
	public void setField(String name, Object value) {
		document.setField(name, value);
	}
	
	public Collection<String> getFieldNames() {
		return document.getFieldNames();
	}
	
	public Object getFieldValue(String name) {
		return document.getFieldValue(name);
	}
	
}
