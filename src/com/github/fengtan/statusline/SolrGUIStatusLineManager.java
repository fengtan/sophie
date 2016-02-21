package com.github.fengtan.statusline;

import org.apache.solr.client.solrj.SolrResponse;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.widgets.Composite;

public class SolrGUIStatusLineManager extends StatusLineManager implements ISolrGUIQueryListener {

	public SolrGUIStatusLineManager(Composite parent) {
		createControl(parent);
	}

	@Override
	public void responseReceived(SolrResponse response) {
		setMessage(response.toString());
		// TODO Auto-generated method stub
		// TODO display error if any
		// TODO deploy numCount + timing + possibly querystring
		/*
		 * 
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  TODO SolrGUIServer.addQueryListener()
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 *  
		 */
	}
	
}
