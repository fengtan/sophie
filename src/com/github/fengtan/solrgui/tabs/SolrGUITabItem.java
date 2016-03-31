package com.github.fengtan.solrgui.tabs;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import com.github.fengtan.solrgui.solr.SolrGUIServer;
import com.github.fengtan.solrgui.tables.SolrGUITable;

// TODO icon in ubuntu launcher
// TODO license
// TODO sort by ID field by default ? so rows remain the same when modify one
// TODO if server empty and click on table viewer -> seems to crash
public class SolrGUITabItem extends CTabItem {

	private SolrGUIServer server;
	private NatTable table;
	
	public SolrGUITabItem(CTabFolder tabFolder, SolrGUIServer server) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		this.server = server;
		setText(server.getName());
		setToolTipText(server.getURL().toString());

		// Add table.
		table = new SolrGUITable(getParent(), server);
		setControl(table);
		
		// Set focus on this tab.
		tabFolder.setSelection(this);
		tabFolder.forceFocus();
	}
	
	@Override
	public void dispose() {
		server.dispose(); // TODO since we dispose server here, it would make sense to create the object in this class
		table.dispose();
		super.dispose();
	}
	
}
