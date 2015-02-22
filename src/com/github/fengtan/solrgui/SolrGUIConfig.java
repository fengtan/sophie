package com.github.fengtan.solrgui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

public class SolrGUIConfig {

	private static final String filename = ".solrgui";
	// TODO make it work on windows (hidden file)
	private static final String path = System.getProperty("user.home") + File.separator + filename;

	public static List<SolrGUIServer> getServers() {
		// TODO make sure file is writable + create file if does not exist
		// TODO what if 2 servers with same config ? mess up ini file ?
		List<SolrGUIServer> servers = new ArrayList<SolrGUIServer>();
		try {
			Ini ini = new Ini(new File(path));
			for (Map.Entry<String, Section> entry:ini.entrySet()) {
				Section section = entry.getValue();
				try {
					URL url = new URL(section.get("protocol"), section.get("host"), Integer.parseInt(section.get("port")), section.get("path"));
					servers.add(new SolrGUIServer(url, entry.getKey().toString())); // TODO test server name works
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();					
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return servers;
	}

	public static void addServer(SolrGUIServer server) {
		try {
			Ini ini = new Ini(new File(path));
			ini.add(server.getName(), "protocol", server.getURL().getProtocol());
			ini.add(server.getName(), "host", server.getURL().getHost());
			ini.add(server.getName(), "port", server.getURL().getPort());
			ini.add(server.getName(), "path", server.getURL().getPath());
			ini.store();
		} catch (InvalidFileFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
