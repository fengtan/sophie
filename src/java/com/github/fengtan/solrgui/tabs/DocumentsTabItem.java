package com.github.fengtan.solrgui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.solrgui.tables.SolrGUITable;

public class DocumentsTabItem extends CTabItem {

	private SolrGUITable table; // TODO should not be static
	
	public DocumentsTabItem(CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());

		setText("Documents");
		
		// Prepare layout.
		Composite composite = new Composite(getParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		setControl(composite);
		
		// Fill in tab.
		table = new SolrGUITable(composite); // TODO not the best place to instantiate table

		composite.pack();
	}
	
	public SolrGUITable getTable() {
		return table;
	}
	
}
