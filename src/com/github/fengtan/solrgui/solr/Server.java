package com.github.fengtan.solrgui.solr;

import java.net.URL;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class Server {
	
	private SolrServer server;
	private URL url;
	private String name;
	
	public Server(URL url) {
		this(url, url.toExternalForm());
	}
	
	public Server(URL url, String name) {
		this.url = url;
		this.name = name;
		this.server = new HttpSolrServer(url.toExternalForm());
	}
	
	public URL getURL() {
		return url;
	}
	
	public String getName() {
		return name;
	}
	
	public SolrDocumentList getAllDocuments() {
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
	/*TODO uncomment
	public void deleteDocument(String id) {
		try {
			server.deleteById(id);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
}
