package com.github.fengtan.solrgui;

import java.util.Collection;

import org.apache.solr.common.SolrDocument;

// TODO extend SolrDocument ? might simplify the code
public class SolrGUIDocument {

	private SolrGUIChange change = SolrGUIChange.NONE;
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
	
	public SolrGUIChange getChange() {
		return change;
	}
	
	public void setChange(SolrGUIChange change) {
		this.change = change;
	}
	
	public SolrDocument getDocument() {
		return document;
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
