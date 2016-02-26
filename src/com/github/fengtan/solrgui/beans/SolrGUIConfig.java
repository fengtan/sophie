package com.github.fengtan.solrgui.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

public class SolrGUIConfig {

	// TODO 'add server to favorites'
	// TODO make sure hidden file works on windows
	// TODO properties file does not support name duplicates -> prevent user from creating 2 servers with the same name
	private static final String filename = ".solrgui";
	private static final String filepath = System.getProperty("user.home") + File.separator + filename;
	
	public static List<SolrGUIServer> getServers() {
		List<SolrGUIServer> servers = new ArrayList<SolrGUIServer>();
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(filepath));
			for (Entry<Object, Object> entry:properties.entrySet()) {
				URL url = new URL(entry.getValue().toString());
				String name = entry.getKey().toString();
				servers.add(new SolrGUIServer(url, name));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return servers;
	}

	public static void addServer(SolrGUIServer server) {
		Properties properties = new Properties();
		properties.put(server.getName(), server.getURL().toString());
		try {
			properties.store(new FileOutputStream(filepath), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
