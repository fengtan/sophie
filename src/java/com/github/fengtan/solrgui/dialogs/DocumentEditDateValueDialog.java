package com.github.fengtan.solrgui.dialogs;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import com.github.fengtan.solrgui.SolrGUI;

public class DocumentEditDateValueDialog extends DocumentEditValueDialog {

	private Date defaultValue;
	private Calendar calendar = Calendar.getInstance();
	
	public DocumentEditDateValueDialog(Date defaultdValue) {
		super(SolrGUI.shell);
		this.defaultValue = defaultdValue;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		calendar.setTime(defaultValue);
					
		final DateTime datePicker = new DateTime(composite, SWT.CALENDAR);
		datePicker.setYear(calendar.get(Calendar.YEAR));
		datePicker.setMonth(calendar.get(Calendar.MONTH));
		datePicker.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		datePicker.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				calendar.set(Calendar.YEAR, datePicker.getYear());
				calendar.set(Calendar.MONTH, datePicker.getMonth());
				calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
				// TODO update solr document
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
				// TODO update solr document
			}
		});
		// TODO cannot commit dates edited by datepicker
		
		return composite;
	}

	@Override
	protected Object getValue() {
		return calendar.getTime();
	}

}
