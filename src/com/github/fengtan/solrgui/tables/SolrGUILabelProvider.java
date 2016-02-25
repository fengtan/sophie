package com.github.fengtan.solrgui.tables;

import java.util.List;
import java.util.Objects;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;

public class SolrGUILabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {

	private static final Color GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	private static final Color RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	private static final Color YELLOW = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
	
	private List<String> fieldsDisplayed;
	
	public SolrGUILabelProvider(List<String> fieldsDisplayed) {
		this.fieldsDisplayed = fieldsDisplayed;
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		SolrGUIDocument document = (SolrGUIDocument) element;
		if (columnIndex < fieldsDisplayed.size()) {
			return Objects.toString(document.getFieldValue(fieldsDisplayed.get(columnIndex)), "");	
		} else {
			return "";
		}
	}
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getBackground(Object element) {
		SolrGUIDocument document = (SolrGUIDocument) element;
		switch (document.getStatus()) {
			case ADDED:
				return GREEN;
			case DELETED:
				return RED;
			case MODIFIED:
				return YELLOW;
			default:
				return null;
		}
	}

	@Override
	public Color getForeground(Object element) {
		return null;
	}

}