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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog to collect multiple values.
 */
public class MultipleInputDialog extends Dialog {

    /**
     * Dialog title.
     */
    private String title;

    /**
     * Widget labels.
     */
    private String[] texts;

    /**
     * Widget default values.
     */
    private String[] defaultValues;

    /**
     * Widgets.
     */
    private Text[] inputs;

    /**
     * Widget values.
     */
    private String[] values = null;

    /**
     * Create a new dialog to collect multiple values.
     * 
     * @param shell
     *            Shell.
     * @param title
     *            Title of the dialog.
     * @param texts
     *            Widget labels.
     * @param defaultValues
     *            Widget default values.
     */
    public MultipleInputDialog(Shell shell, String title, String[] texts, String[] defaultValues) {
        super(shell);
        this.title = title;
        this.texts = texts;
        this.defaultValues = defaultValues;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(title);
    }

    /**
     * Get one of the widget values.
     * 
     * @param i
     *            Widget index.
     * @return Widget value.
     */
    public String getValue(int i) {
        return values[i];
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            // Populate dialog values with what the user entered.
            values = new String[inputs.length];
            for (int i = 0; i < inputs.length; i++) {
                values[i] = inputs[i].getText();
            }
        }
        super.buttonPressed(buttonId);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        // Create grid.
        GridData grid = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        grid.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);

        // Create widgets.
        inputs = new Text[texts.length];
        for (int i = 0; i < texts.length; i++) {
            Label label = new Label(composite, SWT.WRAP);
            label.setText(texts[i]);
            label.setLayoutData(grid);
            label.setFont(parent.getFont());
            inputs[i] = new Text(composite, SWT.SINGLE | SWT.BORDER);
            inputs[i].setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
            if (i < defaultValues.length) {
                inputs[i].setText(defaultValues[i]);
            }
        }

        applyDialogFont(composite);
        return composite;
    }

}
