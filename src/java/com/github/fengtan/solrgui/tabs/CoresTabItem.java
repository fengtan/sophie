package com.github.fengtan.solrgui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.solrgui.SolrGUI;
import com.github.fengtan.solrgui.tables.CoresTable;

public class CoresTabItem extends CTabItem {
	
	// TODO what if admin/cores is not available ? what if available on a different path ?
	public CoresTabItem(CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		setText("Cores");
		
		// Prepare layout.
		Composite composite = new Composite(getParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		setControl(composite);
		
		// Add table.
		new CoresTable(composite, SolrGUI.client);
		
		// Pack.
		composite.pack();
	}
	
}
