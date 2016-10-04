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

public enum SolrConnectionType {

    DIRECT_HTTP("Direct HTTP", "Solr URL:", "http://localhost:8983/solr/gettingstarted"), SOLR_CLOUD("SolrCloud", "ZooKeeper host:", "localhost:9983");

    private SolrConnectionType(String typeName, String valueLabel, String valueDefault) {
        this.typeName = typeName;
        this.valueLabel = valueLabel;
        this.valueDefault = valueDefault;
    }

    private String typeName;
    private String valueLabel;
    private String valueDefault;

    public String getTypeName() {
        return typeName;
    }

    public String getValueLabel() {
        return valueLabel;
    }

    public String getValueDefault() {
        return valueDefault;
    }

}
