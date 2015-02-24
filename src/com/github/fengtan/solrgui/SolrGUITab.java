package com.github.fengtan.solrgui;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
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

public class SolrGUITab extends CTabItem {

	private Table table;
	private TableViewer tableViewer;
	private SolrGUIServer server;
	
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
					tableViewer.setSorter(new SolrGUIServerSorter(field)); // TODO does sorting scale ?
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
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier(new SolrGUICellModifier(server));
		// Set the default sorter for the viewer 
		tableViewer.setSorter(new SolrGUIServerSorter(server.getFields()[0])); // TODO what if there is no field
		
		tableViewer.setContentProvider(new SolrGUIContentProvider());
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
	 * InnerClass that acts as a proxy for the SolrGUIServer
	 * providing content for the Table. It implements the ISolrGUIServerViewer 
	 * interface since it must register changeListeners with the 
	 * SolrGUIServer
	 */
	class SolrGUIContentProvider implements IStructuredContentProvider, ISolrGUIServerViewer {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null) {
				((SolrGUIServer) newInput).addChangeListener(this);
			}
			if (oldInput != null) {
				((SolrGUIServer) oldInput).removeChangeListener(this);
			}
		}

		public void dispose() {
			server.removeChangeListener(this);
		}

		// Return the documents as an array of Objects
		public Object[] getElements(Object parent) {
			return server.getDocuments().toArray();
		}

		public void addDocument(SolrGUIDocument document) {
			tableViewer.add(document);
		}

		public void removeDocument(SolrGUIDocument document) {
			tableViewer.remove(document);
		}

		public void updateDocument(SolrGUIDocument document) {
			tableViewer.update(document, null);	
		}
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
					server.addDocument(document); // TODO clone item multiple times + modify it = pb -- implements Cloneable
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
