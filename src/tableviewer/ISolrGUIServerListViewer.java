package tableviewer;
/*
 * (c) Copyright Mirasol Op'nWorks Inc. 2002, 2003. 
 * http://www.opnworks.com
 * Created on Jun 11, 2003 by lgauthier@opnworks.com
 *
 */

public interface ISolrGUIServerListViewer {
	
	/**
	 * Update the view to reflect the fact that a task was added 
	 * to the task list
	 * 
	 * @param task
	 */
	public void addTask(SolrGUIServer task);
	
	/**
	 * Update the view to reflect the fact that a task was removed 
	 * from the task list
	 * 
	 * @param task
	 */
	public void removeTask(SolrGUIServer task);
	
	/**
	 * Update the view to reflect the fact that one of the tasks
	 * was modified 
	 * 
	 * @param task
	 */
	public void updateTask(SolrGUIServer task);
}