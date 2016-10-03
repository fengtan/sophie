package com.github.fengtan.sophie.beans;


public enum SolrConnectionType {

	DIRECT_HTTP("Direct HTTP", "Solr URL:", "http://localhost:8983/solr/gettingstarted"),
	SOLR_CLOUD("SolrCloud", "ZooKeeper host:", "localhost:9983");
	
	private SolrConnectionType(String typeName, String valueLabel, String valueDefault) {
		this.typeName = typeName;
		this.valueLabel = valueLabel;
		this.valueDefault = valueDefault;
	}
	
	private String typeName;
	private String valueLabel;
	private String valueDefault;
	
	public String getTypeName() {
		return typeName;
	}
	
	public String getValueLabel() {
		return valueLabel;
	}
	
	public String getValueDefault() {
		return valueDefault;
	}
	
}
