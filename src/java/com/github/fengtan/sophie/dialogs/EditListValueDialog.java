package com.github.fengtan.sophie.dialogs;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Shell;

public class EditListValueDialog extends EditValueDialog {

	private AbstractList<?> defaultValue;
	private ListViewer listViewer;
	
	// List is not iterable - we need to use AbstractList.
	public EditListValueDialog(Shell shell, AbstractList<?> defaultValue) {
		super(shell);
		this.defaultValue = (defaultValue == null) ? new ArrayList<Object>() : defaultValue;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
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
	    		InputDialog input = new InputDialog(parent.getShell(), "Add value", "Add value:", null, null);
	    		input.open();
	    		if (input.getReturnCode() == IDialogConstants.OK_ID) {
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
		
	    return composite;
	}

	@Override
	protected Object fetchValue() {
		String[] items = listViewer.getList().getItems();
		return Arrays.asList(items);
	}


}
