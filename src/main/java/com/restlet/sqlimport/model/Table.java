package com.restlet.sqlimport.model;

import java.util.HashMap;
import java.util.Map;

public class Table {

	private String name;
	private Map<String,Column> columnByNames = new HashMap<String,Column>();

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Map<String, Column> getColumnByNames() {
		return columnByNames;
	}

	public void setColumnByNames(final Map<String, Column> columnByNames) {
		this.columnByNames = columnByNames;
	}

}
