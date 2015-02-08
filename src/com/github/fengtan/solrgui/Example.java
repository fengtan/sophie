package com.github.fengtan.solrgui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.solr.common.SolrDocumentList;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

public class Example {

	public static void main(String[] args) {

		try {
			// TODO what if 2 servers with same config ? mess up ini file ?
			Ini ini = new Ini(new File("conf/servers.ini")); // TODO make path configurable + make sure file is writable + create file if does not exist

			for (Map.Entry<String, Section> entry:ini.entrySet()) {
				Section section = entry.getValue();
				URL url = new URL(section.get("protocol"), section.get("host"), Integer.parseInt(section.get("port")), section.get("path"));
				
				SolrGUIServer server = new SolrGUIServer(url, entry.getKey());
				SolrDocumentList list = server.getAllDocuments();
				
				new SolrGUIDisplay(server.getName(), list);
			}
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
