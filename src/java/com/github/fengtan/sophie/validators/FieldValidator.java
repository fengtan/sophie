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
package com.github.fengtan.sophie.validators;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.eclipse.jface.dialogs.IInputValidator;

/**
 * Validates a field name against a list of field globs.
 */
public class FieldValidator implements IInputValidator {

    /**
     * Map of fields keyed by field name.
     */
    private Map<String, FieldInfo> fields;

    /**
     * Collection of excluded fields (already displayed in the table).
     */
    private Collection<String> excludedFieldNames;

    /**
     * Create a new field validator.
     * 
     * @param fields
     *            Solr fields. Field names may be globs (e.g. ss_*).
     */
    public FieldValidator(Map<String, FieldInfo> fields, Collection<String> excludedFieldNames) {
        this.fields = fields;
        this.excludedFieldNames = excludedFieldNames;
    }

    @Override
    public String isValid(String fieldNameCandidate) {
        // A glob itself is not a valid field name.
        if (StringUtils.contains(fieldNameCandidate, "*")) {
            return "Field name should not contain any asterisk (\"*\").";
        }
        // User should not be able to add a field already listed in the table.
        if (excludedFieldNames.contains(fieldNameCandidate)) {
            return "Field \"" + fieldNameCandidate + "\" already exists.";
        }
        // The value entered by the user should match one of the fields.
        if (getMatchingField(fieldNameCandidate) == null) {
            return "\"" + fieldNameCandidate + "\" is not a valid field name.";
        }
        return null;
    }

    /**
     * Get field matching a candidate field name.
     * 
     * @return Field matching the candidate (e.g. ss_* if the candidate is
     *         ss_foobar), or null if the candidate matches no field.
     * @see org.apache.solr.schema.Schema
     */
    public FieldInfo getMatchingField(String fieldNameCondidate) {
        for (Map.Entry<String, FieldInfo> field : fields.entrySet()) {
            // Glob (e.g. ss_*) -> Regex (e.g. ss_.*).
            Pattern pattern = Pattern.compile(field.getKey().replace("*", ".*"));
            if (pattern.matcher(fieldNameCondidate).matches()) {
                return field.getValue();
            }
        }
        // Field name candidate matches no existing field.
        return null;
    }

}
