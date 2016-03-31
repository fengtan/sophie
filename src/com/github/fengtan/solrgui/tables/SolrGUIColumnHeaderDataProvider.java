package com.github.fengtan.solrgui.tables;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;

import com.github.fengtan.solrgui.solr.SolrGUIServer;

public class SolrGUIColumnHeaderDataProvider implements IDataProvider {

	private SolrGUIServer server;
	
	public SolrGUIColumnHeaderDataProvider(SolrGUIServer server) {
		this.server = server;
	}
	
	@Override
	public int getColumnCount() {
		return server.getFields().length;
	}

	@Override
	public Object getDataValue(int columnIndex, int rowIndex) {
		return server.getFields()[columnIndex];
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		 throw new UnsupportedOperationException();
	}

}
