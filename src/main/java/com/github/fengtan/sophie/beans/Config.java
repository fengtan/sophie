/**
 * Sophie - A Solr browser and administration tool
 * 
 * Copyright (C) 2016 fengtan<https://github.com/fengtan>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.fengtan.sophie.beans;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.ArrayUtils;

import com.github.fengtan.sophie.Sophie;

/**
 * Save and load configuration from a properties file located in the user's home
 * directory.
 */
public class Config {

    /**
     * Name of the properties file.
     */
    private static final String filename = ".sophie.properties";

    /**
     * Default value if documents.page.size is not listed in the properties
     * file. By default, we fetch 50 documents at a time when browsing the
     * documents table.
     * 
     * @see getDocumentsPageSize
     */
    private static final int DEFAULT_DOCUMENTS_PAGE_SIZE = 50;

    /**
     * Default value if documents.facets.limit is not listed in the properties
     * file. By default, each filter in the documents table may display at most
     * 50 values.
     * 
     * @see getDocumentsFacetsLimit
     */
    private static final int DEFAULT_DOCUMENTS_FACETS_LIMIT = 50;

    /**
     * Read properties file and load it in a configuration object.
     * 
     * @return Configuration object.
     * @throws ConfigurationException
     *             If the configuration could not be loaded.
     */
    private static Configuration getConfiguration() throws ConfigurationException {
        String filepath = System.getProperty("user.home") + File.separator + filename;
        File file = new File(filepath);
        PropertiesConfiguration configuration = new PropertiesConfiguration(file);
        configuration.setAutoSave(true);
        return configuration;
    }

    /**
     * Get Solr servers listed as favorites in the properties file.
     * 
     * @return Array of Solr servers (either Solr URL's or ZooKeeper hosts).
     */
    public static String[] getFavorites() {
        try {
            String[] favorites = getConfiguration().getStringArray("favorites");
            Arrays.sort(favorites);
            return favorites;
        } catch (ConfigurationException e) {
            // Silently return no favorite and log the exception.
            Sophie.log.warn("Unable to get favorites from configuration file", e);
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
    }

    /**
     * Add a Solr server to favorites listed in the properties file.
     * 
     * @param favorite
     *            The new favorite (either a Solr URL or a ZooKeeper host).
     */
    public static void addFavorite(String favorite) {
        try {
            Configuration configuration = getConfiguration();
            List<Object> favorites = configuration.getList("favorites");
            // If the server is already listed in the favorites, then do
            // nothing.
            if (!favorites.contains(favorite)) {
                configuration.addProperty("favorites", favorite);
            }
        } catch (ConfigurationException e) {
            // Silently do not add the favorite and log the exception.
            Sophie.log.warn("Unable to add favorite to configuration file: " + favorite, e);
        }
    }

    /**
     * Get page size from the properties file. If no value is listed in the
     * properties file, return DEFAULT_DOCUMENTS_PAGE_SIZE
     * 
     * @return Page size used in the documents table.
     */
    public static int getDocumentsPageSize() {
        try {
            return getConfiguration().getInt("documents.page.size", DEFAULT_DOCUMENTS_PAGE_SIZE);
        } catch (ConfigurationException e) {
            // Silently return the default value and log the exception.
            Sophie.log.warn("Unable to get page size from configuration file", e);
            return DEFAULT_DOCUMENTS_PAGE_SIZE;
        }
    }

    /**
     * Get facets limit from the properties file. If no value is listed in the
     * properties file, return DEFAULT_DOCUMENTS_FACETS_LIMIT.
     * 
     * @return Facets limit used in the documents table.
     */
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
