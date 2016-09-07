package com.github.fengtan.solrgui.tables;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.solrgui.SolrGUI;


public class CoresTable {
	
	private Table table;
	private Map<String, Integer> colNames = new HashMap<String, Integer>();
	
	public CoresTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL;

		table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// Add columns + data.
		CoreAdminRequest request = new CoreAdminRequest();
		request.setAction(CoreAdminAction.STATUS);
		try {
			CoreAdminResponse response = request.process(SolrGUI.client); // TODO throws RemoteSolrException if query on /solr/collection1 instead of /solr
			for (Map.Entry<String, NamedList<Object>> core:response.getCoreStatus()) {
				populateLine(core.getValue(), new TableItem(table, SWT.NULL));
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Pack.
		for(TableColumn column:table.getColumns()) {
			column.pack();// TODO needed ? might be worth to setLayout() to get rid of this
		}
	}
		
	private void populateLine(NamedList namedList, TableItem item) {
		for (int idx = 0; idx < namedList.size(); idx++) {
			Object object = namedList.getVal(idx);
			if (object instanceof NamedList) {
				// NamedList: go through all elements recursively.
				populateLine((NamedList) object, item);
			} else {
				// Not a NamedList: add it to the table.
				String name = namedList.getName(idx);
				// If column does not exist yet, create it.
				if (!colNames.containsKey(name)) {
					TableColumn col = new TableColumn(table, SWT.LEFT);
					col.setText(name);
					colNames.put(name, colNames.size());
				}
				item.setText(colNames.get(name), object.toString());	
			}	
		}		
	}
	
}
