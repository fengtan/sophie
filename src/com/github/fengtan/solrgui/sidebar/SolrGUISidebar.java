package com.github.fengtan.solrgui.sidebar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

public class SolrGUISidebar extends Composite {

	public SolrGUISidebar(Composite parent) {
		super(parent, SWT.NULL);
		setLayout(new RowLayout(SWT.VERTICAL));
	}

}
