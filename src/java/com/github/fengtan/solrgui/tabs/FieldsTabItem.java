package com.github.fengtan.solrgui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class FieldsTabItem extends CTabItem {
	
	private String[] columnNames = new String[]{
		"Field", "Type", "Indexed", "Stored"
		// TODO and probably other properties
		// TODO add filter to get only indexed/stored fields
		// TODO sort by field name
	};

	public FieldsTabItem(CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());

		setText("Fields");
		
		// Prepare layout.
		Composite composite = new Composite(getParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		setControl(composite);
		
		// Add table.
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL;

		final Table table = new Table(composite, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		for (String columnName:columnNames) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(columnName);
			column.pack(); // TODO needed ? might be worth to setLayout() to get rid of this
		}
		
		// Pack.
		composite.pack();
	}
	
}
