package com.github.fengtan.solrgui;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SolrGUILabelProvider extends LabelProvider implements ITableLabelProvider {

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		SolrGUIDocument document = (SolrGUIDocument) element;
		switch (columnIndex) {
			case 0: // Change status.
				// TODO tooltips A/D/M
				switch(document.getChange()) {
					case ADDED:
						return "A";
					case DELETED:
						return "D";
					case NONE:
						return "";
					case UPDATED:
						return "M";
				}
			default: // Solr fields.
				int fieldIndex = columnIndex-1;
				List<String> fields = getFields(document);
				if (fieldIndex < fields.size()) {
					String field = fields.get(fieldIndex);
					// TODO Objects.toString(myvalue, "") ? null safe
					return document.getFieldValue(field).toString();	
				} else {
					return "";
				}
		}
	}
	
	// TODO re-use server.getFields() + SolrGUI.getColumnNames() ?
	public List<String> getFields(SolrGUIDocument document) {
		Collection<String> fields = document.getFieldNames();
		return Arrays.asList(fields.toArray(new String[fields.size()]));	
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

}