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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import com.github.fengtan.sophie.Sophie;

/**
 * Dialog to display an exception.
 */
public class ExceptionDialog extends Dialog {

    /**
     * Dialog title.
     */
    private static final String TITLE = "An error happened";

    /**
     * Button to let the user display the stack trace.
     */
    private Button detailsButton;

    /**
     * The throwable we want to display.
     */
    private Throwable throwable;

    /**
     * A list widget to hold the lines of the stack trace.
     */
    private List list;

    /**
     * Whether the list widget has been populated with the stack trace.
     */
    private boolean listCreated = false;

    /**
     * Convenience method to create and open a new dialog.
     * 
     * @param shell
     *            Shell.
     * @param throwable
     *            Throwable.
     */
    public static void open(Shell shell, Throwable throwable) {
        new ExceptionDialog(shell, throwable).open();
    }

    /**
     * Create a new dialog to display an exception.
     * 
     * @param shell
     *            Shell.
     * @param throwable
     *            Throwable.
     */
    private ExceptionDialog(Shell shell, Throwable throwable) {
        super(shell);
        this.throwable = throwable;
        setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
        Sophie.log.error(TITLE, throwable);
    }

    @Override
    protected void buttonPressed(int id) {
        if (id == IDialogConstants.DETAILS_ID) {
            // User clicked on the 'Details' button: show/hide stack trace.
            toggleDetails();
        } else {
            super.buttonPressed(id);
        }
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(TITLE);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        detailsButton = createButton(parent, IDialogConstants.DETAILS_ID, IDialogConstants.SHOW_DETAILS_LABEL, false);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        ((GridLayout) composite.getLayout()).numColumns = 2;
        
        // Create image.
        Image image = parent.getDisplay().getSystemImage(SWT.ICON_ERROR);
        Label labelImg = new Label(composite, SWT.WRAP);
        labelImg.setImage(image);

        // Create label with the exception's message.
        Label labelTxt = new Label(composite, SWT.WRAP);
        labelTxt.setText(throwable.getMessage());
        GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        labelTxt.setLayoutData(data);

        return composite;
    }

    /**
     * Create a list widget and populate it with the stack trace.
     * 
     * @param parent
     *            Parent composite.
     * @return List of lines made of the exception's stack trace.
     */
    private List createDropDownList(Composite parent) {
        list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        for (String line : ExceptionUtils.getFullStackTrace(throwable).split("\n")) {
            list.add(line);
        }
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
        data.heightHint = list.getItemHeight() * 7;
        list.setLayoutData(data);
        listCreated = true;
        return list;
    }

    /**
     * Show/hide the stack trace.
     */
    private void toggleDetails() {
        Point windowSize = getShell().getSize();
        Point oldSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);

        if (listCreated) {
            list.dispose();
            listCreated = false;
            detailsButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
        } else {
            list = createDropDownList((Composite) getContents());
            detailsButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
        }

        Point newSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);

        getShell().setSize(new Point(windowSize.x, windowSize.y + (newSize.y - oldSize.y)));
    }

}