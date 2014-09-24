package com.restlet.sqlimport.report;

/**
 * Report line.
 */
public class ReportLine {

	/**
	 * Message to display.
	 */
	private String message;
	/**
	 * Query.
	 */
	private String query;
	/**
	 * Status.
	 */
	private ReportLineStatus reportLineStatus;
	/**
	 * Table.
	 */
	private String table;
	/**
	 * Column.
	 */
	private String column;

	@Override
	public String toString() {
		final StringBuffer out = new StringBuffer();
		if(reportLineStatus != null) {
			out.append("\n - status : ").append(reportLineStatus);
		}
		if(message != null) {
			out.append("\n - message : ").append(message);
		}
		if(query != null) {
			out.append("\n - query : ").append(query);
		}
		if(table != null) {
			out.append("\n - table : ").append(table);
		}
		if(column != null) {
			out.append("\n - column : ").append(column);
		}
		return out.toString();
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(final String message) {
		this.message = message;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(final String query) {
		this.query = query;
	}
	public ReportLineStatus getReportLineStatus() {
		return reportLineStatus;
	}
	public void setReportLineStatus(final ReportLineStatus reportStatus) {
		this.reportLineStatus = reportStatus;
	}
	public String getTable() {
		return table;
	}
	public void setTable(final String table) {
		this.table = table;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(final String column) {
		this.column = column;
	}

}
