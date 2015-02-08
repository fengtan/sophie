package com.github.fengtan.solrgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class Dashboard {

	public void displayDocuments(SolrDocumentList list) {
	    Display display = new Display();
	    Shell shell = new Shell(display);
	    Table table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION); // TODO
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);
	    	    
	    List<String> titles = new ArrayList<String>(); // TODO hashed ? use of indexOf  
	    for (SolrDocument document:list) {
	    	TableItem item = new TableItem(table, SWT.NONE);
	    	for (Map.Entry<String, Object> field:document.entrySet()) { // TODO is there a utility function to do that quikly
	    		if (!titles.contains(field.getKey())) {
	  		      	new TableColumn(table, SWT.NONE).setText(field.getKey());
	    			titles.add(field.getKey());
	    		}
	    		item.setText(titles.indexOf(field.getKey()), field.getValue().toString());
	    	}
	    }
	    
	    for (int i = 0; i < titles.size(); i++) {
	      table.getColumn(i).pack();
	    }
	    table.setSize(table.computeSize(SWT.DEFAULT, 200));
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
	
}
