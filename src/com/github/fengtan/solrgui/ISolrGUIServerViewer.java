package com.github.fengtan.solrgui;

import org.apache.solr.common.SolrDocument;

public interface ISolrGUIServerViewer {
	
	/**
	 * Update the view to reflect the fact that a document was added to the list.
	 * 
	 * @param document
	 */
	public void addDocument(SolrDocument document);
	
	/**
	 * Update the view to reflect the fact that a document was removed from the list.
	 * 
	 * @param document
	 */
	public void removeDocument(SolrDocument document);
	
	/**
	 * Update the view to reflect the fact that one of the documents was modified. 
	 * 
	 * @param document
	 */
	public void updateDocument(SolrDocument document);
}