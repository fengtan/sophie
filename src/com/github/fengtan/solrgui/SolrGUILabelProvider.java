package com.github.fengtan.solrgui;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
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
		String result = "";
		SolrDocument document = (SolrDocument) element;
		/* TODO drop
		switch (columnIndex) {
			case 0:
				result = document.isModified() ? "X" : "";
				break;
			case 1 :
				result = document.getDescription();
				break;
			case 2 :
				result = document.getOwner();
				break;
			case 3 :
				result = document.getPercentComplete() + "";
				break;
			default :
				break; 	
		}
		*/
		result = Objects.toString(document.getFieldValue("item_id"), "");
		return result;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

}