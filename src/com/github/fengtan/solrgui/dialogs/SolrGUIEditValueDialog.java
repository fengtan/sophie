package com.github.fengtan.solrgui.dialogs;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.tables.SolrGUITable;

public class SolrGUIEditValueDialog extends Dialog {
	
	private Text newValue;
	private String oldValue;
	private TableItem item;
	private int columnIndex;
	private SolrGUITable table;
	
	public SolrGUIEditValueDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		// TODO support multiple-value fields.
		new Label(composite, SWT.NULL).setText("New value");
		newValue = new Text(composite, SWT.BORDER);
		newValue.setText(oldValue);
		// TODO should update remote document probably
	    
	    return composite;
	}
	
	// Set title of the custom dialog.
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Edit value");
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// button "OK' has ID "0".
		if (buttonId == 0) {
			if (!StringUtils.equals(oldValue, newValue.getText())) {
				item.setText(columnIndex, newValue.getText());
				SolrDocument document = (SolrDocument) item.getData("document");
				document.setField(table.getFields().get(columnIndex).getName(), newValue.getText());
				table.addDocumentUpdated(item);
			}
		}
		super.buttonPressed(buttonId);
	}

	public int open(String oldValue, TableItem item, int columnIndex, SolrGUITable table) {
		this.oldValue = oldValue;
		this.item = item;
		this.columnIndex = columnIndex;
		this.table = table;
		return super.open();
	}
	
}
