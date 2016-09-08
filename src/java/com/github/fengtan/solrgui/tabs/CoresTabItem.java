package com.github.fengtan.solrgui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.solrgui.tables.CoresTable;
import com.github.fengtan.solrgui.tables.DocumentsTable;
import com.github.fengtan.solrgui.toolbars.CoresToolbar;

public class CoresTabItem extends CTabItem {
	
	private CoresToolbar toolbar;
	private CoresTable table;
	
	// TODO what if admin/cores is not available ? what if available on a different path ?
	public CoresTabItem(CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		setText("Cores");
		
		// Prepare layout.
		Composite composite = new Composite(getParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		setControl(composite);
		
		// Add toolbar and table.
		toolbar = new CoresToolbar(composite);
		table = new CoresTable(composite);
		
		// Pack.
		composite.pack();
	}
	
	@Override
	public void dispose() {
		toolbar.finalize();
		super.dispose();
	}
	
	// TODO needed ? ugly
	public CoresTable getTable() {
		return table;
	}
	
}
