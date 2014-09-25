package com.restlet.sqlimport.model.resdef;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity
 */
public class Entity {

	private String name;
	private String pkPolicy;
	private List<Field> fields = new ArrayList<Field>();

	public Field getFieldForName(final String name) {
		for(final Field field : fields) {
			if(field.getName().equalsIgnoreCase(name)) {
				return field;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(final List<Field> fields) {
		this.fields = fields;
	}
	public String getPkPolicy() {
		return pkPolicy;
	}
	public void setPkPolicy(final String pkPolicy) {
		this.pkPolicy = pkPolicy;
	}

}
