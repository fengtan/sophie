package com.github.fengtan.solrgui.filters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

public class SolrGUIFilterBar extends Composite {

	public SolrGUIFilterBar(Composite parent) {
		super(parent, SWT.NULL);
		setLayout(new RowLayout(SWT.VERTICAL));
	}

}
