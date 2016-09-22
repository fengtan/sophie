package com.github.fengtan.sophie.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.sophie.Sophie;
import com.github.fengtan.sophie.beans.SophieException;
import com.github.fengtan.sophie.tables.DocumentsTable;
import com.github.fengtan.sophie.toolbars.DocumentsToolbar;

public class DocumentsTabItem extends CTabItem {

	private DocumentsToolbar toolbar;
	private DocumentsTable table;
	
	public DocumentsTabItem(CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());

		setText("Documents");
		
		// Prepare layout.
		Composite composite = new Composite(getParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		setControl(composite);
		
		// Add toolbar and table.
		toolbar = new DocumentsToolbar(composite);
		try {
			table = new DocumentsTable(composite, toolbar, toolbar);
			toolbar.setTable(table);	
		} catch (SophieException e) {
			Sophie.showException(composite.getShell(), new SophieException("Unable to initialize documents table", e));
		}
		
		composite.pack();
	}
	
	@Override
	public void dispose() {
		toolbar.finalize();
		super.dispose();
	}
	
}
