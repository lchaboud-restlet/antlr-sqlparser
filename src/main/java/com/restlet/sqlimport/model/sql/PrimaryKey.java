package com.restlet.sqlimport.model.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Primary key.
 */
public class PrimaryKey {

	/**
	 * Column names.
	 */
	List<String> columnNames = new ArrayList<String>();

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(final List<String> columnNames) {
		this.columnNames = columnNames;
	}

}
