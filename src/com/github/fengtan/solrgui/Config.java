package com.github.fengtan.solrgui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import obsolete.Server;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

public class Config {

	// TODO make path configurable (or in .properties)
	private static final String path = "conf/servers.ini"; 

	public static List<Server> getServers() {
		// TODO make sure file is writable + create file if does not exist
		// TODO what if 2 servers with same config ? mess up ini file ?
		List<Server> servers = new ArrayList<Server>();
		try {
			Ini ini = new Ini(new File(path));
			for (Map.Entry<String, Section> entry:ini.entrySet()) {
				Section section = entry.getValue();
				try {
					URL url = new URL(section.get("protocol"), section.get("host"), Integer.parseInt(section.get("port")), section.get("path"));
					servers.add(new Server(url, entry.getKey()));
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

	public static void addServer(Server server) {
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
