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
		
		for (SolrDocument doc: list) {
			for(String name:doc.getFieldNames()) {
				System.out.println(name);
			}
		}
		
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
	    int count = 128;
	    for (int i = 0; i < count; i++) {
	      TableItem item = new TableItem(table, SWT.NONE);
	      item.setText(0, "x");
	      item.setText(1, "y");
	      item.setText(2, "!");
	      item.setText(3, "this stuff behaves the way I expect");
	      item.setText(4, "almost everywhere");
	      item.setText(5, "some.folder");
	      item.setText(6, "line " + i + " in nowhere");
	      item.setText(7, "line " + i + " in nowhere");
	      item.setText(8, "line " + i + " in nowhere");
	      item.setText(9, "line " + i + " in nowhere");
	      item.setText(10, "line " + i + " in nowhere");
	      item.setText(11, "line " + i + " in nowhere");
	      item.setText(12, "line " + i + " in nowhere");
	      item.setText(13, "line " + i + " in nowhere");
	      item.setText(14, "line " + i + " in nowhere");
	      item.setText(15, "line " + i + " in nowhere");
	      item.setText(16, "line " + i + " in nowhere");
	      item.setText(17, "line " + i + " in nowhere");
	      item.setText(18, "line " + i + " in nowhere");
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
