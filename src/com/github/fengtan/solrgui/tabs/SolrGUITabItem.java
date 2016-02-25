package com.github.fengtan.solrgui.tabs;

import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;
import com.github.fengtan.solrgui.beans.SolrGUIQuery;
import com.github.fengtan.solrgui.beans.SolrGUIServer;
import com.github.fengtan.solrgui.filters.SolrGUIFilter;
import com.github.fengtan.solrgui.tables.SolrGUICellModifier;
import com.github.fengtan.solrgui.tables.SolrGUIContentProvider;
import com.github.fengtan.solrgui.tables.SolrGUILabelProvider;
import com.github.fengtan.solrgui.tables.SolrGUISorter;

// TODO icon in ubuntu launcher
// TODO license
// TODO mechanism to load / delete servers from config file.
// TODO sort by ID field by default ? so rows remain the same when modify one
// TODO if server empty and click on table viewer -> seems to crash
public class SolrGUITabItem extends CTabItem {

	private Composite filtersComposite;
	
	private Set<SolrGUIFilter> filters = new HashSet<SolrGUIFilter>();
	// TODO could be worth using style VIRTUAL since the data source is remote http://help.eclipse.org/luna/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fswt%2Fwidgets%2FTable.html
	private Table table;
	private TableViewer tableViewer;
	private Label statusLine; // Label in footer - displays number of documents received etc.
	
	private SolrGUIServer server;
	private SolrGUISorter sorter;
	
	public SolrGUITabItem(CTabFolder tabFolder, SolrGUIServer server) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		this.server = server;
		setText(server.getName());
		setToolTipText(server.getURL().toString());
		
		// Fill in tab.
		Composite tabComposite = new Composite(getParent(), SWT.NULL);
		createLayout(tabComposite);
		filtersComposite = new Composite(tabComposite, SWT.NULL);
		filtersComposite.setLayout(new GridLayout());
		addFilter();
		createTable(tabComposite);
		createTableViewer();
		createStatusLine(tabComposite);
		setControl(tabComposite);
		
		// Set focus on this tab.
		tabFolder.setSelection(this);
		tabFolder.forceFocus();
		
		// Initialize status line.
		refreshStatusLine();
	}

	public void addFilter() {
		filters.add(new SolrGUIFilter(filtersComposite, server, this)); // TODO passing this is ugly
		filtersComposite.getParent().pack(); // TODO needed ?*/
	}
	
	public void removeFilter(SolrGUIFilter filter) {
		filters.remove(filter);
		filter.dispose();
		filtersComposite.getParent().pack(); // TODO needed ?*/
	}
	
	public Set<SolrGUIFilter> getFilters() {
		return filters;
	}

	private void createLayout(Composite composite) {
		GridData grid = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
		composite.setLayoutData(grid); 
		GridLayout layout = new GridLayout(5, false); // 5 according to number of buttons.
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

		TableColumn column;
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
		tableViewer.setColumnProperties(server.getFields());

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
		tableViewer.setSorter(sorter); // Set default sorter (null-safe).
		tableViewer.setContentProvider(new SolrGUIContentProvider(server, tableViewer));
		tableViewer.setLabelProvider(new SolrGUILabelProvider(server));
		tableViewer.setInput(server);
	}
	
	/**
	 * Create status line.
	 */
	private void createStatusLine(Composite composite) {
		statusLine = new Label(composite, SWT.WRAP);
		statusLine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	}
	
	// TODO sorting could possibly be done using solr, not swt
	// TODO not updated when clicking 'refresh'
	// TODO not updated when launching app
	protected void refreshStatusLine() {
		int numDocuments = tableViewer.getTable().getItemCount();
		statusLine.setText(numDocuments+" documents");// TODO "XX additions, XX deletions, XX modifications"
	}
	
	@Override
	public void dispose() {
		tableViewer.getLabelProvider().dispose();
		server.dispose();
		super.dispose();
	}

	// TODO what is the point of encapsulating server
	public void addNewDocument() {
		server.addDocument();
		refreshStatusLine();
	}
	
	public void deleteCurrentDocument() {
		SolrGUIDocument document = (SolrGUIDocument) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
		if (document != null) {
			server.removeDocument(document);
		}
		refreshStatusLine();
	}
	
	public void cloneCurrentDocument() {
		SolrGUIDocument document = (SolrGUIDocument) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
		if (document != null) {
			// TODO Cloning generate remote exception
			server.addDocument(document.clone());
		}
		refreshStatusLine();
	}
	
	public void refresh() {
		SolrQuery query = new SolrGUIQuery(filters);
		server.refreshDocuments(query);
		tableViewer.refresh();
		refreshStatusLine();
	}
	
	public void commit() {
		server.commit();
		tableViewer.refresh();
		refreshStatusLine();
	}
	
	public void clear() {
		server.clear();
		tableViewer.refresh();
		refreshStatusLine();
	}
	
}
