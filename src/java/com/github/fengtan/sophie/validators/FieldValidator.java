package com.github.fengtan.sophie.validators;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.eclipse.jface.dialogs.IInputValidator;

/**
 * Validates a field name against a list of regular/dynamic fields.
 * 
 * @see org.apache.solr.schema.Schema
 */
public class FieldValidator implements IInputValidator {

	private Map<String, FieldInfo> fields;
	
	public FieldValidator(Map<String, FieldInfo> fields) {
		this.fields = fields;
	}
	
	@Override
	public String isValid(String fieldNameCondidate) {
		if (StringUtils.contains(fieldNameCondidate, "*")) {
			return "Field name should not contain any asterisk (\"*\")";
		}
		if (getMatchingField(fieldNameCondidate) == null) {
			return "\""+fieldNameCondidate+"\" matches no field defined in schema.xml.";	
		}
		return null;
	}
	
	/**
	 * @return
	 *   Schema field matching the candidate (e.g. "ss_*" if candidate is "ss_foobar").
	 *   null if the candidate matches no schema field.
	 */
	public FieldInfo getMatchingField(String fieldNameCondidate) {
		for (Map.Entry<String, FieldInfo> field:fields.entrySet()) {
			Pattern pattern = Pattern.compile(field.getKey().replace("*", ".*")); // glob -> regex
			if (pattern.matcher(fieldNameCondidate).matches()) {
				return field.getValue();
			}
		}
		// Field name candidate matches no existing field.
		return null;
	}

}
