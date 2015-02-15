package com.github.fengtan.solrgui;
import org.apache.solr.common.SolrDocument;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class SolrGUIServerSorter extends ViewerSorter {

	private String field;
	private static boolean ascending;
	private static String currentField = null; // Data are currently sorted by this field

	public SolrGUIServerSorter(String field) {
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
		SolrDocument document1 = (SolrDocument) o1;
		SolrDocument document2 = (SolrDocument) o2;
		String value1 = document1.getFieldValue(field).toString();
		String value2 = document2.getFieldValue(field).toString();
		// TODO null safe
		return ascending ? value1.compareTo(value2) : value2.compareTo(value1); 
	}

}