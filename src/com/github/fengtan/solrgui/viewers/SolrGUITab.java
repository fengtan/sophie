package com.github.fengtan.solrgui.viewers;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;
import com.github.fengtan.solrgui.beans.SolrGUIServer;

// TODO mechanism to load / delete servers from config file.
// TODO sort by ID field by default ? so rows remain the same when modify one
public class SolrGUITab extends CTabItem {

	private Table table;
	private TableViewer tableViewer;
	private SolrGUIServer server;
	private SolrGUISorter sorter;
	
	public SolrGUITab(CTabFolder tabFolder, SolrGUIServer server) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());		
		this.server = server;
		setText(server.getName());
		setToolTipText(server.getURL().toString());
		// Fill up tab.
		Composite composite = new Composite(getParent(), SWT.BORDER);
		createLayout(composite);
		createTable(composite);
		createTableViewer();
		createButtons(composite);
		setControl(composite);
		// Set focus on this tab.
		tabFolder.setSelection(this);
		tabFolder.forceFocus();
	}

	private void createLayout(Composite composite) {
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
		composite.setLayoutData (gridData);
		// Set numColumns to 3 for the buttons 
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 4;
		composite.setLayout(layout);
	}
	
	/**
	 * Create the Table
	 */
	private void createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 4;
		table.setLayoutData(gridData);
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		// TODO Review tooltip + Changes vs Status (Ctrl+F) should be consistent 
		column.setText("Changes");
		column.setToolTipText("Pending changes:\nClick on \"Commit\" to push to Solr\nClick on \"Revert\" to cancel changes");
		column.setWidth(20);
		
		for (final String field:server.getFields()) {
			column = new TableColumn(table, SWT.LEFT);
			column.setText(field);
			// Add listener to column so documents are sorted when clicked.
			column.addSelectionListener(new SelectionAdapter() { 
				public void widgetSelected(SelectionEvent e) {
					sorter = new SolrGUISorter(field);
					tableViewer.setSorter(sorter); // TODO does sorting scale ?
				}
			});
			column.pack();
		}

	}

	/**
	 * Create the TableViewer 
	 */
	private void createTableViewer() {
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);

		String[] columns = ArrayUtils.addAll(new String[]{"Status"}, server.getFields());
		tableViewer.setColumnProperties(columns);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[tableViewer.getColumnProperties().length];
		TextCellEditor textEditor;

		for (int i=0; i<editors.length; i++) {
			textEditor = new TextCellEditor(table);
			((Text) textEditor.getControl()).setTextLimit(60);
			editors[i] = textEditor;
		}

		tableViewer.setCellEditors(editors);
		tableViewer.setCellModifier(new SolrGUICellModifier(server));
		tableViewer.setSorter(sorter); // Set default sorter (null-safe).
		tableViewer.setContentProvider(new SolrGUIContentProvider(server, tableViewer));
		tableViewer.setLabelProvider(new SolrGUILabelProvider(server));
		tableViewer.setInput(server);
	}

	@Override
	public void dispose() {
		tableViewer.getLabelProvider().dispose();
		server.dispose();
		super.dispose();
	}

	/**
	 * Add the "Add", "Delete" and "Commit" buttons
	 * 
	 * @param composite the parent composite
	 */
	private void createButtons(Composite composite) {

		// Create and configure the "Add" button
		Button add = new Button(composite, SWT.PUSH | SWT.CENTER);
		add.setText("Add");
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		add.setLayoutData(gridData);
		add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				server.addDocument();
			}
		});

		//	Create and configure the "Delete" button
		Button delete = new Button(composite, SWT.PUSH | SWT.CENTER);
		delete.setText("Delete");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80; 
		delete.setLayoutData(gridData); 
		delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUIDocument document = (SolrGUIDocument) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
				if (document != null) {
					server.removeDocument(document);
				} 				
			}
		});

		//	Create and configure the "Clone" button
		Button clone = new Button(composite, SWT.PUSH | SWT.CENTER);
		clone.setText("Clone");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80; 
		clone.setLayoutData(gridData);
		clone.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUIDocument document = (SolrGUIDocument) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
				if (document != null) {
					// TODO Cloning generate remote exception
					server.addDocument(document.clone());
				} 				
			}
		});

		//	Create and configure the "Commit" button.
		Button commit = new Button(composite, SWT.PUSH | SWT.CENTER);
		commit.setText("Commit");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_END);
		gridData.widthHint = 80; 
		commit.setLayoutData(gridData);
		commit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				server.commit();
				tableViewer.refresh();
			}
		});
	}

}
