package com.github.fengtan.solrgui.dialogs;

import java.util.AbstractList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.SolrGUI;

public class DocumentEditValueDialog extends Dialog {
	
	private Object oldValueObject;
	private String oldValueString;
	private Text newValue;
	private TableItem item;
	private int columnIndex;
	
	public DocumentEditValueDialog(Shell parentShell) {
		super(parentShell);
		// Allow user to resize the dialog window.
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(1, false));
		
		new Label(composite, SWT.NULL).setText("New value:");
		
		// Add text to let the user set the new value.
		newValue = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		newValue.setText(oldValueString);
		GridData grid = new GridData();
		grid.widthHint = 500;
		grid.heightHint = 50;
		grid.horizontalAlignment = GridData.FILL;;
		grid.verticalAlignment = GridData.FILL;;
		grid.grabExcessHorizontalSpace = true;
		grid.grabExcessVerticalSpace = true;
		newValue.setLayoutData(grid);
		
		// Add datepicker if we are dealing with a date field.
		if (oldValueObject instanceof Date) {
			Date oldValueDate = (Date) oldValueObject;
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(oldValueDate);
						
			final DateTime datePicker = new DateTime(composite, SWT.CALENDAR);
			datePicker.setYear(calendar.get(Calendar.YEAR));
			datePicker.setMonth(calendar.get(Calendar.MONTH));
			datePicker.setDay(calendar.get(Calendar.DAY_OF_MONTH));
			datePicker.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					calendar.set(Calendar.YEAR, datePicker.getYear());
					calendar.set(Calendar.MONTH, datePicker.getMonth());
					calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
					newValue.setText(calendar.getTime().toString());
			    }
			});

			final DateTime timePicker = new DateTime(composite, SWT.TIME);
			timePicker.setHours(calendar.get(Calendar.HOUR_OF_DAY));
			timePicker.setMinutes(calendar.get(Calendar.MINUTE));
			timePicker.setSeconds(calendar.get(Calendar.SECOND));
			timePicker.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHours());
					calendar.set(Calendar.MINUTE, timePicker.getMinutes());
					calendar.set(Calendar.SECOND, timePicker.getSeconds());
					newValue.setText(calendar.getTime().toString());
				}
			});
			// TODO cannot commit dates edited by datepicker
		}
		
		// Add list widget if we are dealing with a multi-valued field.
		// List is not iterable - we need to use AbstractList.
		if (oldValueObject instanceof AbstractList) {
			Composite listComposite = new Composite(composite, SWT.NULL);
			listComposite.setLayout(new RowLayout());
			AbstractList oldValueList = (AbstractList) oldValueObject;
			// TODO support editing values http://sandipchitale.blogspot.ca/2008/09/enhanced-listeditor-implementation.html
			// TODO seems to be buggy when many values.
			final ListViewer listViewer = new ListViewer(listComposite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER);
			for (Object value:oldValueList) {
				listViewer.add(Objects.toString(value, StringUtils.EMPTY));	
			}
			
			Composite buttonsComposite = new Composite(listComposite, SWT.NULL);
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
		}

		// TODO hide Text if Date/List field?
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
			// TODO move StringUtils.equals() into table.updateDocument() ?
			if (!StringUtils.equals(oldValueString, newValue.getText())) {
				SolrGUI.tabFolder.getDocumentsTabItem().getTable().updateDocument(item, columnIndex, newValue.getText());
			}
		}
		super.buttonPressed(buttonId);
	}

	// TODO cannot edit empty values (window closes)
	public int open(Object oldValue, TableItem item, int columnIndex) {
		this.oldValueObject = oldValue;
		this.oldValueString = Objects.toString(oldValue, StringUtils.EMPTY);
		this.item = item;
		this.columnIndex = columnIndex;
		return super.open();
	}
	
}
