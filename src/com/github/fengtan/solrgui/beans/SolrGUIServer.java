package com.github.fengtan.solrgui.beans;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

import com.github.fengtan.solrgui.tables.ISolrGUIChangeListener;

public class SolrGUIServer {

	private String url;
	private SolrServer server;

	private List<FieldInfo> fields;
	private List<SolrGUIDocument> documents;
	private Set<ISolrGUIChangeListener> changeListeners = new HashSet<ISolrGUIChangeListener>();

	public SolrGUIServer(String url) {
		this.url = url;
		this.server = new HttpSolrServer(url);
		this.fields = getRemoteFields();
		refreshDocuments(SolrGUIQuery.ALL_DOCUMENTS);
	}
	
	public String getURL() {
		return url;
	}
	
	public void refreshDocuments(SolrQuery query) {
		// TODO cache ? use transactions ?
		// TODO allow not to use the default request handler + allow to configure req params => advanded "Add Server" in menus
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
		addDocument(new SolrGUIDocument(fields));
	}

	/**
	 * Add a new document to the collection of documents
	 */
	public void addDocument(SolrGUIDocument document) {
		document.setStatus(SolrGUIStatus.ADDED);
		documents.add(documents.size(), document);
		for (ISolrGUIChangeListener listener:changeListeners) {
			listener.addDocument(document);
		}
	}

	/**
	 * @param document
	 */
	public void removeDocument(SolrGUIDocument document) {
		document.setStatus(SolrGUIStatus.DELETED);
		for (ISolrGUIChangeListener listener:changeListeners) {
			listener.modifyDocument(document);
		}
	}

	/**
	 * @param document
	 *
	 * If user modified an existing document, flag as 'Updated'.
	 * If user modified a new document, leave flag as 'Added'.
	 */
	public void documentChanged(SolrGUIDocument document) {
		if (!document.getStatus().equals(SolrGUIStatus.ADDED)) {
			document.setStatus(SolrGUIStatus.MODIFIED);
		}
		for (ISolrGUIChangeListener listener:changeListeners) {
			listener.modifyDocument(document);
		}
	}

	/**
	 * @param changeListener
	 */
	public void addChangeListener(ISolrGUIChangeListener changeListener) {
		changeListeners.add(changeListener);
	}
	
	/**
	 * @param changeListener
	 */
	public void removeChangeListener(ISolrGUIChangeListener changeListener) {
		changeListeners.remove(changeListener);
	}
	
	public List<FieldInfo> getRemoteFields() {
		LukeRequest request = new LukeRequest();
		try {
			LukeResponse response = request.process(server);
			return new ArrayList<FieldInfo>(response.getFieldInfo().values());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<FieldInfo>(); // TODO Collections.EMPTY_LIST
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<FieldInfo>(); // TODO Collections.EMPTY_LIST
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
				case MODIFIED:
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

	public void clear() {
		try {
			server.deleteByQuery("*:*");
			server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Release resources.
	 */
	public void dispose() {
		server.shutdown();
	}

}