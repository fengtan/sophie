package com.github.fengtan.solrgui.beans;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.github.fengtan.solrgui.viewers.ISolrGUIServerViewer;

// TODO test with Solr < 4.
// TODO test 2 servers with different fields/schema.
public class SolrGUIServer {

	private URL url;
	private String name;
	private SolrServer server;
	private Map<String,String> parameters = new HashMap<String, String>(); // Query parameters.
	private String[] fields;
	private List<SolrGUIDocument> documents;
	private Set<ISolrGUIServerViewer> changeListeners = new HashSet<ISolrGUIServerViewer>();

	public SolrGUIServer(URL url, String name, Map<String, String> parameters) {
		this.url = url;
		this.name = name;
		this.server = new HttpSolrServer(url.toExternalForm());
		this.parameters = parameters;
		refreshFields();
		refreshDocuments();
	}
	
	public URL getURL() {
		return url;
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public void refreshDocuments() {
		// TODO cache ? use transactions ?
		// TODO allow not to use the default request handler + allow to configure req params => advanded "Add Server" in menus
		// Build query.
		SolrQuery query = new SolrQuery();
		for (Map.Entry<String, String> parameter: parameters.entrySet()) {
			query.set(parameter.getKey(), parameter.getValue());
		}
		// Initialize attribute.
		documents = new ArrayList<SolrGUIDocument>();
		// Get Solr response and update local attribute.
		try {
			QueryResponse response = server.query(query);
			for (SolrDocument document:response.getResults()) {
				documents.add(new SolrGUIDocument(document));
			}
		} catch (SolrServerException e) {
			// TODO log error
			e.printStackTrace();
		}
	}

	/**
	 * Return the collection of documents
	 */
	public List<SolrGUIDocument> getDocuments() {
		return documents;
	}
	
	/**
	 * Add a blank document to the collection of documents
	 */
	public void addDocument() {
		addDocument(new SolrGUIDocument(getFields()));
	}

	/**
	 * Add a new document to the collection of documents
	 */
	public void addDocument(SolrGUIDocument document) {
		document.setStatus(SolrGUIStatus.ADDED);
		documents.add(documents.size(), document);
		for (ISolrGUIServerViewer viewer:changeListeners) {
			viewer.addDocument(document);
		}
	}

	/**
	 * @param document
	 */
	public void removeDocument(SolrGUIDocument document) {
		document.setStatus(SolrGUIStatus.DELETED);
		for (ISolrGUIServerViewer viewer:changeListeners) {
			viewer.updateDocument(document);
		}
	}

	/**
	 * @param document
	 */
	public void documentChanged(SolrGUIDocument document) {
		document.setStatus(SolrGUIStatus.UPDATED);
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
	
	public String[] getFields() {
		return fields;
	}
	
	public void refreshFields() {
		LukeRequest request = new LukeRequest();
		try {
			LukeResponse response = request.process(server);
			Collection<FieldInfo> fieldsInfo = response.getFieldInfo().values();
			List<String> fieldsList = new ArrayList<String>();
			for (FieldInfo fieldInfo:fieldsInfo) {
				fieldsList.add(fieldInfo.getName());
			}
			Collections.sort(fieldsList);
			fields = fieldsList.toArray(new String[fieldsList.size()]);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fields = new String[]{};
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fields = new String[]{};
		}
		/* TODO provide option to use this in case Luke handler is not available? requires at least 1 document in the server
		Collection<String> fields = getAllDocuments().get(0).getFieldNames();
		return fields.toArray(new String[fields.size()]);
		*/
	}
	
	public void commit() {
		// TODO could use CollectionUtils.filter().
		// TODO graceful degradation if Luke handlers not provided by server
		// TODO are all jars required ?
		SolrInputDocument input;
		for (SolrGUIDocument document:documents) {
			switch (document.getStatus()) {
				case ADDED:
					input = ClientUtils.toSolrInputDocument(document.getDocument());
					try {
						// TODO use LukeRequest ?
						server.add(input); // Returned object seems to have no relevant information.
					} catch(SolrServerException e) {
						// TODO
						e.printStackTrace();
					} catch(IOException e) {
						// TODO
						e.printStackTrace();
					}
					break;
				case DELETED:
					// TODO use LukeRequest ?
					String id = document.getDocument().getFieldValue("id").toString(); // TODO what if no field named "id"
					try {
						server.deleteById(id);
					} catch (SolrServerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case NONE:
					// Nothing to do.
					break;
				case UPDATED:
					// TODO use LukeRequest ?
					input = ClientUtils.toSolrInputDocument(document.getDocument());
					try {
						server.add(input); // Returned object seems to have no relevant information.
					} catch (SolrServerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
			}
		}
		try {
			server.commit(); 
			refreshDocuments();  // Returned object seems to have no relevant information.
			// TODO allow to revert a specific document
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO reload data
	}
	
	/*
	 * Release resources.
	 */
	public void dispose() {
		server.shutdown();
	}

}