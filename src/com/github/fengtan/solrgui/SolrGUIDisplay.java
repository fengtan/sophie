package com.github.fengtan.solrgui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class SolrGUIDisplay {
	
	private SolrDocumentList docs;
	private List<String> titles = new ArrayList<String>(); // TODO hashed ? use of indexOf
	private Table table;
	
	private Listener sortListener = new Listener() {
        public void handleEvent(Event e) {
        	/*
            TableColumn column = (TableColumn) e.widget;
            /*
            if (column == intColumn) Arrays.sort(rows, BY_VAL);
            if (column == strColumn) Arrays.sort(rows, BY_STR);
            if (column == dateColumn) Arrays.sort(rows, BY_DATE);
            */
            //table.setSortColumn(column);
            updateDocuments();

        }
    };
	
	public SolrGUIDisplay(SolrDocumentList docs) {
	    this.docs = docs;
	    
	    for (SolrDocument document:docs) {
	    	for (String title:document.keySet()) {
	    		if (!titles.contains(title)) {
	    			titles.add(title);
	    		}
	    	}
	    }
		
	    Display display = new Display();
	    Shell shell = new Shell(display);
	    table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION); // TODO
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);
	    	    
	    updateTitles();
	    updateDocuments();
                
	    table.setSize(table.computeSize(SWT.DEFAULT, 200)); // TODO
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    display.dispose();
	}
	
	private void updateTitles() {
		for (String title:titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
	      	column.setText(title);
	      	column.addListener(SWT.Selection, sortListener);
		}
	}
	
	private void updateDocuments() {
		table.removeAll();

		for(SolrDocument doc:docs) {
	    	TableItem item = new TableItem(table, SWT.NONE);
	    	for (Map.Entry<String, Object> field:doc.entrySet()) {
	    		item.setText(titles.indexOf(field.getKey()), field.getValue().toString());	
	    	}
		}

	    for (int i = 0; i < titles.size(); i++) {
	      table.getColumn(i).pack();
	    }
	}
	
}
