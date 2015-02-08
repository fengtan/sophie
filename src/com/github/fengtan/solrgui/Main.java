package com.github.fengtan.solrgui;

import com.github.fengtan.solrgui.solr.Config;
import com.github.fengtan.solrgui.solr.Server;
import com.github.fengtan.solrgui.ui.ServersDisplay;


public class Main {

	public static void main(String[] args) {
		ServersDisplay display = new ServersDisplay();
		for (Server server:Config.getServers()) {
			display.addServer(server);
		}
	}
	
}
