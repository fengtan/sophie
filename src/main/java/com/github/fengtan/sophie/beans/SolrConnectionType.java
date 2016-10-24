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

/**
 * Different ways to connect to Solr: either Direct HTTP or SolrCloud.
 */
public enum SolrConnectionType {

    DIRECT_HTTP("Direct HTTP", "Solr URL:", "http://localhost:8983/solr/gettingstarted"), SOLR_CLOUD("SolrCloud", "ZooKeeper host:", "localhost:9983");

    /**
     * Name of connection type.
     */
    private String typeName;

    /**
     * Label of connection string.
     */
    private String valueLabel;

    /**
     * Default value of connection string.
     */
    private String valueDefault;

    /**
     * Create a new connection type.
     * 
     * @param typeName
     *            Name of connection type.
     * @param valueLabel
     *            Label of connection string.
     * @param valueDefault
     *            Default value of connection string.
     */
    private SolrConnectionType(String typeName, String valueLabel, String valueDefault) {
        this.typeName = typeName;
        this.valueLabel = valueLabel;
        this.valueDefault = valueDefault;
    }

    /**
     * Get name of connection type.
     * 
     * @return Name of connection type.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Get label of connection string.
     * 
     * @return Label of connection string.
     */
    public String getValueLabel() {
        return valueLabel;
    }

    /**
     * Get default value of connection string.
     * 
     * @return Default value of connection string.
     */
    public String getValueDefault() {
        return valueDefault;
    }

}
