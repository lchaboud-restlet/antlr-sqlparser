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

}
