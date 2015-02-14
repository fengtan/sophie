package tableviewer;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class SolrGUI {

	private Table table;
	private TableViewer tableViewer;
	private Button closeButton;
	
	private SolrGUIServer server = new SolrGUIServer(); 

	private String[] columnNames = new String[] {"completed", "description", "owner", "percent"};

	/**
	 * Launch the window.
	 */
	public static void main(String[] args) {
		Shell shell = new Shell();
		shell.setText("Solr GUI");

		// Set layout for shell.
		GridLayout layout = new GridLayout();
		shell.setLayout(layout);
		
		// Create a composite to hold the children.
		Composite composite = new Composite(shell, SWT.NONE);
		final SolrGUI solrGUI = new SolrGUI(composite);
		solrGUI.getControl().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				solrGUI.dispose();			
			}
		});

		// Make the shell to display its content.
		shell.open();
		solrGUI.run(shell);
	}
	
	public SolrGUI(Composite parent) {
		this.addChildControls(parent);
	}

	/**
	 * Run and wait for a close event
	 * @param shell Instance of Shell
	 */
	private void run(Shell shell) {
		// Add a listener for the close button
		closeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// Close the view i.e. dispose of the composite's parent.
				table.getParent().getParent().dispose();
			}
		});
		
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Release resources
	 */
	public void dispose() {
		// Tell the label provider to release its resources.
		tableViewer.getLabelProvider().dispose();
	}

	/**
	 * Create a new shell, add the widgets, open the shell
	 * @return the shell that was created	 
	 */
	private void addChildControls(Composite composite) {

		// Create a composite to hold the children
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
		composite.setLayoutData (gridData);

		// Set numColumns to 3 for the buttons 
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 4;
		composite.setLayout (layout);

		// Create the table 
		createTable(composite);
		
		// Create and setup the TableViewer
		createTableViewer();
		tableViewer.setContentProvider(new SolrGUIContentProvider());
		tableViewer.setLabelProvider(new SolrGUILabelProvider());
		// The input for the table viewer is the instance of ExampleTaskList
		server = new SolrGUIServer();
		tableViewer.setInput(server);

		// Add the buttons
		createButtons(composite);
	}

	/**
	 * Create the Table
	 */
	private void createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		table = new Table(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// 1st column with image/checkboxes - NOTE: The SWT.CENTER has no effect!!
		TableColumn column = new TableColumn(table, SWT.CENTER, 0);		
		column.setText("Modified");
		column.setWidth(20);
		
		// 2nd column with task Description
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Description");
		column.setWidth(400);
		// Add listener to column so tasks are sorted by description when clicked 
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setSorter(new SolrGUIServerSorter(SolrGUIServerSorter.DESCRIPTION));
			}
		});

		// 3rd column with task Owner
		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("Owner");
		column.setWidth(100);
		// Add listener to column so tasks are sorted by owner when clicked
		column.addSelectionListener(new SelectionAdapter() {
       	
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setSorter(new SolrGUIServerSorter(SolrGUIServerSorter.OWNER));
			}
		});

		// 4th column with task PercentComplete 
		column = new TableColumn(table, SWT.CENTER, 3);
		column.setText("% Complete");
		column.setWidth(80);
		//  Add listener to column so tasks are sorted by percent when clicked
		column.addSelectionListener(new SelectionAdapter() {
       	
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setSorter(new SolrGUIServerSorter(SolrGUIServerSorter.PERCENT_COMPLETE));
			}
		});
	}

	/**
	 * Create the TableViewer 
	 */
	private void createTableViewer() {

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		
		tableViewer.setColumnProperties(columnNames);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[columnNames.length];

		// Column 1 : Modified (Checkbox)
		editors[0] = new CheckboxCellEditor(table);

		// Column 2 : Description (Free text)
		TextCellEditor textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).setTextLimit(60);
		editors[1] = textEditor;

		// Column 3 : Owner (Combo Box) 
		editors[2] = new ComboBoxCellEditor(table, server.getOwners(), SWT.READ_ONLY);

		// Column 4 : Percent complete (Text with digits only)
		textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).addVerifyListener(
		
			new VerifyListener() {
				public void verifyText(VerifyEvent e) {
					// Here, we could use a RegExp such as the following 
					// if using JRE1.4 such as  e.doit = e.text.matches("[\\-0-9]*");
					e.doit = "0123456789".indexOf(e.text) >= 0 ;
				}
			});
		editors[3] = textEditor;

		// Assign the cell editors to the viewer 
		tableViewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier(new SolrGUICellModifier(this));
		// Set the default sorter for the viewer 
		tableViewer.setSorter(new SolrGUIServerSorter(SolrGUIServerSorter.DESCRIPTION));
	}

	/*
	 * Close the window and dispose of resources
	 */
	public void close() {
		Shell shell = table.getShell();
		if (shell != null && !shell.isDisposed())
			shell.dispose();
	}


	/**
	 * InnerClass that acts as a proxy for the ExampleTaskList 
	 * providing content for the Table. It implements the ITaskListViewer 
	 * interface since it must register changeListeners with the 
	 * ExampleTaskList 
	 */
	class SolrGUIContentProvider implements IStructuredContentProvider, ISolrGUIServerViewer {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null)
				((SolrGUIServer) newInput).addChangeListener(this);
			if (oldInput != null)
				((SolrGUIServer) oldInput).removeChangeListener(this);
		}

		public void dispose() {
			server.removeChangeListener(this);
		}

		// Return the tasks as an array of Objects
		public Object[] getElements(Object parent) {
			return server.getDocuments().toArray();
		}

		public void addDocument(SolrGUIDocument task) {
			tableViewer.add(task);
		}

		public void removeDocument(SolrGUIDocument task) {
			tableViewer.remove(task);			
		}

		public void updateDocument(SolrGUIDocument task) {
			tableViewer.update(task, null);	
		}
	}
	
	/**
	 * Return the array of choices for a multiple choice cell
	 */
	public String[] getChoices(String property) {
		if ("owner".equals(property))
			return server.getOwners();  // The ExampleTaskList knows about the choice of owners
		else
			return new String[]{};
	}

	/**
	 * Add the "Add", "Delete" and "Close" buttons
	 * @param parent the parent composite
	 */
	private void createButtons(Composite parent) {
		
		// Create and configure the "Add" button
		Button add = new Button(parent, SWT.PUSH | SWT.CENTER);
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
		Button delete = new Button(parent, SWT.PUSH | SWT.CENTER);
		delete.setText("Delete");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80; 
		delete.setLayoutData(gridData); 
		delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SolrGUIDocument task = (SolrGUIDocument) ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
				if (task != null) {
					server.removeDocument(task);
				} 				
			}
		});

		//	Create and configure the "Commit" button.
		Button commit = new Button(parent, SWT.PUSH | SWT.CENTER);
		commit.setText("Commit");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80; 
		commit.setLayoutData(gridData); 
		commit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO 				
			}
		});	
		
		//	Create and configure the "Close" button.
		closeButton = new Button(parent, SWT.PUSH | SWT.CENTER);
		closeButton.setText("Close");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_END);
		gridData.widthHint = 80; 
		closeButton.setLayoutData(gridData); 		
	}

	/**
	 * Return the column names in a collection
	 * 
	 * @return List  containing column names
	 */
	public List<String> getColumnNames() {
		return Arrays.asList(columnNames);
	}

	/**
	 * @return currently selected item
	 */
	public ISelection getSelection() {
		return tableViewer.getSelection();
	}

	/**
	 * Return the ExampleTaskList
	 */
	public SolrGUIServer getTaskList() {
		return server;	
	}

	/**
	 * Return the parent composite
	 */
	public Control getControl() {
		return table.getParent();
	}

	/**
	 * Return the 'close' Button
	 */
	public Button getCloseButton() {
		return closeButton;
	}
}