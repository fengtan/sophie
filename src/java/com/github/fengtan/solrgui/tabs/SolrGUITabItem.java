package com.github.fengtan.solrgui.tabs;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.solrgui.tables.SolrGUITable;

public class SolrGUITabItem extends CTabItem {

	private SolrGUITable table;
	private SolrServer server; // TODO move into SolrGUITable ? so we do not store both this.server and this.url
	private String url; // TODO needed ? should be gettable from this.server
	
	public SolrGUITabItem(CTabFolder tabFolder, String url) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		this.url = url;
		setText(url);
		
		// Fill in tab.
		Composite tabComposite = new Composite(getParent(), SWT.NULL);
		tabComposite.setLayout(new GridLayout());
		server = new HttpSolrServer(url);
		table = new SolrGUITable(tabComposite, server);
		setControl(tabComposite); // TODO need composite ? TODO add status line ?
		
		// Set focus on this tab.
		tabFolder.setSelection(this);
		tabFolder.forceFocus();
	}
	
	@Override
	public void dispose() {
		server.shutdown();
		super.dispose();
	}
	
	public SolrGUITable getTable() {
		return table;
	}
	
	public String getURL() {
		return url;
	}
	
}
