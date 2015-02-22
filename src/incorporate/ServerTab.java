package incorporate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

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

	    tabItem.setControl(table);
	    table.setSize(table.computeSize(SWT.DEFAULT, 200)); // TODO
	    
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
