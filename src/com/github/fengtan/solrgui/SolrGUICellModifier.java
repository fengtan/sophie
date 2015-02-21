package com.github.fengtan.solrgui;
import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

public class SolrGUICellModifier implements ICellModifier {
	private SolrGUI solrGUI;
	
	public SolrGUICellModifier(SolrGUI solrGUI) {
		super();
		this.solrGUI = solrGUI;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 * 
	 * All Solr fields can be modified. The others cannot.
	 */
	public boolean canModify(Object element, String columnName) {
		return Arrays.asList(solrGUI.getServer().getFields()).contains(columnName);
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String columnName) {
		SolrGUIDocument document = (SolrGUIDocument) element;
		return Objects.toString(document.getFieldValue(columnName), "");
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String columnName, Object value) {
		TableItem item = (TableItem) element;
		SolrGUIDocument document = (SolrGUIDocument) item.getData();
		document.setField(columnName, value.toString());
		solrGUI.getServer().documentChanged(document);
		// TODO do not mark as Modified if new value = old value
	}
}