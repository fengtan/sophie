package com.github.fengtan.solrgui;


public class Main {

	public static void main(String[] args) {
		for (Server server:Config.getServers()) {	
			new ServerDisplay(server.getName(), server.getAllDocuments());	
		}
	}
	
}
