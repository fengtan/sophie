package com.github.fengtan.solrgui.tables;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;


public interface ISolrGUIServerViewer {
	
	/**
	 * Update the view to reflect the fact that a document was added to the list.
	 * 
	 * @param document
	 */
	public void addDocument(SolrGUIDocument document);
	
	/**
	 * Update the view to reflect the fact that a document was removed from the list.
	 * 
	 * @param document
	 */
	public void removeDocument(SolrGUIDocument document);
	
	/**
	 * Update the view to reflect the fact that one of the documents was modified. 
	 * 
	 * @param document
	 */
	public void updateDocument(SolrGUIDocument document);
}