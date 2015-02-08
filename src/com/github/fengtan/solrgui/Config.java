package com.github.fengtan.solrgui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

public class Config {

	private static String path = "conf/servers.ini";
	
	public static List<Server> getServers() {
		// TODO make path configurable + make sure file is writable + create file if does not exist
		// TODO what if 2 servers with same config ? mess up ini file ?
		List<Server> servers = new ArrayList<Server>();
		Ini ini;
		try {
			ini = new Ini(new File(path));
			for (Map.Entry<String, Section> entry:ini.entrySet()) {
				Section section = entry.getValue();
				URL url = new URL(section.get("protocol"), section.get("host"), Integer.parseInt(section.get("port")), section.get("path"));
				servers.add(new Server(url, entry.getKey()));
			}	
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return servers;
	}
	
}
