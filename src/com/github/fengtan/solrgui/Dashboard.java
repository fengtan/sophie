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
	
	private SolrDocumentList list;
	private Table table;
	
	public Dashboard(SolrDocumentList list) {
		this.list = list;
	    Display display = new Display();
	    Shell shell = new Shell(display);
	    this.table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION); // TODO
	    this.table.setLinesVisible(true);
	    this.table.setHeaderVisible(true);
	    this.updateTable();
	    this.table.setSize(this.table.computeSize(SWT.DEFAULT, 200));
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
	
	private void updateTable() {
	    List<String> titles = new ArrayList<String>(); // TODO hashed ? use of indexOf  
	    for (SolrDocument document:this.list) {
	    	TableItem item = new TableItem(this.table, SWT.NONE);
	    	for (Map.Entry<String, Object> field:document.entrySet()) {
	    		if (!titles.contains(field.getKey())) {
	  		      	new TableColumn(this.table, SWT.NONE).setText(field.getKey());
	    			titles.add(field.getKey());
	    		}
	    		item.setText(titles.indexOf(field.getKey()), field.getValue().toString());
	    	}
	    }
	    for (int i = 0; i < titles.size(); i++) {
	      this.table.getColumn(i).pack();
	    }
	}
	
}
