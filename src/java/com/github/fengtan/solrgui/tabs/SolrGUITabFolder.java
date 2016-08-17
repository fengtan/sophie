package com.github.fengtan.solrgui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SolrGUITabFolder extends CTabFolder {
	
	public SolrGUITabFolder(Shell shell) {
		// Create the tabs.
		super(shell, SWT.TOP | SWT.CLOSE | SWT.BORDER);

		// Configure tab folder.
		setBorderVisible(true);
		setLayoutData(new GridData(GridData.FILL_BOTH));
		setSimple(false);
		setTabHeight(25);
		// TODO should not be able to close the tabs

		// Set up a gradient background for the selected tab
		Display display = shell.getDisplay();
		Color titleForeColor = display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
		Color titleBackColor1 = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
		Color titleBackColor2 = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
		setSelectionForeground(titleForeColor);
		setSelectionBackground(
		    new Color[] {titleBackColor1, titleBackColor2},
			new int[] {100},
			true
		);
		
		// Initialize tabs from config file.
		new SolrGUITabItem(this);
	}

}
