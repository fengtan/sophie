package com.github.fengtan.solrgui.tabs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.apache.solr.common.util.NamedList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.fengtan.solrgui.SolrGUI;

public class CoresTabItem extends CTabItem {
	
	// TODO what if admin/cores is not available ? what if available on a different path ?
	public CoresTabItem(CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		setText("Cores");
		
		// Prepare layout.
		Composite composite = new Composite(getParent(), SWT.NULL);
		composite.setLayout(new GridLayout());
		setControl(composite);
		
		// Add table.
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL;

		final Table table = new Table(composite, style); // TODO turn into a FieldsTable class ?

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
			NamedList<NamedList<Object>> cores = response.getCoreStatus();
			Map<String, Integer> cols = new HashMap<String, Integer>();
			for (int idx = 0; idx < cores.size(); idx++) {
				TableItem item = new TableItem(table, SWT.NULL);
				//item.setText(0, cores.getName(idx));
				NamedList<Object> values = cores.getVal(idx);
				for (int idy = 0; idy < values.size(); idy++) {
					String name = values.getName(idy);
					// Add column if it does not exist yet
					if (!cols.containsKey(name)) {
						TableColumn col = new TableColumn(table, SWT.LEFT);
						col.setText(name);
						cols.put(name, idy);
					}
					item.setText(cols.get(name), values.getVal(idy).toString());	
				}
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
		composite.pack();
		
	}
	
}
