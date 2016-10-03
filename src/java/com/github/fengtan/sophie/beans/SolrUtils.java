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
	 * TODO what if new fields get created ? refresh ? should update tables accordingly when refresh
	 */
	public static List<FieldInfo> getRemoteFields() throws SophieException {
		LukeRequest request = new LukeRequest();
		try {
			LukeResponse response = request.process(Sophie.client);
			return new ArrayList<FieldInfo>(response.getFieldInfo().values());
		} catch (SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to fetch list of Solr fields", e);
		}
	}
	
	/**
	 * Get fields defined in the schema (regular + dynamic).
	 * TODO SchemaRequest.Fields and SchemaRequest.DynamicField are experimental classes
	 * @return
	 */
	public static List<String> getRemoteSchemaFields() throws SophieException {
		List<String> fields = new ArrayList<String>();
		SchemaRequest.Fields regularRequest = new SchemaRequest.Fields();
		SchemaRequest.DynamicFields dynamicRequest = new SchemaRequest.DynamicFields();
		try {
			List<NamedList<String>> regularFields = (List<NamedList<String>>) regularRequest.process(Sophie.client).getResponse().getVal(1);
			for (NamedList<String> regularField:regularFields) {
				fields.add(regularField.get("name"));
			}
			List<NamedList<String>> dynamicFields = (List<NamedList<String>>) dynamicRequest.process(Sophie.client).getResponse().getVal(1);
			for (NamedList<String> dynamicField:dynamicFields) {
				fields.add(dynamicField.get("name"));
			}
		} catch (SolrServerException|IOException|SolrException e) {
			throw new SophieException("Unable to fetch list of schema fields", e);
		}
		return fields;
	}

	// TODO could merge with getRemoteFields() to make less queries.
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
