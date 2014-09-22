package com.restlet.sqlimport.report;

/**
 * Status of the import.
 */
public enum ReportStatus {

	/** Successful import */
	SUCCESS,

	/** SQL query filtered : not a CREATE TABLE or a ALTER TABLE */
	FILTERED,

	/** SQL query not filtered - it is a temporary status : the query will be parsed by the parser */
	TO_PARSE,

	/** Error during parsing */
	PARSING_ERROR

}
