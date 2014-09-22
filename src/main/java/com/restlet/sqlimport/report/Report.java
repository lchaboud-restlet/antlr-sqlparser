package com.restlet.sqlimport.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Report.
 */
public class Report {

	/**
	 * Report lines by query.
	 */
	private Map<String, ReportLine> reportLineByQuerys = new HashMap<String, ReportLine>();

	/**
	 * Add report for the query.
	 * @param reportLine Report line
	 */
	public void add(final ReportLine reportLine) {
		reportLineByQuerys.put(reportLine.getQuery(), reportLine);
	}

	/**
	 * Get report lines.
	 * @return Report lines
	 */
	public Collection<ReportLine> getReportLines() {
		return reportLineByQuerys.values();
	}

	/**
	 * Get report lines belongs their status
	 * @param reportStatus Report status
	 * @return Number of report lines
	 */
	public List<ReportLine> getReportLinesForStatus(final ReportStatus reportStatus) {
		final List<ReportLine> reportLines = new ArrayList<ReportLine>();

		for(final ReportLine reportLine : getReportLines()) {
			if(reportLine.getReportStatus() == reportStatus) {
				reportLines.add(reportLine);
			}
		}

		return reportLines;
	}

	public Map<String, ReportLine> getReportLineByQuerys() {
		return reportLineByQuerys;
	}

	public void setReportLineByQuerys(final Map<String, ReportLine> reportLineByQuerys) {
		this.reportLineByQuerys = reportLineByQuerys;
	}


}
