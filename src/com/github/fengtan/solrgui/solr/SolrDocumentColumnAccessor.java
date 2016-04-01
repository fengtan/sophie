package com.github.fengtan.solrgui.solr;

import org.apache.solr.common.SolrDocument;
import org.eclipse.nebula.widgets.nattable.data.IColumnAccessor;

public class SolrDocumentColumnAccessor implements IColumnAccessor<SolrDocument>{

	private String[] fields;
	
	public SolrDocumentColumnAccessor(String[] fields) {
		this.fields = fields;
	}
	
	@Override
	public int getColumnCount() {
		return fields.length;
	}

	@Override
	public Object getDataValue(SolrDocument document, int columnIndex) {
		return document.getFieldValue(fields[columnIndex]);
	}

	@Override
	public void setDataValue(SolrDocument document, int columnIndex, Object newValue) {
		// TODO Auto-generated method stub
	}

}
