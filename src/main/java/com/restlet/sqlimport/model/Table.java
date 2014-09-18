package com.restlet.sqlimport.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {

	private String name;
	private Map<String,Column> columnByNames = new HashMap<String,Column>();
	private Column primaryKey;
	private List<CleEtrangere> cleEtrangeres = new ArrayList<CleEtrangere>();

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

	public Column getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(final Column primaryKey) {
		this.primaryKey = primaryKey;
	}

	public List<CleEtrangere> getCleEtrangeres() {
		return cleEtrangeres;
	}

	public void setCleEtrangeres(final List<CleEtrangere> cleEtrangeres) {
		this.cleEtrangeres = cleEtrangeres;
	}

}
