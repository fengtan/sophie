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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog to collect a value.
 */
public abstract class EditValueDialog extends Dialog {

    /**
     * Value to be collected.
     */
    private Object value = null;

    /**
     * Create a new dialog to collect a value.
     * 
     * @param shell
     *            Shell.
     */
    public EditValueDialog(Shell shell) {
        super(shell);
        // Allow user to resize the dialog window.
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Edit value");
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            // Populate value with what the user provided.
            value = fetchValue();
        }
        super.buttonPressed(buttonId);
    }

    /**
     * Collect value.
     * 
     * @return Value to be collected.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Get value provided by the user.
     * 
     * @return Value provided by the user.
     */
    protected abstract Object fetchValue();

}
