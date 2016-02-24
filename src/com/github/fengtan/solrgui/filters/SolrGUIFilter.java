package com.github.fengtan.solrgui.filters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.beans.SolrGUIServer;

// TODO refresh fields when hit button 'refresh' ?
public class SolrGUIFilter extends Composite {

	private Combo combo;
	private Text text;
	
	public SolrGUIFilter(Composite parent, SolrGUIServer server) {
		super(parent, SWT.SHADOW_ETCHED_IN);
		
		setLayout(new FillLayout(SWT.HORIZONTAL));

		combo = new Combo(this, SWT.SIMPLE | SWT.BORDER);
		combo.setItems(server.getFields());
		combo.setText("Filter by");
		
	    text = new Text(this, SWT.BORDER);	
	}
	
	public String getField() {
		int index = combo.getSelectionIndex();
		return (index == -1) ? "" : combo.getItem(index);
	}
	
	public String getValue() {
		return text.getText();
	}

}
