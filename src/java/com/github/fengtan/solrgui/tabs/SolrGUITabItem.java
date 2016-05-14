package com.github.fengtan.solrgui.tabs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.github.fengtan.solrgui.tables.SolrGUITable;

public class SolrGUITabItem extends CTabItem {

	private SolrGUITable table;
	private SolrClient client; // TODO move into SolrGUITable ? so we do not store both this.server and this.url
	private String url; // TODO needed ? should be gettable from this.server
	
	public SolrGUITabItem(CTabFolder tabFolder, String url) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		this.url = url;
		client = new HttpSolrClient(url);
		setText(formatTabTitle(url));
		
		// Prepare layout.
		Composite composite = new Composite(getParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		setControl(composite);
		
		// Set focus on this tab.
		tabFolder.setSelection(this);
		tabFolder.forceFocus();
		
		// Fill in tab.
		try {
			client.ping(); // TODO check that response is "OK" ?
			table = new SolrGUITable(composite, client);
		} catch (Throwable t) {
			// TODO add button "retry"
			// TODO what if server works and then goes down
			t.printStackTrace();
			new Label(composite, SWT.NULL).setText(t.getMessage());
		}
		composite.pack();
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
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.dispose();
	}
	
	public SolrGUITable getTable() {
		return table;
	}
	
	public String getURL() {
		return url;
	}
	
}
