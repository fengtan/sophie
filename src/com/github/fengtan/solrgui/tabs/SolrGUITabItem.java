package com.github.fengtan.solrgui.tabs;

import java.net.URL;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IColumnAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;

import com.github.fengtan.solrgui.solr.SolrDocumentColumnAccessor;
import com.github.fengtan.solrgui.solr.SolrGUIServer;

// TODO icon in ubuntu launcher
// TODO license
// TODO sort by ID field by default ? so rows remain the same when modify one
// TODO if server empty and click on table viewer -> seems to crash
public class SolrGUITabItem extends CTabItem {

	private SolrGUIServer server;
	private NatTable table;
	
	public SolrGUITabItem(URL url, CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		this.server = new SolrGUIServer(url);
		setText(url.toExternalForm());

		// Add table.
		try {
				SolrQuery query = new SolrQuery("*:*");
				EventList<SolrDocument> list = GlazedLists.eventList(server.query(query).getResults());
				IColumnAccessor<SolrDocument> columnPropertyAccessor = new SolrDocumentColumnAccessor(server.getFields());
				IDataProvider dataProvider = new GlazedListsDataProvider<>(list, columnPropertyAccessor);
				ILayer layer = new DataLayer(dataProvider);
				table = new NatTable(getParent(), layer);	
		}catch(SolrServerException e) {
			e.printStackTrace(); // TODO handle exception
		}

		setControl(table);
		
		// Set focus on this tab.
		tabFolder.setSelection(this);
		tabFolder.forceFocus();
	}
	
	@Override
	public void dispose() {
		server.shutdown();
		table.dispose();
		super.dispose();
	}
	
}
