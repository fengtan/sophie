package com.github.fengtan.solrgui.tables;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.github.fengtan.solrgui.beans.SolrGUIDocument;
import com.github.fengtan.solrgui.beans.SolrGUIServer;


/**
 * InnerClass that acts as a proxy for the SolrGUIServer
 * providing content for the Table. It implements the ISolrGUIServerViewer 
 * interface since it must register changeListeners with the 
 * SolrGUIServer
 */
public class SolrGUIContentProvider implements IStructuredContentProvider, ISolrGUIChangeListener {
	
	private SolrGUIServer server;
	private TableViewer tableViewer;
	
	public SolrGUIContentProvider(SolrGUIServer server, TableViewer tableViewer) {
		this.server = server;
		this.tableViewer = tableViewer;
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			((SolrGUIServer) newInput).addChangeListener(this);
		}
		if (oldInput != null) {
			((SolrGUIServer) oldInput).removeChangeListener(this);
		}
	}

	@Override
	public void dispose() {
		server.removeChangeListener(this);
	}

	// Return the documents as an array of Objects
	@Override
	public Object[] getElements(Object parent) {
		return server.getDocuments().toArray();
	}

	@Override
	public void addDocument(SolrGUIDocument document) {
		tableViewer.add(document);
	}

	@Override
	public void removeDocument(SolrGUIDocument document) {
		tableViewer.remove(document);
	}

	@Override
	public void modifyDocument(SolrGUIDocument document) {
		tableViewer.update(document, null);
	}
	
}
