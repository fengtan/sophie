package com.github.fengtan.solrgui.ui;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Column {

	private String title;
	private boolean displayed; // Whether the column is displayed / hidden.
	private boolean fixed; // Whether the column can be hidden (false for edit/delete columns). 

	public Column(String title) {
		this(title, false);
	}
	
	public Column(String title, boolean fixed) {
		this.title = title;
		this.fixed = fixed;
		this.displayed = true;
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

	public boolean isFixed() {
		return fixed;
	}
	
	public int hashcode() {
		return new HashCodeBuilder().append(title).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Column)) {
			return false;
		}
		Column c = (Column) o;
		return new EqualsBuilder().append(this.title, c.title).isEquals();
	}
	
}
