package com.github.fengtan.solrgui.tables;
import java.util.Objects;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;

public class SolrGUISorter extends ViewerSorter {

	private String field;
	private static boolean ascending;
	private static String currentField = null; // Data are currently sorted by this field.

	public SolrGUISorter(String field) {
		super();
		this.field = field;
		// If click on new field, then sort ascending.
		// Otherwise, toggle sort.
		if (!field.equals(currentField)) {
			ascending = true;
		} else {
			ascending = !ascending;
		}
		currentField = field;
	}

	public int compare(Viewer viewer, Object o1, Object o2) {
		SolrGUIDocument document1 = (SolrGUIDocument) o1;
		SolrGUIDocument document2 = (SolrGUIDocument) o2;
		String value1 = Objects.toString(document1.getFieldValue(field), "");
		String value2 = Objects.toString(document2.getFieldValue(field), "");
		return ascending ? value1.compareTo(value2) : value2.compareTo(value1); 
	}

}