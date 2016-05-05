package com.github.fengtan.solrgui.tables;
import java.util.Objects;

import org.apache.solr.common.SolrDocument;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

public class SolrGUICellModifier implements ICellModifier {

	/**
	 * @Override
	 * 
	 * All cells may be modified.
	 */
	public boolean canModify(Object element, String columnName) {
		return true;
	}

	/**
	 * @Override
	 */
	public Object getValue(Object element, String columnName) {
		SolrDocument document = (SolrDocument) element;
		return Objects.toString(document.getFieldValue(columnName), "");
	}

	/**
	 * @Override
	 */
	public void modify(Object element, String columnName, Object value) { // TODO needed ?
		TableItem item = (TableItem) element;
		SolrDocument document = (SolrDocument) item.getData();
		String oldValue = Objects.toString(document.getFieldValue(columnName), "");
		String newValue = value.toString();
		if (!newValue.equals(oldValue)) {
			document.setField(columnName, value.toString());
		}
	}
}