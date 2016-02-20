package com.github.fengtan.solrgui.beans;

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

	// TODO 'add server to favorites'
	// TODO make sure hidden file works on windows
	private static final String filename = ".solrgui";

	public static Ini getIni() throws IOException {
		// TODO make sure file is writable
		// TODO what if 2 servers with same config ? mess up ini file ?
		String path = System.getProperty("user.home") + File.separator + filename;
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		return new Ini(file);
	}
	
	public static List<SolrGUIServer> getServers() {
		List<SolrGUIServer> servers = new ArrayList<SolrGUIServer>();
		try {
			Ini ini = getIni();
			for (Map.Entry<String, Section> entry:ini.entrySet()) {
				Section section = entry.getValue();
				try {
					// TODO what if one of the params does not exist.
					URL url = new URL(section.get("protocol"), section.get("host"), Integer.parseInt(section.get("port")), section.get("path"));
					String name = entry.getKey().toString();
					String q = section.get("q");
					int rows = Integer.parseInt(section.get("rows"));
					servers.add(new SolrGUIServer(url, name, q, rows));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();					
				}
			}
			return servers;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<SolrGUIServer>();
		}
	}

	public static void addServer(SolrGUIServer server) {
		try {
			Ini ini = getIni();
			ini.add(server.getName(), "protocol", server.getURL().getProtocol());
			ini.add(server.getName(), "host", server.getURL().getHost());
			ini.add(server.getName(), "port", server.getURL().getPort());
			ini.add(server.getName(), "path", server.getURL().getPath());
			ini.add(server.getName(), "q", server.getQ());
			ini.add(server.getName(), "rows", server.getRows());
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
