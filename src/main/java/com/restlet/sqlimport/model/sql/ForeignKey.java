package com.restlet.sqlimport.model.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Foreing key
 */
public class ForeignKey {

	/**
	 * Name of columns in the origin table
	 */
	private List<String> columnNameOrigins = new ArrayList<String>();
	/**
	 * Name of columns in the foreign table
	 */
	private List<String> columnNameTargets = new ArrayList<String>();
	/**
	 * Name of the origin table
	 */
	private String tableNameOrigin;
	/**
	 * Name of the foreign table
	 */
	private String tableNameTarget;

	public List<String> getColumnNameOrigins() {
		return columnNameOrigins;
	}
	public void setColumnNameOrigins(final List<String> columnNameOrigins) {
		this.columnNameOrigins = columnNameOrigins;
	}
	public List<String> getColumnNameTargets() {
		return columnNameTargets;
	}
	public void setColumnNameTargets(final List<String> columnNameTargets) {
		this.columnNameTargets = columnNameTargets;
	}
	public String getTableNameOrigin() {
		return tableNameOrigin;
	}
	public void setTableNameOrigin(final String tableNameOrigin) {
		this.tableNameOrigin = tableNameOrigin;
	}
	public String getTableNameTarget() {
		return tableNameTarget;
	}
	public void setTableNameTarget(final String tableNameTarget) {
		this.tableNameTarget = tableNameTarget;
	}

}
