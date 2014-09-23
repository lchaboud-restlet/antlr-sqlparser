package com.restlet.sqlimport.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Report.
 */
public class Report {

	/**
	 * Global status.
	 */
	private ReportStatus reportStatus;

	/**
	 * Report lines.
	 */
	private List<ReportLine> reportLines = new ArrayList<ReportLine>();

	/**
	 * Add report for the query.
	 * @param reportLine Report line
	 */
	public void add(final ReportLine reportLine) {

		reportLines.add(reportLine);
	}

	/**
	 * Get report lines belongs their status
	 * @param reportStatus Report status
	 * @return Number of report lines
	 */
	public List<ReportLine> getReportLinesForStatus(final ReportLineStatus reportLineStatus) {
		final List<ReportLine> reportLines = new ArrayList<ReportLine>();

		for(final ReportLine reportLine : getReportLines()) {
			if(reportLine.getReportLineStatus() == reportLineStatus) {
				reportLines.add(reportLine);
			}
		}

		return reportLines;
	}

	/**
	 * Return report line corresponding to a query
	 * @param query SQL Query
	 * @return report line
	 */
	public ReportLine getReportLineForQuery(final String query) {
		if(query == null) {
			return null;
		}
		for(final ReportLine reportLine : reportLines) {
			if(query.equals(reportLine.getQuery())) {
				return reportLine;
			}
		}
		return null;
	}

	/**
	 * Add message.
	 * @param reportStatus Status
	 * @param message Message
	 */
	public void addMessage(final ReportLineStatus reportLineStatus, final String message) {
		final ReportLine reportLine = new ReportLine();
		reportLine.setReportLineStatus(reportLineStatus);
		reportLine.setMessage(message);
		reportLines.add(reportLine);
	}

	public List<ReportLine> getReportLines() {
		return reportLines;
	}

	public void setReportLines(final List<ReportLine> messages) {
		this.reportLines = messages;
	}

	public ReportStatus getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(final ReportStatus reportStatus) {
		this.reportStatus = reportStatus;
	}

}
