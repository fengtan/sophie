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
 * Dialog to collect two values.
 */
public class DoubleInputDialog extends Dialog {

    /**
     * Title of the dialog.
     */
    private String title;

    /**
     * Label of the first widget.
     */
    private String text1;

    /**
     * Label of the second widget.
     */
    private String text2;

    /**
     * Default value of the first widget.
     */
    private String defaultValue1;

    /**
     * Default value of the second widget.
     */
    private String defaultValue2;

    /**
     * First widget.
     */
    private Text input1;

    /**
     * Second widget.
     */
    private Text input2;

    /**
     * Value of the first widget.
     */
    private String value1 = null;

    /**
     * Value of the second widget.
     */
    private String value2 = null;

    /**
     * Create a new dialog to collect two values.
     * 
     * @param shell
     *            Shell.
     * @param title
     *            Title of the dialog.
     * @param text1
     *            Label of the first widget.
     * @param defaultValue1
     *            Default value of the first widget.
     * @param text2
     *            Label of the second widget.
     * @param defaultValue2
     *            Default value of the second widget.
     */
    public DoubleInputDialog(Shell shell, String title, String text1, String defaultValue1, String text2, String defaultValue2) {
        super(shell);
        this.title = title;
        this.text1 = text1;
        this.text2 = text2;
        this.defaultValue1 = defaultValue1;
        this.defaultValue2 = defaultValue2;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(title);
    }

    /**
     * Get value of the first widget.
     * 
     * @return Value of the first widget.
     */
    public String getValue1() {
        return value1;
    }

    /**
     * Get value of the second widget.
     * 
     * @return Value of the second widget.
     */
    public String getValue2() {
        return value2;
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            // Populate dialog values with what the user entered.
            value1 = input1.getText();
            value2 = input2.getText();
        }
        super.buttonPressed(buttonId);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        // Create grid.
        GridData grid = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        grid.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);

        // Create first widget.
        Label label1 = new Label(composite, SWT.WRAP);
        label1.setText(text1);
        label1.setLayoutData(grid);
        label1.setFont(parent.getFont());
        input1 = new Text(composite, SWT.SINGLE | SWT.BORDER);
        input1.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        input1.setText(defaultValue1);

        // Create second widget.
        Label label2 = new Label(composite, SWT.WRAP);
        label2.setText(text2);
        label2.setLayoutData(grid);
        label2.setFont(parent.getFont());
        input2 = new Text(composite, SWT.SINGLE | SWT.BORDER);
        input2.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        input2.setText(defaultValue2);

        applyDialogFont(composite);
        return composite;
    }

}
