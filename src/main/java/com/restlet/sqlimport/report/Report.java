package com.restlet.sqlimport.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.model.Table;

/**
 * Report.
 */
public class Report {

	/**
	 * Date.
	 */
	private final Date date = new Date();

	/**
	 * Global status.
	 */
	private ReportStatus reportStatus;

	/**
	 * Report lines.
	 */
	private List<ReportLine> reportLines = new ArrayList<ReportLine>();

	/**
	 * Number of created entities
	 */
	private int nbCreatedEntity;

	/**
	 * Database schema.
	 */
	private Database database;

	@Override
	public String toString() {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

		final StringBuffer out = new StringBuffer();
		/*
		out.append("Report :");
		out.append("\n - date : ").append(sdf.format(date));
		out.append("\n - status : ").append(reportStatus);
		out.append("\n - nb created entities : ").append(nbCreatedEntity);
		out.append("\nLines :");
		for(final ReportLine reportLine : reportLines) {
			out.append("\n");
			out.append(reportLine.toString());
		}
		 */

		out.append("\n--------------------");
		out.append("\nMessage : ");
		if(reportStatus == ReportStatus.SUCCESS) {
			out.append("\n - Title : SQL schema successfully imported : "+nbCreatedEntity+" entities created");
			out.append("\n - Content : "+nbCreatedEntity+" entities successfully created :");
			if(database != null) {
				for(final Table table : database.getTables()) {
					out.append("\n               - ").append(table.getName());
				}
			}
		}
		if(reportStatus == ReportStatus.EMPTY_DATABASE) {
			out.append("\n - Title : SQL schema import failed : no table definition found in the SQL file content");
			out.append("\n - Content : No entity created");
		}
		out.append("\n - Date : ").append(sdf.format(date));
		out.append("\n - Type : SQL schema import");
		out.append("\n");
		out.append("\n--------------------");
		out.append("\nTrace : ");
		out.append("\n - Location : SQL schema parsing");
		boolean hasParsingError = false;
		for(final ReportLine reportLine : reportLines) {
			if(reportLine.getReportLineStatus() == ReportLineStatus.PARSING_ERROR) {
				hasParsingError = true;
			}
		}
		if(hasParsingError) {
			out.append("\n - Level : Error");
		} else {
			out.append("\n - Level : Info");
		}
		out.append("\n - Date : ").append(sdf.format(date));
		out.append("\n - Content : ");
		for(final ReportLine reportLine : reportLines) {
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

		out.append("\n");
		out.append("\n--------------------");
		out.append("\nTrace : ");
		out.append("\n - Location : SQL import validation");
		boolean hasValidationWarning = false;
		for(final ReportLine reportLine : reportLines) {
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
		out.append("\n - Date : ").append(sdf.format(date));
		out.append("\n - Content : ");
		if(!hasValidationWarning) {
			out.append("The database is imported with no warning");
		} else {
			for(final ReportLine reportLine : reportLines) {
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
		out.append("\n");

		return out.toString();
	}

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

	public void setNbCreatedEntity(final int nbCreatedEntity) {
		this.nbCreatedEntity = nbCreatedEntity;
	}

	public int getNbCreatedEntity() {
		return nbCreatedEntity;
	}

	public Date getDate() {
		return date;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(final Database database) {
		this.database = database;
	}

}
