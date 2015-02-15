/**
 * (c) Copyright Mirasol Op'nWorks Inc. 2002, 2003. 
 * http://www.opnworks.com
 * Created on Apr 2, 2003 by lgauthier@opnworks.com
 * 
 */

package tableviewer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Class that plays the role of the domain model in the TableViewerExample
 * In real life, this class would access a persistent store of some kind.
 * 
 */
public class SolrGUIServer {

	private final int COUNT = 10;
	private Vector<SolrGUIDocument> documents = new Vector<SolrGUIDocument>(COUNT);
	private Set<ISolrGUIServerViewer> changeListeners = new HashSet<ISolrGUIServerViewer>();

	// Combo box choices
	static final String[] OWNERS_ARRAY = { "?", "Nancy", "Larry", "Joe" };

	/**
	 * Initialize the table data.
	 * Create COUNT documents and add them them to the collection of documents.
	 */
	public SolrGUIServer() {
		for (int i = 0; i < COUNT; i++) {
			SolrGUIDocument document = new SolrGUIDocument("Document "  + i);
			document.setOwner(OWNERS_ARRAY[i % 3]);
			documents.add(document);
		}
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