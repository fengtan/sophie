package com.github.fengtan.solrgui;

import org.apache.solr.common.SolrDocument;

public class SolrGUIDocument {

	private SolrGUIChange changes = SolrGUIChange.NONE;
	private SolrDocument document;
	
	public SolrGUIDocument(SolrDocument document) {
		this.document = document;
	}
	
}
