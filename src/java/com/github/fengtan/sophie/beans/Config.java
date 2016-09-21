package com.github.fengtan.sophie.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

	// TODO make sure hidden file works on windows
	// TODO what if file is not properties format
	// TODO could use org.eclipse.jface.preference.PreferenceStore
	private static final String filename = ".sophie";
	private static final String filepath = System.getProperty("user.home") + File.separator + filename;
	
	public static String getURL() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(filepath));
			return properties.getProperty("url", null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void setURL(String url) {
		Properties properties = new Properties();
		properties.put("url", url);
		try {
			properties.store(new FileOutputStream(filepath), "URL of Solr server that was last open");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
