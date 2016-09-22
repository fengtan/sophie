package com.github.fengtan.sophie.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;

import com.github.fengtan.sophie.Sophie;

public class SolrUtils {

	/**
	 * Helper to get remote fields
	 * TODO should move somethere else
	 * TODO what if new fields get created ? refresh ? should update tables accordingly when refresh
	 */
	public static List<FieldInfo> getRemoteFields() throws SophieException {
		// TODO use SchemaRequest instead of LukeRequest
		LukeRequest request = new LukeRequest();
		try {
			LukeResponse response = request.process(Sophie.client);
			return new ArrayList<FieldInfo>(response.getFieldInfo().values());
		} catch (SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to fetch list of Solr fields", e);
		}
		/* TODO provide option to use this in case Luke handler is not available? requires at least 1 document in the server
		Collection<String> fields = getAllDocuments().get(0).getFieldNames();
		return fields.toArray(new String[fields.size()]);
		*/
	}
	

	// TODO could merge with getRemoteFields() to make less queries.
	// TODO what if uniquefield is not defined ?
	// TODO should move somethere else
	public static String getRemoteUniqueField() throws SophieException {
		SchemaRequest.UniqueKey request = new SchemaRequest.UniqueKey();
		try {
			return request.process(Sophie.client).getUniqueKey();
		} catch (SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to fetch name of unique field", e);
		}
	}
	
	// TODO cache result until new connection or refresh ?
	/**
	 * @return Map <core name, attributes>
	 */
	public static Map<String, NamedList<Object>> getCores() throws SophieException {
		CoreAdminRequest request = new CoreAdminRequest();
		request.setAction(CoreAdminAction.STATUS);
		try {
			CoreAdminResponse response = request.process(Sophie.client);
			return response.getCoreStatus().asMap(-1);
		} catch (SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to fetch list of Solr cores", e);
		}
	}
	
}
