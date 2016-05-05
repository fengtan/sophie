package com.github.fengtan.solrgui.tables;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;
import com.github.fengtan.solrgui.beans.SolrGUIServer;

public class SolrGUITable { // TODO extend Composite ?

	private Table table;
	private TableViewer tableViewer;
	private SolrGUIServer server;

	public SolrGUITable(Composite parent, SolrGUIServer server) {
		this.server = server;
		createTable(parent);
		createTableViewer();
	}
	
	/**
	 * Create the Table
	 */
	private void createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION; // TODO HIDE_SELECTION ?

		table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 5; // 5 according to number of buttons.
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				// TODO document in readme: typing 'Suppr' deletes a row.
				if (event.keyCode == SWT.DEL) {
					Table table = (Table) event.getSource();
					TableItem item = table.getSelection()[0]; // TODO what if [0] does not exist.
					SolrGUIDocument document = (SolrGUIDocument) item.getData();
					server.removeDocument(document);
				}
			}
		});
		
		// Add columns
		for (FieldInfo field:server.getRemoteFields()) { // TODO should cache the result of getRemoteFields() in SolrGUIServer and call getFields()
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText(field.getName());
			column.pack(); // TODO needed ? might be worth to setLayout() to get rid of this
		}
	}
	
	/**
	 * Create the TableViewer 
	 */
	private void createTableViewer() {
		// TODO use collectionutils to transform List<FieldInfo> into List<String>
		List<String> columnNames = new ArrayList<String>(); // TODO should cache the result of getRemoteFields() in SolrGUIServer and call getFields()
		for(FieldInfo field:server.getRemoteFields()) {
			columnNames.add(field.getName());
		}
		// TODO cols
		String[] cols = new String[columnNames.size()];
		columnNames.toArray(cols);
		
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(cols);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[tableViewer.getColumnProperties().length];
		TextCellEditor textEditor;

		for (int i =0; i < editors.length; i++) {
			textEditor = new TextCellEditor(table);
			((Text) textEditor.getControl()).setTextLimit(60);
			editors[i] = textEditor;
		}
		
		tableViewer.setCellEditors(editors);
		tableViewer.setCellModifier(new SolrGUICellModifier(server));
		tableViewer.setContentProvider(new SolrGUIContentProvider(server, tableViewer));
		tableViewer.setLabelProvider(new SolrGUILabelProvider(server.getRemoteFields())); // TODO should cache the result of getRemoteFields() in SolrGUIServer and call getFields()
		tableViewer.setInput(server);
	}

	// Return selected document (or null if none selected).
	public SolrGUIDocument getSelectedDocument() {
		return (SolrGUIDocument) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
	}
	
	public void refresh() {
		tableViewer.refresh();
	}
	
	public int getItemCount() {
		return tableViewer.getTable().getItemCount();
	}
	
	public void dispose() {
		tableViewer.getLabelProvider().dispose();
	}

}
