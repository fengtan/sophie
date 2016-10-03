package com.github.fengtan.sophie.beans;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.ArrayUtils;

import com.github.fengtan.sophie.Sophie;

public class Config {

	// TODO make sure hidden file works on windows
	private static final String filename = ".sophie.properties";
	
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
			// Silently return no favorite and log the exception.
			Sophie.log.warn("Unable to get favorites from configuration file", e);
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
			// Silently do not add the favorite and log the exception.
			Sophie.log.warn("Unable to add favorite to configuration file: "+favorite, e);
		}
	}
	
	public static int getDocumentsPageSize() {
		try {
			return getConfiguration().getInt("documents.page.size", DEFAULT_DOCUMENTS_PAGE_SIZE);
		} catch (ConfigurationException e) {
			// Silently return the default value and log the exception.
			Sophie.log.warn("Unable to get page size from configuration file", e);
			return DEFAULT_DOCUMENTS_PAGE_SIZE;
		}
	}
	
	public static int getDocumentsFacetsLimit() {
		try {
			return getConfiguration().getInt("documents.facets.limit", DEFAULT_DOCUMENTS_FACETS_LIMIT);
		} catch (ConfigurationException e) {
			// Silently return the default value and log the exception.
			Sophie.log.warn("Unable to get facets limit from configuration file", e);
			return DEFAULT_DOCUMENTS_FACETS_LIMIT;
		}
	}
	
}
