package com.github.fengtan.solrgui;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SolrGUILabelProvider extends LabelProvider implements ITableLabelProvider {

	// Names of images used to represent checkboxes
	public static final String CHECKED_IMAGE 	= "checked";
	public static final String UNCHECKED_IMAGE  = "unchecked";

	// For the checkbox images
	private static ImageRegistry imageRegistry = new ImageRegistry(); // TODO

	/**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */ 
	static {
		String iconPath = "icons/"; 
		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(SolrGUI.class, iconPath + CHECKED_IMAGE + ".gif"));
		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(SolrGUI.class, iconPath + UNCHECKED_IMAGE + ".gif"));	
	}
	

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		SolrDocument document = (SolrDocument) element;
		switch (columnIndex) {
			case 0: // Modified
				// TODO result = document.isModified() ? "X" : "";
				return "";
			default:
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
	public List<String> getFields(SolrDocument document) {
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