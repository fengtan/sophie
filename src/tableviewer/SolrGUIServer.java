/**
 * (c) Copyright Mirasol Op'nWorks Inc. 2002, 2003. 
 * http://www.opnworks.com
 * Created on Apr 2, 2003 by lgauthier@opnworks.com
 * 
 */

package tableviewer;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 * Class that plays the role of the domain model in the TableViewerExample
 * In real life, this class would access a persistent store of some kind.
 * 
 */
public class SolrGUIServer {

	private Vector<SolrGUIDocument> documents = new Vector<SolrGUIDocument>();
	private Set<ISolrGUIServerViewer> changeListeners = new HashSet<ISolrGUIServerViewer>();
	private SolrServer server;

	static final String[] OWNERS_ARRAY = { "?", "Nancy", "Larry", "Joe" };

	/**
	 * Initialize the table data.
	 * Create COUNT documents and add them them to the collection of documents.
	 */
	public SolrGUIServer(URL url) {
		server = new HttpSolrServer(url.toExternalForm());
		
		
			SolrGUIDocument document = new SolrGUIDocument("Document");
			document.setOwner(OWNERS_ARRAY[0]);
			documents.add(document);
		
	}
	
	private SolrDocumentList getAllDocuments() {
		SolrQuery query = new SolrQuery();
		query.set("q", "*:*");
		return getDocumentList(query);
	}
	
	private SolrDocumentList getDocumentList(SolrQuery query) {
		QueryResponse response;
		try {
			response = server.query(query);
		} catch (SolrServerException e) {
			// TODO log error
			return new SolrDocumentList();
		}
		return response.getResults();
	}

	/**
	 * Return the array of owners   
	 */
	public String[] getOwners() {
		return OWNERS_ARRAY;
	}
	
	/**
	 * Return the collection of documents
	 */
	public Vector<SolrGUIDocument> getDocuments() {
		return documents;
	}
	
	/**
	 * Add a new document to the collection of documents
	 */
	public void addDocument() {
		SolrGUIDocument document = new SolrGUIDocument("New document");
		documents.add(documents.size(), document);
		Iterator<ISolrGUIServerViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((ISolrGUIServerViewer) iterator.next()).addDocument(document);
	}

	/**
	 * @param document
	 */
	public void removeDocument(SolrGUIDocument document) {
		documents.remove(document);
		Iterator<ISolrGUIServerViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((ISolrGUIServerViewer) iterator.next()).removeDocument(document);
	}

	/**
	 * @param document
	 */
	public void documentChanged(SolrGUIDocument document) {
		Iterator<ISolrGUIServerViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((ISolrGUIServerViewer) iterator.next()).updateDocument(document);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(ISolrGUIServerViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(ISolrGUIServerViewer viewer) {
		changeListeners.add(viewer);
	}

}