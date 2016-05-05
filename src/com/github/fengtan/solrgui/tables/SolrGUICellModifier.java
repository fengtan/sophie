package com.github.fengtan.solrgui.tables;
import java.util.Objects;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;
import com.github.fengtan.solrgui.beans.SolrGUIServer;

public class SolrGUICellModifier implements ICellModifier {

	private SolrGUIServer server;
	
	public SolrGUICellModifier(SolrGUIServer server) {
		super();
		this.server = server;
	}

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
		SolrGUIDocument document = (SolrGUIDocument) element;
		return Objects.toString(document.getFieldValue(columnName), "");
	}

	/**
	 * @Override
	 * 
	 * If user has set a new value, then flag the document as Modified.
	 */
	public void modify(Object element, String columnName, Object value) {
		TableItem item = (TableItem) element;
		SolrGUIDocument document = (SolrGUIDocument) item.getData();
		String oldValue = Objects.toString(document.getFieldValue(columnName), "");
		String newValue = value.toString();
		if (!newValue.equals(oldValue)) {
			document.setField(columnName, value.toString());
			server.documentChanged(document);
		}
	}
}