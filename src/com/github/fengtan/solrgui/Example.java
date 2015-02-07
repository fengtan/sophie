package com.github.fengtan.solrgui;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Example {

	
	
	public static void main(String[] args) throws MalformedURLException {

		URL url = new URL("http", "localhost", 8983, "/solr/collection1");
		
		SolrQuery params = new SolrQuery();
		params.set("q", "*:*");
		
		SolrGUIServer server = new SolrGUIServer(url);
		SolrDocumentList list = server.getDocumentsList(params);
		
		System.out.println(list.getNumFound());
		
		Dashboard db = new Dashboard();
		db.paint();
		
	}
	
}
