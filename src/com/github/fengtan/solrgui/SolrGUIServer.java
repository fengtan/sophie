package com.github.fengtan.solrgui;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class SolrGUIServer {

	private SolrServer server;
	private List<SolrDocument> documents;
	private Set<ISolrGUIServerViewer> changeListeners = new HashSet<ISolrGUIServerViewer>();

	public SolrGUIServer(URL url) {
		server = new HttpSolrServer(url.toExternalForm());
		documents = getAllDocuments();
	}
	
	private SolrDocumentList getAllDocuments() {
		SolrQuery query = new SolrQuery();
		query.set("q", "*:*");
		return getDocumentList(query);
	}
	
	private SolrDocumentList getDocumentList(SolrQuery query) {
		QueryResponse response;
		try {
			response = server.query(query);
		} catch (SolrServerException e) {
			// TODO log error
			return new SolrDocumentList();
		}
		return response.getResults();
	}

	/**
	 * Return the array of owners   
	 */
	public String[] getOwners() {
		return new String[] { "?", "Nancy", "Larry", "Joe" };
	}
	
	/**
	 * Return the collection of documents
	 */
	public List<SolrDocument> getDocuments() {
		return documents;
	}
	
	/**
	 * Add a new document to the collection of documents
	 */
	public void addDocument() {
		SolrDocument document = new SolrDocument();
		document.setField("item_id", "plop");
		documents.add(documents.size(), document);
		for (ISolrGUIServerViewer viewer:changeListeners) {
			viewer.addDocument(document);
		}
	}

	/**
	 * @param document
	 */
	public void removeDocument(SolrDocument document) {
		documents.remove(document);
		for (ISolrGUIServerViewer viewer:changeListeners) {
			viewer.removeDocument(document);
		}
	}

	/**
	 * @param document
	 */
	public void documentChanged(SolrDocument document) {
		for (ISolrGUIServerViewer viewer:changeListeners) {
			viewer.updateDocument(document);
		}
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(ISolrGUIServerViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(ISolrGUIServerViewer viewer) {
		changeListeners.add(viewer);
	}

}