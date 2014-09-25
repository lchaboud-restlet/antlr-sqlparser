package com.restlet.sqlimport.validation;

import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.model.sql.ForeignKey;
import com.restlet.sqlimport.model.sql.Table;
import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLine;
import com.restlet.sqlimport.report.ReportLineStatus;

/**
 * Validate database
 */
public class DatabaseValidator {

	/**
	 * Report
	 */
	private final Report report;

	/**
	 * Constructor
	 * @param report Report (must not be null)
	 */
	public DatabaseValidator(final Report report) {
		this.report = report;
	}

	/**
	 * Validate database
	 * @param database Database
	 */
	public void validateDatabase(final Database database) {
		for(final Table table : database.getTables()) {
			validateTable(table);
		}
	}

	/**
	 * Validate table and return true if the table is valid.
	 * @param table Table
	 * @return true if the table is valid
	 */
	public void validateTable(final Table table) {
		// primary key with only one column
		if(table.getPrimaryKey().getColumnNames().size() > 1) {
			final ReportLine reportLine = new ReportLine();
			report.add(reportLine);
			reportLine.setReportLineStatus(ReportLineStatus.PRIMARY_KEY_MORE_THAN_ONE_COLUMN);
			reportLine.setTable(table.getName());
		}

		// foreign key with only one column
		for(final ForeignKey foreignKey : table.getForeignKeys()) {
			if((foreignKey.getColumnNameOrigins().size() > 1) || (foreignKey.getColumnNameTargets().size() > 1)) {
				final ReportLine reportLine = new ReportLine();
				report.add(reportLine);
				reportLine.setReportLineStatus(ReportLineStatus.FOREIGN_KEY_MORE_THAN_ONE_COLUMN);
				reportLine.setTable(table.getName());
			}
		}
	}

}
