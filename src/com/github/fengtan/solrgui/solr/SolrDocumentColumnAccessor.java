package com.github.fengtan.solrgui.solr;

import java.util.List;

import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.common.SolrDocument;
import org.eclipse.nebula.widgets.nattable.data.IColumnAccessor;

public class SolrDocumentColumnAccessor implements IColumnAccessor<SolrDocument>{

	private List<FieldInfo> fields;
	
	public SolrDocumentColumnAccessor(List<FieldInfo> fields) {
		this.fields = fields;
	}
	
	@Override
	public int getColumnCount() {
		return fields.size();
	}

	@Override
	public Object getDataValue(SolrDocument document, int columnIndex) {
		return document.getFieldValue(fields.get(columnIndex).getName());
	}

	@Override
	public void setDataValue(SolrDocument document, int columnIndex, Object newValue) {
		document.setField(fields.get(columnIndex).getName(), newValue);
		// TODO update server
	}

}
