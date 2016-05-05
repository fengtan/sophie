package com.github.fengtan.solrgui.statusline;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Label in footer - displays number of documents received etc.
 */
public class SolrGUIStatusLine {

	// We cannot subclass Label so we use an attribute.
	private Label label;
	
	public SolrGUIStatusLine(Composite parent) {
		label = new Label(parent, SWT.WRAP);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	}
	
	public void setText(String string) {
		label.setText(string);
	}

}
