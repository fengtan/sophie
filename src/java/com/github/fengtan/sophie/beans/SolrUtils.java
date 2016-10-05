/**
 * Sophie - A Solr browser and administration tool
 * 
 * Copyright (C) 2016 fengtan<https://github.com/fengtan>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.fengtan.sophie.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
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
import org.apache.solr.common.luke.FieldFlag;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;

import com.github.fengtan.sophie.Sophie;

/**
 * Common operations on Solr.
 */
public class SolrUtils {

    /**
     * Extract flags from a Solr field.
     *
     * Because of a bug in SolrJ, FieldInfo.getFlags() may return null if the
     * request was made by calling LukeRequest.setSchema(false). This method
     * extracts the flags in all cases.
     * 
     * @param field
     *            Solr field.
     * @return List of flags.
     * @see SOLR-9205
     */
    public static EnumSet<FieldFlag> getFlags(FieldInfo field) {
        EnumSet<FieldFlag> flags = field.getFlags();
        if (flags == null) {
            flags = FieldInfo.parseFlags(field.getSchema());
        }
        return flags;
    }

    /**
     * Get list of instantiated fields from the remote Solr server.
     * 
     * Dynamic fields are named according to how they are instantiated (e.g.
     * dynamic_foobar).
     * 
     * @return List of fields defined on the remote Solr server.
     * @throws SophieException
     *             If the remote fields cannot be fetched.
     */
    public static List<FieldInfo> getRemoteFields() throws SophieException {
        LukeRequest request = new LukeRequest();
        try {
            LukeResponse response = request.process(Sophie.client);
            return new ArrayList<FieldInfo>(response.getFieldInfo().values());
        } catch (SolrServerException | IOException | SolrException e) {
            throw new SophieException("Unable to fetch list of Solr fields", e);
        }
    }

    /**
     * Get a list of declared fields from the remote Solr server.
     * 
     * Dynamic fields are named according to how they are declared (e.g.
     * dynamic_*).
     * 
     * @return Map of fields keyed by field name.
     * @throws SophieException
     *             If the remote fields cannot be fetched.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, FieldInfo> getRemoteSchemaFields() throws SophieException {
        // Send request.
        LukeRequest request = new LukeRequest();
        request.setShowSchema(true);
        LukeResponse response;
        try {
            response = request.process(Sophie.client);
        } catch (SolrServerException | IOException | SolrException e) {
            throw new SophieException("Unable to fetch list of Solr fields", e);
        }
        // Extract regular fields.
        Map<String, FieldInfo> fields = new HashMap<String, FieldInfo>();
        fields.putAll(response.getFieldInfo());
        // Extract dynamic fields.
        NamedList<Object> schema = (NamedList<Object>) response.getResponse().get("schema");
        if (schema != null) {
            NamedList<Object> dynamicFields = (NamedList<Object>) schema.get("dynamicFields");
            if (dynamicFields != null) {
                for (Map.Entry<String, Object> dynamicField : dynamicFields) {
                    FieldInfo fieldInfo = new FieldInfo(dynamicField.getKey());
                    fieldInfo.read((NamedList<Object>) dynamicField.getValue());
                    fields.put(dynamicField.getKey(), fieldInfo);
                }
            }
        }
        return fields;
    }

    /**
     * Get unique key field from the remote Solr server.
     * 
     * TODO could merge with getRemoteFields() to make less queries
     * 
     * TODO could use admin/luke?show=schema
     * 
     * TODO cache uniqueKey - 2 identical requests (fields+tables)
     * 
     * @return Unique key field name.
     * @throws SophieException
     *             If the unique key field cannot be fetched.
     */
    public static String getRemoteUniqueField() throws SophieException {
        SchemaRequest.UniqueKey request = new SchemaRequest.UniqueKey();
        try {
            return request.process(Sophie.client).getUniqueKey();
        } catch (SolrServerException | IOException | SolrException e) {
            throw new SophieException("Unable to fetch name of unique field", e);
        }
    }

    /**
     * Get list of cores from the remote Solr server.
     * 
     * @return Map of cores attributes keyed by core name.
     * @throws SophieException
     *             If the list of cores cannot be fetched.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, NamedList<Object>> getCores() throws SophieException {
        CoreAdminRequest request = new CoreAdminRequest();
        request.setAction(CoreAdminAction.STATUS);
        try {
            CoreAdminResponse response = request.process(Sophie.client);
            return response.getCoreStatus().asMap(-1);
        } catch (SolrServerException | IOException | SolrException e) {
            throw new SophieException("Unable to fetch list of Solr cores", e);
        }
    }
    

    /**
     * Whether Solr can sort a field.
     * 
     * @param field
     *            Field.
     * @return True if Solr can sort the field, false otherwise.
     */
    public static boolean isFieldSortable(FieldInfo field) {
        // A field is sortable if
        // 1) it is indexed
        // 2) it is not multivalued
        // 3) it does not have docValues
        EnumSet<FieldFlag> flags = SolrUtils.getFlags(field);
        return (flags.contains(FieldFlag.INDEXED) && !flags.contains(FieldFlag.DOC_VALUES) && !flags.contains(FieldFlag.MULTI_VALUED));
    }

}
