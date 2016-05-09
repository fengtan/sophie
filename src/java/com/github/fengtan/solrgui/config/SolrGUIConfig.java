package com.github.fengtan.solrgui.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

public class SolrGUIConfig {

	// TODO make sure hidden file works on windows
	// TODO what if file is not ini format
	private static final String filename = ".solrgui";
	private static final String filepath = System.getProperty("user.home") + File.separator + filename;
	
	private static Ini getIni() throws IOException {
		// TODO make sure file is writable
		// TODO what if 2 servers with same config ? mess up ini file ?
		File file = new File(filepath);
		if (!file.exists()) {
			file.createNewFile();
		}
		Ini ini = new Ini(file);
		// Make sure the "global" section exists. TODO no need to use sections
		if (!ini.containsKey("global")) {
			ini.add("global");	
		}
		return ini;
	}
	
	public static List<String> getURLs() {
		try {
			return getIni().get("global").getAll("urls");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}

	public static void addURL(String url) {
		try {
			Ini ini = getIni();
			ini.get("global").add("urls", url);
			ini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void removeURL(String url) {
		try {
			Ini ini = getIni();
			List<String> urls = ini.get("global").getAll("urls");
			int index = urls.indexOf(url);
			if (index != -1) {
				ini.get("global").remove("urls", index);
				ini.store();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
}
