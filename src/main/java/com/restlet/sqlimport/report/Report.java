package com.restlet.sqlimport.report;

import java.util.Collection;
import java.util.HashMap;
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

	public Map<String, ReportLine> getReportLineByQuerys() {
		return reportLineByQuerys;
	}

	public void setReportLineByQuerys(final Map<String, ReportLine> reportLineByQuerys) {
		this.reportLineByQuerys = reportLineByQuerys;
	}


}
