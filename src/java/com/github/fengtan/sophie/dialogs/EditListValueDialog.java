/**
 * Sophie - A Solr browser and administration tool
 * Copyright (C) 2016 fengtan<https://github.com/fengtan>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
