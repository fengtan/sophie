package com.github.fengtan.solrgui;


public class Example {

	public static void main(String[] args) {
		for (SolrGUIServer server:Config.getServers()) {	
			new SolrGUIDisplay(server.getName(), server.getAllDocuments());	
		}
	}
	
}
