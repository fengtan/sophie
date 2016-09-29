package com.github.fengtan.sophie.beans;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.ArrayUtils;

public class Config {

	// TODO make sure hidden file works on windows
	private static final String filename = ".sophie";
	
	// By default, fetch 50 documents at a time when viewing the documents table.
	private static final int DEFAULT_DOCUMENTS_PAGE_SIZE = 50;
	
	// By default, show at most 50 facet values in the filters.
	private static final int DEFAULT_DOCUMENTS_FACETS_LIMIT = 50;
	
	private static Configuration getConfiguration() throws ConfigurationException {
		String filepath = System.getProperty("user.home") + File.separator + filename;
		File file = new File(filepath);
		PropertiesConfiguration configuration = new PropertiesConfiguration(file);
		configuration.setAutoSave(true);
		return configuration;
	}

	public static String[] getFavorites() {
		try {
			return getConfiguration().getStringArray("favorites");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Silently return no favorite and just log the exception.
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
	}
	
	/**
	 * If favorite does not exist in the configuration, then add it.
	 */
	public static void addFavorite(String favorite) {
		try {
			Configuration configuration = getConfiguration();
			List<Object> favorites = configuration.getList("favorites");
			if (!favorites.contains(favorite)) {
				configuration.addProperty("favorites", favorite);	
			}
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Silently do not add the favorite and just log the exception.
		}
	}
	
	public static int getDocumentsPageSize() {
		try {
			return getConfiguration().getInt("documents.page.size", DEFAULT_DOCUMENTS_PAGE_SIZE);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return DEFAULT_DOCUMENTS_PAGE_SIZE;
		}
	}
	
	public static int getDocumentsFacetsLimit() {
		try {
			return getConfiguration().getInt("documents.facets.limit", DEFAULT_DOCUMENTS_FACETS_LIMIT);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return DEFAULT_DOCUMENTS_FACETS_LIMIT;
		}
	}
	
}
