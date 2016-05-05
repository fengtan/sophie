package com.github.fengtan.solrgui.tabs;

import org.apache.solr.client.solrj.SolrQuery;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;
import com.github.fengtan.solrgui.beans.SolrGUIQuery;
import com.github.fengtan.solrgui.beans.SolrGUIServer;
import com.github.fengtan.solrgui.statusline.SolrGUIStatusLine;
import com.github.fengtan.solrgui.tables.SolrGUITable;

public class SolrGUITabItem extends CTabItem {

	private SolrGUIServer server;
	private SolrGUITable table; // TODO Composite ?
	private SolrGUIStatusLine statusLine; // TODO Composite ? TODO use a column for row # + drop statusline ?
	
	public SolrGUITabItem(CTabFolder tabFolder, SolrGUIServer server) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		this.server = server;
		setText(server.getURL());
		setToolTipText(server.getURL().toString());
		
		// Fill in tab.
		Composite tabComposite = new Composite(getParent(), SWT.NULL);
		tabComposite.setLayout(new GridLayout());
		table = new SolrGUITable(tabComposite, server); // TODO passing server is ugly
		statusLine = new SolrGUIStatusLine(tabComposite);
		setControl(tabComposite);
		
		// Set focus on this tab.
		tabFolder.setSelection(this);
		tabFolder.forceFocus();
		
		// Initialize status line.
		refreshStatusLine();
	}
	
	public SolrGUIServer getServer() {
		return server;
	}
	
	// TODO not updated when clicking 'refresh'
	// TODO not updated when launching app
	protected void refreshStatusLine() {
		int count = table.getItemCount();
		String suffix = (count > 1) ? "documents" : "document";
		statusLine.setText(count + " " + suffix);// TODO "XX additions, XX deletions, XX modifications"
	}
	
	@Override
	public void dispose() {
		table.dispose();
		server.dispose();
		super.dispose();
	}

	// TODO what is the point of encapsulating server
	public void addNewDocument() {
		server.addDocument();
		refreshStatusLine();
	}
	
	public void deleteCurrentDocument() {
		SolrGUIDocument document = table.getSelectedDocument();
		if (document != null) {
			server.removeDocument(document);
		}
		refreshStatusLine();
	}
	
	public void cloneCurrentDocument() {
		SolrGUIDocument document = table.getSelectedDocument();
		if (document != null) {
			// TODO Cloning generate remote exception
			server.addDocument(document.clone());
		}
		refreshStatusLine();
	}
	
	public void refresh() {
		SolrQuery query = SolrGUIQuery.ALL_DOCUMENTS;
		server.refreshDocuments(query);
		table.refresh();
		refreshStatusLine();
	}
	
	public void commit() {
		server.commit();
		table.refresh();
		refreshStatusLine();
	}
	
	public void clear() {
		server.clear();
		table.refresh();
		refreshStatusLine();
	}
	
}
