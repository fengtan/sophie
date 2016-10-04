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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class EditTextValueDialog extends EditValueDialog {

	private String defaultValue;
	private Text text;
	
	public EditTextValueDialog(Shell shell, String defaultValue) {
		super(shell);
		this.defaultValue = defaultValue;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		text = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setText(defaultValue);
		GridData grid = new GridData();
		grid.widthHint = 500;
		grid.heightHint = 50;
		grid.horizontalAlignment = GridData.FILL;;
		grid.verticalAlignment = GridData.FILL;;
		grid.grabExcessHorizontalSpace = true;
		grid.grabExcessVerticalSpace = true;
		text.setLayoutData(grid);
		return composite;
	}

	@Override
	protected Object fetchValue() {
		return text.getText();
	}

}
