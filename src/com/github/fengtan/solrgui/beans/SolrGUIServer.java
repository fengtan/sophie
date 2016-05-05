package com.github.fengtan.solrgui.beans;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.SolrDocument;

// TODO use SolrServer
public class SolrGUIServer {

	private String url;
	private SolrServer server;
	private List<SolrDocument> documents;

	public SolrGUIServer(String url) {
		this.url = url;
		this.server = new HttpSolrServer(url);
		this.documents = refreshDocuments(SolrGUIQuery.ALL_DOCUMENTS); // TODO do we need to use a local attribute documents ?
	}
	
	public String getURL() { // TODO needed ?
		return url;
	}
	
	public List<SolrDocument> refreshDocuments(SolrQuery query) {
		// TODO cache ? use transactions ?
		// TODO allow not to use the default request handler + allow to configure req params => advanded "Add Server" in menus
		try {
			return server.query(query).getResults();
		} catch (SolrServerException e) {
			// TODO log error
			e.printStackTrace();
			return new ArrayList<SolrDocument>(); // TODO collectionutils.EMPTY
		}
	}

	/**
	 * Return the collection of documents
	 */
	public List<SolrDocument> getDocuments() {
		return documents;
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
		/* TODO implement
		SolrInputDocument input;
		for (SolrDocument document:documents) {
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
		*/
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