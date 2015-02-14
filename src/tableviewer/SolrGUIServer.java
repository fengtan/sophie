package tableviewer;

public class SolrGUIServer {

	private boolean completed 	= false;
	private String description 	= "";
	private String owner 		= "?";
	private int percentComplete = 0;  

	/**
	 * Create a task with an initial description
	 * 
	 * @param string
	 */
	public SolrGUIServer(String string) {
		
		super();
		setDescription(string);
	}

	/**
	 * @return true if completed, false otherwise
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @return String task description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return String task owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @return int percent completed
	 * 
	 */
	public int getPercentComplete() {
		return percentComplete;
	}

	/**
	 * Set the 'completed' property
	 * 
	 * @param b
	 */
	public void setCompleted(boolean b) {
		completed = b;
	}

	/**
	 * Set the 'description' property
	 * 
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * Set the 'owner' property
	 * 
	 * @param string
	 */
	public void setOwner(String string) {
		owner = string;
	}

	/**
	 * Set the 'percentComplete' property
	 * 
	 * @param i
	 */
	public void setPercentComplete(int i) {
		percentComplete = i;
	}

}