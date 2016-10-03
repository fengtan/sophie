package com.github.fengtan.sophie.validators;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.IInputValidator;

/**
 * Validates a field name against a list of regular/dynamic fields.
 * 
 * @see org.apache.solr.schema.Schema
 */
public class FieldValidator implements IInputValidator {

	private String[] fields;
	
	public FieldValidator(String[] fields) {
		this.fields = fields;
	}
	
	@Override
	public String isValid(String newText) {
		if (StringUtils.contains(newText, "*")) {
			return "Field name should not contain any asterisk (\"*\")";
		}
		for (String field:fields) {
			Pattern pattern = Pattern.compile(field.replace("*", ".*")); // glob -> regex
			if (pattern.matcher(newText).matches()) {
				return null;
			}
		}
		return "\""+newText+"\" matches no field defined in schema.xml.";
	}

}
