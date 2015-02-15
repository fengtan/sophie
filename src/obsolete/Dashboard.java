package obsolete;

import incorporate.ServerTab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;


public class Dashboard {
	
	private Shell shell;
	private Display display;
	private TabFolder tabFolder;
    private Menu menu;
	
	public Dashboard() {
	    display = new Display();
	    shell = new Shell(display);
	    	    
	    shell.setText("Solr GUI");
	    shell.setMaximized(true);

	    tabFolder = new TabFolder(shell, SWT.BORDER);
	    tabFolder.setSize(1300, 700); // TODO set max size of window
	    
	    menu = new Menu(shell, SWT.BAR);
	    shell.setMenuBar(menu);
	}
	
	public void display() {    
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
	
	public void addServer(Server server) {
		ServerTab tab = new ServerTab(server, tabFolder);
		tab.updateTable(tabFolder);
		tab.updateMenu(menu, shell);
	}
	
}
