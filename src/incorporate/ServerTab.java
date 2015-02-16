package incorporate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.SolrGUIServer;

public class ServerTab {

    private Table table;
    private TabItem tabItem;
    
    public ServerTab(SolrGUIServer server, TabFolder tabFolder) {
    	
	    
	    tabItem = new TabItem(tabFolder, SWT.NULL);
    }
    
	public void updateTable(Composite parent) {
	    table = new Table(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION); // TODO
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);


	    for (int i = 0; i < table.getColumnCount(); i++) {
	    	table.getColumn(i).pack();
	    }
	    
	    tabItem.setControl(table);
	    table.setSize(table.computeSize(SWT.DEFAULT, 200)); // TODO
	    	    
	    final TableEditor editor = new TableEditor(table);
	    editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;

	    table.addMouseListener(new MouseAdapter() {
	    	public void mouseDown(MouseEvent event) {   		
	    		Control old = editor.getEditor();
	    		if (old != null) old.dispose();

	    		Point pt = new Point(event.x, event.y);

	    		final TableItem item = table.getItem(pt);
	    		if (item == null) return;

	    		int column = -1;
	    		for (int i = 0, n = table.getColumnCount(); i < n; i++) {
	    			Rectangle rect = item.getBounds(i);
	    			if (rect.contains(pt)) {
	    				column = i;
	    				break;
	    			}
	    		}
	    
	    		final Text text = new Text(table, SWT.NONE);
	    		text.setForeground(item.getForeground());

	    		text.setText(item.getText(column));
	    		text.setForeground(item.getForeground());
	    		text.selectAll();
	    		text.setFocus();

	    		editor.minimumWidth = text.getBounds().width;

	    		editor.setEditor(text, item, column);

	    		final int col = column;
	    		text.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
	    				item.setText(col, text.getText());
	    				// TODO update document on Solr
					}
				});
	    	}
	    });
	    
	}
	
	public void updateMenu(Menu menu, final Shell shell) { // TODO drop shell argument ?
        // File menu.
        MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
        fileMenuItem.setText("&File");
        
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuItem.setMenu(fileMenu);

        MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
        newItem.setText("Add &server");
        newItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                new NewServerDialog(shell).open();
              }
		});
        
        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("&Exit");

        exitItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
        
        // Columns menu.
        MenuItem columnsMenuItem = new MenuItem(menu, SWT.CASCADE);
        columnsMenuItem.setText("&Columns");

        Menu columnsMenu = new Menu(shell, SWT.DROP_DOWN);
        columnsMenuItem.setMenu(columnsMenu);

        
	}
}
