package com.github.fengtan.solrgui;

import java.util.Objects;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SolrGUILabelProvider extends LabelProvider implements ITableLabelProvider {

	// TODO ideally we should not need this in a LabelProvider.
	private SolrGUIServer server;
	
	public SolrGUILabelProvider(SolrGUIServer server) {
		this.server = server;
	}
	
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
				String[] fields = server.getFields();
				if (fieldIndex < fields.length) {
					String field = fields[fieldIndex];
					return Objects.toString(document.getFieldValue(field), "");	
				} else {
					return "";
				}
		}
	}
	
	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

}