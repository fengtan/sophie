package com.github.fengtan.solrgui;

import com.github.fengtan.solrgui.solr.Config;
import com.github.fengtan.solrgui.solr.Server;
import com.github.fengtan.solrgui.ui.Dashboard;


public class Main {

	public static void main(String[] args) {
		Dashboard dashboard = new Dashboard();
		for (Server server:Config.getServers()) {
			dashboard.addServer(server);
		}
		dashboard.display();
	}
	
}
