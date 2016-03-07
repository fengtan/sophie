package com.github.fengtan.solrgui.tabs;

import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;
import com.github.fengtan.solrgui.beans.SolrGUIQuery;
import com.github.fengtan.solrgui.beans.SolrGUIServer;
import com.github.fengtan.solrgui.sidebar.SolrGUIFilter;
import com.github.fengtan.solrgui.sidebar.SolrGUISidebar;
import com.github.fengtan.solrgui.statusline.SolrGUIStatusLine;
import com.github.fengtan.solrgui.tables.SolrGUITable;

// TODO icon in ubuntu launcher
// TODO license
// TODO sort by ID field by default ? so rows remain the same when modify one
// TODO if server empty and click on table viewer -> seems to crash
public class SolrGUITabItem extends CTabItem {

	private Composite sidebar;
	
	private SolrGUIServer server;
	private Set<SolrGUIFilter> filters = new HashSet<SolrGUIFilter>();
	private SolrGUITable table; // TODO Composite ?
	private SolrGUIStatusLine statusLine; // TODO Composite ?
	
	public SolrGUITabItem(CTabFolder tabFolder, SolrGUIServer server) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		this.server = server;
		setText(server.getName());
		setToolTipText(server.getURL().toString());
		
		// Fill in tab.
		Composite tabComposite = new Composite(getParent(), SWT.NULL);
		tabComposite.setLayout(new GridLayout());
		sidebar = new SolrGUISidebar(tabComposite);
		addFilter();
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

	public void addFilter() {
		filters.add(new SolrGUIFilter(sidebar, server, this)); // TODO passing this is ugly
		sidebar.getParent().pack(); // TODO needed ?
	}
	
	public void removeFilter(SolrGUIFilter filter) {
		filters.remove(filter);
		filter.dispose();
		sidebar.getParent().pack(); // TODO needed ?
	}
	
	public Set<SolrGUIFilter> getFilters() {
		return filters;
	}
	
		
	// TODO not updated when clicking 'refresh'
	// TODO not updated when launching app
	protected void refreshStatusLine() {
		statusLine.setText(table.getItemCount() + " documents");// TODO "XX additions, XX deletions, XX modifications"
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
		SolrQuery query = new SolrGUIQuery(filters, table.getFieldsDisplayed());
		server.refreshDocuments(query);
		table.refreshColumns();
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
