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
	private SolrServer server;
	
	public SolrGUITabItem(CTabFolder tabFolder, String url) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
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
	
	public SolrGUITable getTable() {
		return table;
	}
	
	@Override
	public void dispose() {
		server.shutdown();
		super.dispose();
	}
	
	
	/* TODO implement

	// TODO what is the point of encapsulating server
	public void addNewDocument() {
		server.addDocument();
		refreshStatusLine();
	}
	
	public void deleteCurrentDocument() {
		SolrDocument document = table.getSelectedDocument();
		if (document != null) {
			server.removeDocument(document);
		}
		refreshStatusLine();
	}
	
	public void cloneCurrentDocument() {
		SolrDocument document = table.getSelectedDocument();
		if (document != null) {
			// TODO Cloning generate remote exception
			server.addDocument(document.clone());
		}
		refreshStatusLine();
	}
	
	public void clear() {
		server.clear();
		table.refresh();
		refreshStatusLine();
	}
	*/
	
}
