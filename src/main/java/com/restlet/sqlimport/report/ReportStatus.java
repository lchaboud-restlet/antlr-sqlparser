package com.restlet.sqlimport.report;

/**
 * Status of the import.
 */
public enum ReportStatus {

	/** Successful import */
	SUCCESS,

	/** SQL query filtered : not a CREATE TABLE or a ALTER TABLE */
	FILTERED,

	/** Error during parsing */
	PARSING_ERROR

}
