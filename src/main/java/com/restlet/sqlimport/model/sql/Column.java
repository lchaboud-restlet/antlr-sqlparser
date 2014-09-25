package com.restlet.sqlimport.model.sql;

/**
 * Table column
 */
public class Column {

	/**
	 * name
	 */
	private String name;
	/**
	 * type
	 */
	private String type;
	/**
	 * Length
	 */
	private String length;
	/**
	 * Indicates if the column must be not null
	 */
	private Boolean isNotNull = false;
	/**
	 * Converted type.
	 */
	private String convertedType;
	/**
	 * Default value.
	 */
	private String defaultValue;

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

	public Boolean getIsNotNull() {
		return isNotNull;
	}

	public void setIsNotNull(final Boolean isNotNull) {
		this.isNotNull = isNotNull;
	}

	public String getConvertedType() {
		return convertedType;
	}

	public void setConvertedType(final String convertedType) {
		this.convertedType = convertedType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
