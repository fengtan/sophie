package com.github.fengtan.solrgui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

public class CoresTabItem extends CTabItem {

	// TODO what if admin/cores is not available ? what if available on a different path ?
	public CoresTabItem(CTabFolder tabFolder) {
		super(tabFolder, SWT.NONE, tabFolder.getItemCount());
		setText("Cores");
		
		/*
		CoreAdminRequest request = new CoreAdminRequest();
		request.setAction(CoreAdminAction.STATUS); // TODO dummy action
		try {
			NamedList<Object> response = SolrGUI.client.request(request);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}
	
}
