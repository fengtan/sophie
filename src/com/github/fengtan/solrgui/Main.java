package com.github.fengtan.solrgui;

import com.github.fengtan.solrgui.solr.Config;
import com.github.fengtan.solrgui.solr.Server;
import com.github.fengtan.solrgui.ui.ServerDisplay;


public class Main {

	public static void main(String[] args) {
		for (Server server:Config.getServers()) {	
			new ServerDisplay(server.getName(), server.getAllDocuments());	
		}
	}
	
}
