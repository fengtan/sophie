package obsolete;


// Column that cannot be hidden (e.g. edit/delete columns).
public class ActionColumn extends Column {

	public ActionColumn(String title) {
		super(title);
	}
	
	public boolean isDisplayed() {
		return true;
	}
	
	public void setDisplayed() {
		// Do nothing.
	}
	
	public boolean isEditable() {
		return false;
	}
	
}
