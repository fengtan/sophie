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
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog with a drop-down list.
 */
public class CComboDialog extends Dialog {

    /**
     * Shell.
     */
    private Shell shell;

    /**
     * Title of the dialog.
     */
    private String title;

    /**
     * Label displayed in the dialog.
     */
    private String text;

    /**
     * Items listed in the drop-down list.
     */
    private String[] items;

    /**
     * Validates the value provided by the user.
     */
    private IInputValidator validator;

    /**
     * Drop-down list.
     */
    private Combo combo;

    /**
     * Value provided by the user.
     */
    private String value = null;

    /**
     * Create a new dialog with a drop-down list.
     * 
     * @param shell
     *            Shell.
     * @param title
     *            Title of the dialog.
     * @param text
     *            Label displayed in the dialog.
     * @param items
     *            Items listed in the drop-down list.
     * @param validator
     *            Validates the value provided by the user.
     */
    public CComboDialog(Shell shell, String title, String text, String[] items, IInputValidator validator) {
        super(shell);
        this.shell = shell;
        this.title = title;
        this.text = text;
        this.items = items;
        this.validator = validator;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        // Create label.
        Label label = new Label(composite, SWT.WRAP);
        GridData grid = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        grid.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        label.setText(text);
        label.setLayoutData(grid);
        label.setFont(parent.getFont());

        // Create drop-down list.
        // TODO add some spacing
        combo = new Combo(parent, SWT.SINGLE | SWT.BORDER);
        combo.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        combo.setItems(items);

        applyDialogFont(composite);
        return composite;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(title);
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            // Validate user-provided value.
            if (validator != null) {
                String errorMessage = validator.isValid(combo.getText());
                if (errorMessage != null) {
                    MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                    messageBox.setText("Invalid value");
                    messageBox.setMessage(errorMessage);
                    messageBox.open();
                    return;
                }
            }
            // Set dialog value from user-provided value.
            value = combo.getText();
        }
        super.buttonPressed(buttonId);
    }

    /**
     * Get user-provided value.
     * 
     * @return User-provided value.
     */
    public String getValue() {
        return value;
    }

}
