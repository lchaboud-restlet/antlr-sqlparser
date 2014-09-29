package com.restlet.sqlimport.parser;

import java.util.ArrayList;
import java.util.List;

import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLine;
import com.restlet.sqlimport.report.ReportLineStatus;

/**
 * Parse SQL file and returns the list of SQL queries.
 */
public class GetSqlQuery {

	/**
	 * Report.
	 */
	private final Report report;

	/**
	 * Constructor.
	 * @param report Report (must not be null)
	 */
	public GetSqlQuery(final Report report) {
		this.report = report;
	}

	/**
	 * Indicates if the query is correct
	 * @param query Query
	 * @return boolean
	 */
	public boolean isQueryFiltered(final String query) {
		final String queryUpperCase = query.toUpperCase();
		if(queryUpperCase.indexOf("CREATE TABLE") == 0) {
			return false;
		}
		// support ALTER TABLE with ADD CONSTRAINT
		if(queryUpperCase.indexOf("ALTER TABLE") == 0) {
			if(queryUpperCase.indexOf("ADD CONSTRAINT") != -1) {
				if((queryUpperCase.indexOf("PRIMARY KEY") != -1) || (queryUpperCase.indexOf("FOREIGN KEY") != -1)) {
					return false;
				}
			}
			if(queryUpperCase.indexOf("ADD PRIMARY KEY") != -1) {
				return false;
			}
			if(queryUpperCase.indexOf("ADD FOREIGN KEY") != -1) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Return the liste of SQL queries to parse
	 * @param content SQL file content
	 * @return SQL queries
	 */
	public List<String> getSqlQuerys(final String content) {
		final List<String> querys = new ArrayList<String>();

		int posStart = getPosStartQuery(content, 0);
		int posEnd = getPosEndQuery(content, posStart);
		while((posStart != -1) && (posStart < content.length()) && (posEnd < content.length())) {

			final String query = content.substring(posStart, posEnd);

			final boolean isFiltered = isQueryFiltered(query);
			if(!isFiltered) {
				querys.add(query);
			}

			final ReportLine reportLine = new ReportLine();
			reportLine.setQuery(query);
			if(isFiltered) {
				reportLine.setReportLineStatus(ReportLineStatus.IGNORED);
			} else {
				reportLine.setReportLineStatus(ReportLineStatus.TO_PARSE);
			}
			getReport().add(reportLine);

			if((posEnd + 1) >= content.length()) {
				posStart = -1;
			} else {
				posStart = getPosStartQuery(content,posEnd+1);
				posEnd = getPosEndQuery(content, posStart);
			}
		}

		return querys;
	}

	/**
	 * Return the position of the beginning character of the next SQL query.
	 * @param content SQL content
	 * @param pos Current position in the SQL content
	 * @return Position of the beginning character of the next SQL query
	 */
	private int getPosStartQuery(final String content, int pos) {

		boolean inLineComment = false;
		boolean inMultiLineComment = false;
		boolean inStringValue = false;

		while(pos < content.length()) {
			final char character = content.charAt(pos);
			if(character == '/') {
				if(!inStringValue && !inLineComment && !inMultiLineComment) {
					if(((pos+1) < content.length()) && (content.charAt(pos+1) == '*')) {
						inMultiLineComment = true;
						pos = pos+2;
						continue;
					}
				}
			}
			if(character == '*') {
				if(!inStringValue && !inLineComment && inMultiLineComment) {
					if(((pos+1) < content.length()) && (content.charAt(pos+1) == '/')) {
						inMultiLineComment = false;
						pos = pos+2;
						continue;
					}
				}
			}
			if(character == '"') {
				if(!inLineComment && !inMultiLineComment) {
					inStringValue = !inStringValue;
				}
			}
			if(character == '-') {
				if(!inStringValue && !inLineComment && !inMultiLineComment) {
					if(((pos+1) < content.length()) && (content.charAt(pos+1) == '-')) {
						inLineComment = true;
						pos = pos + 2;
						continue;
					}
				}
			}
			if((character == '\n') || (character == '\r') ) {
				if(inLineComment) {
					inLineComment = false;
				}
			}
			if(((character >= 'a') && (character <= 'z')) ||((character >= 'A') && (character <= 'Z'))) {
				if(!inStringValue && !inLineComment && !inMultiLineComment) {
					break;
				}
			}
			pos++;
		}

		return pos;
	}

	/**
	 * Return the position of the end of the current SQL query.
	 * @param content SQL content
	 * @param pos Current position in the SQL content
	 * @return Position of the end of the current SQL query
	 */
	private int getPosEndQuery(final String content, int pos) {

		boolean inLineComment = false;
		boolean inMultiLineComment = false;
		boolean inStringValue = false;

		while(pos < content.length()) {
			final char character = content.charAt(pos);
			if(character == '/') {
				if(!inStringValue && !inLineComment && !inMultiLineComment) {
					if(((pos+1) < content.length()) && (content.charAt(pos+1) == '*')) {
						inMultiLineComment = true;
						pos = pos+2;
						continue;
					}
				}
			}
			if(character == '*') {
				if(!inStringValue && !inLineComment && inMultiLineComment) {
					if(((pos+1) < content.length()) && (content.charAt(pos+1) == '/')) {
						inMultiLineComment = false;
						pos = pos+2;
						continue;
					}
				}
			}
			if(character == '"') {
				if(!inLineComment && !inMultiLineComment) {
					inStringValue = !inStringValue;
				}
			}
			if(character == '-') {
				if(!inStringValue && !inLineComment && !inMultiLineComment) {
					if(((pos+1) < content.length()) && (content.charAt(pos+1) == '-')) {
						inLineComment = true;
						pos = pos + 2;
						continue;
					}
				}
			}
			if((character == '\n') || (character == '\r') ) {
				if(inLineComment) {
					inLineComment = false;
				}
			}
			if(character == ';') {
				if(!inStringValue && !inLineComment && !inMultiLineComment) {
					break;
				}
			}
			pos++;
		}

		return pos;
	}

	/**
	 * Get report
	 * @return report
	 */
	public Report getReport() {
		return report;
	}

}
