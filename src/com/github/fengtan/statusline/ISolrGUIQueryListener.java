package com.github.fengtan.statusline;

import org.apache.solr.client.solrj.SolrResponse;

public interface ISolrGUIQueryListener {

	// TODO merge QueryListener + ChangeListener ?
	public void responseReceived(SolrResponse response);
	
}
