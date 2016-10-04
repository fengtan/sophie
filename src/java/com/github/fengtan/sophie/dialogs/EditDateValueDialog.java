/**
 * Sophie - A Solr browser and administration tool
 * 
 * Copyright (C) 2016 fengtan<https://github.com/fengtan>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.fengtan.sophie.dialogs;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

public class EditDateValueDialog extends EditValueDialog {

    private Date defaultValue;
    private Calendar calendar = Calendar.getInstance();

    public EditDateValueDialog(Shell shell, Date defaultValue) {
        super(shell);
        this.defaultValue = defaultValue;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        calendar.setTime(defaultValue);

        final DateTime datePicker = new DateTime(composite, SWT.CALENDAR);
        datePicker.setYear(calendar.get(Calendar.YEAR));
        datePicker.setMonth(calendar.get(Calendar.MONTH));
        datePicker.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
            }
        });

        final DateTime timePicker = new DateTime(composite, SWT.TIME);
        timePicker.setHours(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinutes(calendar.get(Calendar.MINUTE));
        timePicker.setSeconds(calendar.get(Calendar.SECOND));
        timePicker.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHours());
                calendar.set(Calendar.MINUTE, timePicker.getMinutes());
                calendar.set(Calendar.SECOND, timePicker.getSeconds());
            }
        });

        return composite;
    }

    @Override
    protected Object fetchValue() {
        return calendar.getTime();
    }

}
