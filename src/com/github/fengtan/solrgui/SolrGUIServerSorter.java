package com.github.fengtan.solrgui;
import org.apache.solr.common.SolrDocument;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class SolrGUIServerSorter extends ViewerSorter {

	private String field;

	public SolrGUIServerSorter(String field) {
		super();
		this.field = field;
	}

	public int compare(Viewer viewer, Object o1, Object o2) {
		SolrDocument document1 = (SolrDocument) o1;
		SolrDocument document2 = (SolrDocument) o2;
		String value1 = document1.getFieldValue(field).toString();
		String value2 = document2.getFieldValue(field).toString();
		return value1.compareTo(value2); // TODO null safe
	}

}