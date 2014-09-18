package com.restlet.sqlimport.model;

public class CleEtrangere {

	private Column columnOrigin;
	private Column columnTarget;
	private String tableOrigin;
	private String tableTarget;

	public Column getColumnOrigin() {
		return columnOrigin;
	}
	public void setColumnOrigin(final Column columnOrigin) {
		this.columnOrigin = columnOrigin;
	}
	public Column getColumnTarget() {
		return columnTarget;
	}
	public void setColumnTarget(final Column columnTarget) {
		this.columnTarget = columnTarget;
	}
	public String getTableOrigin() {
		return tableOrigin;
	}
	public void setTableOrigin(final String tableOrigin) {
		this.tableOrigin = tableOrigin;
	}
	public String getTableTarget() {
		return tableTarget;
	}
	public void setTableTarget(final String tableTarget) {
		this.tableTarget = tableTarget;
	}

}
