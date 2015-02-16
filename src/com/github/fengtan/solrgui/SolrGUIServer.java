package com.github.fengtan.solrgui;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

public class SolrGUIServer {

	private SolrServer server;
	private List<SolrGUIDocument> documents;
	private Set<ISolrGUIServerViewer> changeListeners = new HashSet<ISolrGUIServerViewer>();

	public SolrGUIServer(URL url) {
		server = new HttpSolrServer(url.toExternalForm());
		documents = getAllDocuments();
	}
	
	// TODO cache ? use transactions ?
	private List<SolrGUIDocument> getAllDocuments() {
		SolrQuery query = new SolrQuery();
		query.set("q", "*:*");
		return getDocumentList(query);
	}
	
	private List<SolrGUIDocument> getDocumentList(SolrQuery query) {
		List<SolrGUIDocument> list = new ArrayList<SolrGUIDocument>();
		QueryResponse response;
		try {
			response = server.query(query);
		} catch (SolrServerException e) {
			// TODO log error
			return list;
		}
		for (SolrDocument document:response.getResults()) {
			list.add(new SolrGUIDocument(document));
		}
		return list;
	}

	/**
	 * Return the collection of documents
	 */
	public List<SolrGUIDocument> getDocuments() {
		return documents;
	}
	
	/**
	 * Add a new document to the collection of documents
	 */
	public void addDocument() {
		SolrGUIDocument document = new SolrGUIDocument(getFields());
		document.setChange(SolrGUIChange.ADDED);
		documents.add(documents.size(), document);
		for (ISolrGUIServerViewer viewer:changeListeners) {
			viewer.addDocument(document);
		}
	}

	/**
	 * @param document
	 */
	public void removeDocument(SolrGUIDocument document) {
		documents.remove(document);
		for (ISolrGUIServerViewer viewer:changeListeners) {
			viewer.removeDocument(document);
		}
	}

	/**
	 * @param document
	 */
	public void documentChanged(SolrGUIDocument document) {
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
	
	// TODO is there a better way to get the list of fields ? e.g. SolrQuery with only 1 document (not q *:*)
	// TODO what if there is no document in the server ?
	public String[] getFields() {
		Collection<String> fields = getAllDocuments().get(0).getFieldNames();
		return fields.toArray(new String[fields.size()]);
	}

}