package com.github.fengtan.solrgui.tabs;

import java.net.URL;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import com.github.fengtan.solrgui.tables.SolrGUITable;

// TODO icon in ubuntu launcher
// TODO license
// TODO sort by ID field by default ? so rows remain the same when modify one
// TODO if server empty and click on table viewer -> seems to crash
public class SolrGUITabItem extends CTabItem {

	private SolrServer server;
	private NatTable table;
	
	// TODO use String instead of URL ?
	public SolrGUITabItem(URL url, CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		this.server = new HttpSolrServer(url.toExternalForm());
		setText(url.toExternalForm());

		// Add table.
		table = new SolrGUITable(getParent(), server);
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
