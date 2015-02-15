package obsolete;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Column {

	private String title;
	private boolean displayed; // Whether the column is displayed / hidden.
	private boolean editable; // Whether the column can be edited.
	
	public Column(String title) {
		this.title = title;
		this.displayed = true;
		this.editable = true;
	}
		
	public String getTitle() {
		return title;
	}
	
	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public int hashcode() {
		return new HashCodeBuilder(53, 11).append(title).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Column)) {
			return false;
		}
		Column c = (Column) o;
		return new EqualsBuilder().append(this.title, c.title).isEquals();
	}
	
}
