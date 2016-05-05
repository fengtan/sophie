package com.github.fengtan.solrgui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.solrgui.tables.SolrGUITable;

public class SolrGUITabItem extends CTabItem {

	private SolrGUITable table; // TODO Composite ?
	
	public SolrGUITabItem(CTabFolder tabFolder, String url) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		setText(url);
		
		// Fill in tab.
		Composite tabComposite = new Composite(getParent(), SWT.NULL);
		tabComposite.setLayout(new GridLayout());
		table = new SolrGUITable(tabComposite, url);
		setControl(tabComposite); // TODO need composite ? TODO add status line ?
		
		// Set focus on this tab.
		tabFolder.setSelection(this);
		tabFolder.forceFocus();
	}
	
	@Override
	public void dispose() {
		table.dispose();
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
	*/
	
}
