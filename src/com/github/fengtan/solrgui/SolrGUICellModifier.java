package com.github.fengtan.solrgui;
import org.apache.solr.common.SolrDocument;
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
	 */
	public boolean canModify(Object element, String property) {
		return true;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String columnName) {
		SolrDocument document = (SolrDocument) element;
		return document.getFieldValue(columnName).toString();	
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String columnName, Object value) {	
		TableItem item = (TableItem) element;
		SolrDocument document = (SolrDocument) item.getData();
		document.setField(columnName, value.toString());
		solrGUI.getServer().documentChanged(document);
	}
}