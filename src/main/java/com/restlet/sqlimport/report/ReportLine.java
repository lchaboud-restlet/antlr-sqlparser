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
	private ReportStatus reportStatus;

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
	public ReportStatus getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(final ReportStatus reportStatus) {
		this.reportStatus = reportStatus;
	}

}
