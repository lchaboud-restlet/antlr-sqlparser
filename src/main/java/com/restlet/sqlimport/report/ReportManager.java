package com.restlet.sqlimport.report;

import java.text.SimpleDateFormat;

import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.model.sql.Table;

/**
 * Report manager
 */
public class ReportManager {

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

	/**
	 * Export report
	 * @param report Report
	 * @return String
	 */
	public String toString(final Report report) {
		final StringBuffer out = new StringBuffer();

		out.append(getTrace1(report));
		out.append(getTrace2(report));
		out.append(getTrace3(report));
		out.append(getTrace4(report));
		out.append(getTrace5(report));
		out.append(getTrace6(report));

		return out.toString();
	}

	/**
	 * Trace : Starting sql import process
	 * @param report Report
	 * @return String
	 */
	public String getTrace1(final Report report) {

		final StringBuffer out = new StringBuffer();

		// starting sql import process
		out.append("\n--------------------");
		out.append("\nTrace : ");
		out.append("\n - Location : Starting sql import process");
		out.append("\n - Content : Starting sql import process");
		out.append("\n - Date : ").append(sdf.format(report.getDate()));
		out.append("\n - Type : SQL schema import");
		out.append("\n");

		return out.toString();
	}

	/**
	 * Trace : SQL parsing
	 * @param report Report
	 * @return String
	 */
	public String getTrace2(final Report report) {

		final StringBuffer out = new StringBuffer();

		// sql parsing
		out.append("\n--------------------");
		out.append("\nTrace : ");
		out.append("\n - Location : SQL parsing");
		boolean hasParsingError = false;
		for(final ReportLine reportLine : report.getReportLines()) {
			if(reportLine.getReportLineStatus() == ReportLineStatus.PARSING_ERROR) {
				hasParsingError = true;
			}
		}
		if(hasParsingError) {
			out.append("\n - Level : Error");
		} else {
			out.append("\n - Level : Info");
		}
		out.append("\n - Date : ").append(sdf.format(report.getDate()));
		out.append("\n - Content : ");
		for(final ReportLine reportLine : report.getReportLines()) {
			if( (reportLine.getReportLineStatus() == ReportLineStatus.IGNORED)
					|| (reportLine.getReportLineStatus() == ReportLineStatus.PARSING_ERROR)
					|| (reportLine.getReportLineStatus() == ReportLineStatus.PARSED) ) {
				final String begin = "["+reportLine.getReportLineStatus()+"] : ";
				out.append("\n").append(begin);
				final StringBuffer spaces = new StringBuffer("\n");
				out.append(reportLine.getQuery().replace("\n",spaces.toString()));
				if(reportLine.getReportLineStatus() == ReportLineStatus.PARSING_ERROR) {
					out.append(spaces).append("Error : ").append(reportLine.getMessage());
				}
			}
		}

		return out.toString();
	}

