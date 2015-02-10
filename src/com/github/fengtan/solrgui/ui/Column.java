package com.github.fengtan.solrgui.ui;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Column {

	private String title;
	private boolean fixed; // Whether the column cannot be hidden (e.g. edit/delete columns).
	private boolean displayed; // Whether the column is displayed / hidden.

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
	
	public boolean isFixed() {
		return fixed;
	}

	public boolean isDisplayed() {
		return fixed ? true : displayed;
	}

	public void setDisplayed(boolean displayed) {
		if (!fixed) {
			this.displayed = displayed;
		}
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
