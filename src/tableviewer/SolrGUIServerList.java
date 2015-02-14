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
public class SolrGUIServerList {

	private final int COUNT = 10;
	private Vector<SolrGUIServer> tasks = new Vector<SolrGUIServer>(COUNT);
	private Set<ISolrGUIServerListViewer> changeListeners = new HashSet<ISolrGUIServerListViewer>();

	// Combo box choices
	static final String[] OWNERS_ARRAY = { "?", "Nancy", "Larry", "Joe" };
	
	/**
	 * Constructor
	 */
	public SolrGUIServerList() {
		super();
		this.initData();
	}
	
	/*
	 * Initialize the table data.
	 * Create COUNT tasks and add them them to the 
	 * collection of tasks
	 */
	private void initData() {
		SolrGUIServer task;
		for (int i = 0; i < COUNT; i++) {
			task = new SolrGUIServer("Task "  + i);
			task.setOwner(OWNERS_ARRAY[i % 3]);
			tasks.add(task);
		}
	};

	/**
	 * Return the array of owners   
	 */
	public String[] getOwners() {
		return OWNERS_ARRAY;
	}
	
	/**
	 * Return the collection of tasks
	 */
	public Vector<SolrGUIServer> getTasks() {
		return tasks;
	}
	
	/**
	 * Add a new task to the collection of tasks
	 */
	public void addTask() {
		SolrGUIServer task = new SolrGUIServer("New task");
		tasks.add(tasks.size(), task);
		Iterator<ISolrGUIServerListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((ISolrGUIServerListViewer) iterator.next()).addTask(task);
	}

	/**
	 * @param task
	 */
	public void removeTask(SolrGUIServer task) {
		tasks.remove(task);
		Iterator<ISolrGUIServerListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((ISolrGUIServerListViewer) iterator.next()).removeTask(task);
	}

	/**
	 * @param task
	 */
	public void taskChanged(SolrGUIServer task) {
		Iterator<ISolrGUIServerListViewer> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((ISolrGUIServerListViewer) iterator.next()).updateTask(task);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(ISolrGUIServerListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(ISolrGUIServerListViewer viewer) {
		changeListeners.add(viewer);
	}

}