package com.restlet.sqlimport;

import com.restlet.sqlimport.export.ExportToPivotFormat;
import com.restlet.sqlimport.export.TypeConverter;
import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.parser.SqlImport;
import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportStatus;
import com.restlet.sqlimport.validation.DatabaseValidator;

/**
 * Main manager for SQL import and export in the pivot format file
 */
public class MainProcess {

	/**
	 * Report.
	 */
	private Report report = new Report();

	/**
	 * Main method
	 */
	public String process(final String sqlContent) {

		// Load SQL file, filter and parse SQL queries
		final SqlImport sqlImport = new SqlImport(report);
		final Database database = sqlImport.getDatabase(sqlContent);

		if((database == null) || database.getTables().isEmpty()) {
			// Empty database
			report.setReportStatus(ReportStatus.EMPTY_DATABASE);
			return null;
		}

		// Convert SQL types to Entity store types
		final TypeConverter typeConverter = new TypeConverter(report);
		typeConverter.convertTypeFromSQLToEntityStore(database);

		// Database schema validator
		final DatabaseValidator databaseValidator = new DatabaseValidator(report);
		final boolean isValidDatabase = databaseValidator.validateDatabase(database);

		if(!isValidDatabase) {
			report.setReportStatus(ReportStatus.NOT_SUPPORTED_DATABASE);
			return null;
		} else {
			// Export
			final ExportToPivotFormat sqlExport = new ExportToPivotFormat();
			final String pivotFileContent = sqlExport.getContent(database);

			report.setReportStatus(ReportStatus.PIVOT_FILE_GENERATED);
			return pivotFileContent;
		}

	}

	/**
	 * Get report.
	 * @return report
	 */
	public Report getReport() {
		return report;
	}

	public void setReport(final Report report) {
		this.report = report;
	}

}
