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

public class CoreAddDialog extends Dialog {
	
	private static final String DEFAULT_CORE_NAME = "collectionX";
	private static final String DEFAULT_INSTANCE_DIR = "/path/to/solr/collectionX";
	
	private Text textCoreName;
	private Text textInstanceDir;
	
	private String valueCoreName = null;
	private String valueInstanceDir = null;
	
	public CoreAddDialog(Shell shell) {
		super(shell);
	}

	/**
	 * Set title of the custom dialog.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add new core");
	}

	public String getValueCoreName() {
		return valueCoreName;
	}
	
	public String getValueInstanceDir() {
		return valueInstanceDir;
	}


    protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			valueCoreName = textCoreName.getText();
			valueInstanceDir = textInstanceDir.getText();
		}
        super.buttonPressed(buttonId);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        
        GridData grid = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        grid.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        
        Label labelCoreName = new Label(composite, SWT.WRAP);
        labelCoreName.setText("Name:");
        labelCoreName.setLayoutData(grid);
        labelCoreName.setFont(parent.getFont());
        textCoreName = new Text(composite, SWT.SINGLE | SWT.BORDER);
        textCoreName.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        textCoreName.setText(DEFAULT_CORE_NAME);

        Label labelInstanceDir = new Label(composite, SWT.WRAP);
        labelInstanceDir.setText("Instance directory:");
        labelInstanceDir.setLayoutData(grid);
        labelInstanceDir.setFont(parent.getFont());
        textInstanceDir = new Text(composite, SWT.SINGLE | SWT.BORDER);
        textInstanceDir.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));        
        textInstanceDir.setText(DEFAULT_INSTANCE_DIR);
        
        applyDialogFont(composite);
        return composite;
    }

}
