package com.restlet.sqlimport.report;

/**
 * Report line.
 */
public class ReportLine {

	/**
	 * Message to display.
	 */
	private String msg;
	/**
	 * Query.
	 */
	private String query;
	/**
	 * Status.
	 */
	private ReportStatus reportStatus;

	public String getMsg() {
		return msg;
	}
	public void setMsg(final String msg) {
		this.msg = msg;
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
