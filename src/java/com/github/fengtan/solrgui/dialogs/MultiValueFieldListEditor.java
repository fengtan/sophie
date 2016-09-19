package com.github.fengtan.solrgui.dialogs;

import java.util.AbstractList;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.github.fengtan.solrgui.SolrGUI;

public class MultiValueFieldListEditor extends ListEditor {

	public MultiValueFieldListEditor(Composite parent, AbstractList values) {
		super(StringUtils.EMPTY, StringUtils.EMPTY, parent);
    	List list = getListControl(parent);
        if (list != null) {
        	for (Object value:values) {
        		list.add(Objects.toString(value, StringUtils.EMPTY));
        	}
        }
	}
	
	@Override
	protected String createList(String[] items) {
		// Do nothing - we don't use the eclipse/jface preferences system.
		return null;
	}

	@Override
	protected String getNewInputObject() {
        InputDialog input = new InputDialog(SolrGUI.shell, "New value", "New value:", StringUtils.EMPTY, null);
        input.open();
        return input.getValue();
	}

	@Override
	protected String[] parseString(String stringList) {
		// Do nothing - we don't use the eclipse/jface preferences system.
		return null;
	}
    
}
