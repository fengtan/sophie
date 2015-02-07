package com.github.fengtan.solrgui;

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
	    String[] titles = { 
	    "id",
	    "index_id",
	    "item_id",
	    "hash",
	    "site",
	    "is_author",
	    "tm_body$value",
	    "spell",
	    "ds_changed",
	    "is_comment_count",
	    "ds_created",
	    "bs_promote",
	    "ss_search_api_language",
	    "bs_sticky",
	    "tm_title",
	    "ss_type",
	    "_version_",
	    "timestamp"};
	    	    
	    for (String title: titles) {
	      TableColumn column = new TableColumn(table, SWT.NONE);
	      column.setText(title);
	    }

	    for (SolrDocument document:list) {
	      TableItem item = new TableItem(table, SWT.NONE);
	      int count = 0;
	      for (Object value: document.values()) {
	    	  item.setText(count++, value.toString());
	      }
	    }
	    for (int i = 0; i < titles.length; i++) {
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
