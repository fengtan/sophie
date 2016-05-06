package com.github.fengtan.solrgui.tabs;

import java.net.MalformedURLException;
import java.net.URL;

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
		server = new HttpSolrServer(url);
		setText(formatTabTitle(url));
		
		// Prepare layout.
		Composite composite = new Composite(getParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		setControl(composite);
		
		// Set focus on this tab.
		tabFolder.setSelection(this);
		tabFolder.forceFocus();
		
		// Fill in tab.
		table = new SolrGUITable(composite, server);
	}
	
	private String formatTabTitle(String url) {
		try {
			URL u = new URL(url);
			return u.getPath()+"@"+u.getHost()+":"+u.getPort();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return url;
		}
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
