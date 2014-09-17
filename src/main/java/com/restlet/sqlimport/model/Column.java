package com.restlet.sqlimport.model;

public class Column {

	private String name;
	private String type;
	private String length;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getLength() {
		return length;
	}

	public void setLength(final String length) {
		this.length = length;
	}

}