	/**
	 * Trace : Checking database structure
	 * @param report Report
	 * @return String
	 */
	public String getTrace3(final Report report) {

		final StringBuffer out = new StringBuffer();

		// checking database structure
		out.append("\n");
		out.append("\n--------------------");
		out.append("\nTrace : ");
		out.append("\n - Location : Checking database structure");
		boolean hasValidationWarning = false;
		for(final ReportLine reportLine : report.getReportLines()) {
			if((reportLine.getReportLineStatus() == ReportLineStatus.UNKNOWN_SQL_TYPE)
					|| (reportLine.getReportLineStatus() == ReportLineStatus.PRIMARY_KEY_MORE_THAN_ONE_COLUMN)
					|| (reportLine.getReportLineStatus() == ReportLineStatus.FOREIGN_KEY_MORE_THAN_ONE_COLUMN)) {
				hasValidationWarning = true;
			}
		}
		if(hasValidationWarning) {
			out.append("\n - Level : Warning");
		} else {
			out.append("\n - Level : Info");
		}
		out.append("\n - Date : ").append(sdf.format(report.getDate()));
		out.append("\n - Content : ");
		if(!hasValidationWarning) {
			out.append("The database is imported with no warning");
		} else {
			for(final ReportLine reportLine : report.getReportLines()) {
				if((reportLine.getReportLineStatus() == ReportLineStatus.UNKNOWN_SQL_TYPE)
						|| (reportLine.getReportLineStatus() == ReportLineStatus.PRIMARY_KEY_MORE_THAN_ONE_COLUMN)
						|| (reportLine.getReportLineStatus() == ReportLineStatus.FOREIGN_KEY_MORE_THAN_ONE_COLUMN)) {
					out.append("\n[WARNING] : ");
					if(reportLine.getTable() != null) {
						out.append("Table : "+reportLine.getTable()+" - ");
					}
					if(reportLine.getReportLineStatus() == ReportLineStatus.PRIMARY_KEY_MORE_THAN_ONE_COLUMN) {
						out.append("Composite primary key is not supported : created a new primary key named \"id\"");
					}
					if(reportLine.getReportLineStatus() == ReportLineStatus.FOREIGN_KEY_MORE_THAN_ONE_COLUMN) {
						out.append("Foreign key on several columns is not supported and is ignored");
					}
					if(reportLine.getReportLineStatus() == ReportLineStatus.UNKNOWN_SQL_TYPE) {
						out.append("The SQL type \""+reportLine.getMessage()+"\" is not supported and replaced by the “String” type");
					}
				}
			}
		}

		return out.toString();
	}

	/**
	 * Trace : Configuring entity store
	 * @param report Report
	 * @return String
	 */
	public String getTrace4(final Report report) {

		final StringBuffer out = new StringBuffer();


		// configuring entity store
		out.append("\n--------------------");
		out.append("\nTrace : ");
		out.append("\n - Location : Configuring entity store");
		out.append("\n - Content : Configuring entity store");
		out.append("\n - Date : ").append(sdf.format(report.getDate()));
		out.append("\n - Type : SQL schema import");
		out.append("\n");

		return out.toString();
	}

	/**
	 * Trace : Generating entity store
	 * @param report Report
	 * @return String
	 */
	public String getTrace5(final Report report) {

		final StringBuffer out = new StringBuffer();


		// generating entity store
		out.append("\n--------------------");
		out.append("\nTrace : ");
		out.append("\n - Location : Generating entity store");
		out.append("\n - Content : Generating entity store");
		out.append("\n - Date : ").append(sdf.format(report.getDate()));
		out.append("\n - Type : SQL schema import");
		out.append("\n");

		return out.toString();
	}

	/**
	 * Trace : SQL schema imported : summary
	 * @param report Report
	 * @return String
	 */
	public String getTrace6(final Report report) {

		final StringBuffer out = new StringBuffer();

		final ReportStatus reportStatus = report.getReportStatus();

		final Database database = report.getDatabase();

		// SQL schema imported : summary
		out.append("\n--------------------");
		out.append("\nTrace : ");
		if(reportStatus == ReportStatus.SUCCESS) {
			out.append("\n - Location : SQL schema successfully imported : "+report.getNbCreatedEntity()+" entities created");
			out.append("\n - Content : "+report.getNbCreatedEntity()+" entities successfully created :");
			if(database != null) {
				for(final Table table : database.getTables()) {
					out.append("\n    - ").append(table.getName());
				}
			}
		}
		if(reportStatus == ReportStatus.EMPTY_DATABASE) {
			out.append("\n - Location : SQL schema import failed : no table definition found in the SQL file content");
			out.append("\n - Content : No entity created");
		}
		out.append("\n - Date : ").append(sdf.format(report.getDate()));
		out.append("\n - Type : SQL schema import");
		out.append("\n");
		/*
		out.append("\n");
		for(final Table table : database.getTables()) {
			out.append("\n- entity : ").append(table.getName());
			out.append("\n  - fields : [").append(table.getName());
			for(final Column column : table.getColumnByNames()) {
				out.append("\n    - {name:").append(.getName());
			}
		}
		 */
		return out.toString();
	}

}
