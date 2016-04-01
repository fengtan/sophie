package com.github.fengtan.solrgui.solr;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;

// TODO drop and manipulate only HttpSolrServer ?
public class SolrGUIServer extends HttpSolrServer {

	private String[] fields;
	
	public SolrGUIServer(URL url) {
		super(url.toExternalForm());
		// TODO what if user adds a new field after the SolrGUIServer object gets created ?
		refreshFields();
	}
	
	public String[] getFields() {
		return fields;
	}
	
	// TODO probably not the right place to call this 
	private void refreshFields() {
		LukeRequest request = new LukeRequest();
		try {
			LukeResponse response = request.process(this);
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

}