package tableviewer;

public class SolrGUIDocument {

	private boolean modified	= false;
	private String description 	= "";
	private String owner 		= "?";
	private int percentComplete = 0;  

	public SolrGUIDocument(String string) {
		super();
		setDescription(string);
	}
	
	/**
	 * @return Boolean whether document is modified
	 */
	public Boolean isModified() {
		return modified;
	}

	/**
	 * @return String document description
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
	 * Set the 'modified' property
	 * 
	 * @param b
	 */
	public void setModified(boolean b) {
		modified = b;
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