package com.restlet.sqlimport;

import com.restlet.sqlimport.export.DatabaseToResdef;
import com.restlet.sqlimport.export.ResdefToJson;
import com.restlet.sqlimport.model.resdef.Resdef;
import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.parser.SqlImport;
import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportStatus;
import com.restlet.sqlimport.type.TypeConverter;
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
		databaseValidator.validateDatabase(database);

		// Convert to Resdef bean
		final DatabaseToResdef databaseToResdef = new DatabaseToResdef();
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		report.setResdef(resdef);

		// Export to JSON
		final ResdefToJson resdefToJson = new ResdefToJson();
		final String json = resdefToJson.resdefToJson(resdef);
		report.setReportStatus(ReportStatus.SUCCESS);

		// Summary
		report.setNbCreatedEntity(database.getTables().size());

		return json;
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
