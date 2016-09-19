package com.github.fengtan.solrgui.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;

import com.github.fengtan.solrgui.SolrGUI;

public class SolrUtils {

	/**
	 * Helper to get remote fields
	 * TODO should move somethere else
	 * TODO what if new fields get created ? refresh ? should update tables accordingly when refresh
	 */
	public static List<FieldInfo> getRemoteFields() {
		// TODO use SchemaRequest instead of LukeRequest
		LukeRequest request = new LukeRequest();
		try {
			LukeResponse response = request.process(SolrGUI.client);
			return new ArrayList<FieldInfo>(response.getFieldInfo().values());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Collections.emptyList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Collections.emptyList();
		}
		/* TODO provide option to use this in case Luke handler is not available? requires at least 1 document in the server
		Collection<String> fields = getAllDocuments().get(0).getFieldNames();
		return fields.toArray(new String[fields.size()]);
		*/
	}
	

	// TODO could merge with getRemoteFields() to make less queries.
	// TODO what if uniquefield is not defined ?
	// TODO should move somethere else
	public static String getRemoteUniqueField() {
		SchemaRequest.UniqueKey request = new SchemaRequest.UniqueKey();
		try {
			return request.process(SolrGUI.client).getUniqueKey();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return StringUtils.EMPTY;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return StringUtils.EMPTY;
		}
	}
	
	// TODO cache result until new connection or refresh ?
	/**
	 * @return Map <core name, attributes>
	 */
	public static Map<String, NamedList<Object>> getCores() {
		CoreAdminRequest request = new CoreAdminRequest();
		request.setAction(CoreAdminAction.STATUS);
		try {
			CoreAdminResponse response = request.process(SolrGUI.client); // TODO throws RemoteSolrException if query on /solr/collection1 instead of /solr
			return response.getCoreStatus().asMap(-1);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.EMPTY_MAP;
	}
	
}
