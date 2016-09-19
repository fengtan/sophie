package com.github.fengtan.solrgui.dialogs;

import java.util.AbstractList;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.github.fengtan.solrgui.SolrGUI;

public class DocumentEditListValueDialog extends DocumentEditValueDialog {

	private AbstractList defaultValue;
	private ListViewer listViewer;
	
	// List is not iterable - we need to use AbstractList.
	public DocumentEditListValueDialog(AbstractList defaultValue) {
		super(SolrGUI.shell);
		this.defaultValue = defaultValue;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new RowLayout());

		// TODO support editing values http://sandipchitale.blogspot.ca/2008/09/enhanced-listeditor-implementation.html
		// TODO seems to be buggy when many values.
		listViewer = new ListViewer(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER);
		for (Object value:defaultValue) {
			listViewer.add(Objects.toString(value, StringUtils.EMPTY));	
		}
		
		Composite buttonsComposite = new Composite(composite, SWT.NULL);
		buttonsComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		Button buttonAdd = new Button(buttonsComposite, SWT.PUSH);
	    buttonAdd.setText("Add");
	    buttonAdd.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent e) {
	    		InputDialog input = new InputDialog(SolrGUI.shell, "Add value", "Add value:", StringUtils.EMPTY, null);
	    		input.open();
	    		// button "OK' has ID "0".
	    		if (input.getReturnCode() == 0) {
		    		listViewer.add(input.getValue());
		    		// TODO update Text
	    		}
	    	}
		});
		
	    // TODO disable remove when no value selected.
	    Button buttonRemove = new Button(buttonsComposite, SWT.PUSH);
	    buttonRemove.setText("Remove");
	    buttonRemove.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent e) {
	    		IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
	    		if (!selection.isEmpty()) {
	    			listViewer.remove(selection.toArray());
		    		// TODO update Text	
	    		}
	    	}
		});
				
		// TODO cannot commit lists edited by listviewer
	    
	    return composite;
	}

	@Override
	protected Object getValue() {
		return listViewer.getList(); // TODO test not updating the list does not turn the line yellow (and vice-versa)
	}


}
