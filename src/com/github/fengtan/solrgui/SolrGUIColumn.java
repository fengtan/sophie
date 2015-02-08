package com.github.fengtan.solrgui;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SolrGUIColumn {

	private boolean displayed = true;
	private String title;
	
	public SolrGUIColumn(String title) {
		this.setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
	
	/**
	 * @Override
	 */
	public boolean equals(Object object) {
		if (!(object instanceof SolrGUIColumn)) {
			return false;
		}
		if (object == this) {
			return true;
		}
		SolrGUIColumn column = (SolrGUIColumn) object;
		return new EqualsBuilder().append(title, column.title).isEquals();
	}
	
	/**
	 * @Override
	 */
	public int hashCode() {
		return new HashCodeBuilder(91, 27).append(title).toHashCode();
	}
	
}
