package com.restlet.sqlimport.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.model.sql.ForeignKey;
import com.restlet.sqlimport.model.sql.Table;
import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLine;
import com.restlet.sqlimport.report.ReportLineStatus;


public class DatabaseValidatorTest {

	@Test
	public void testValidateTable_ok() {
		// Given
		final Report report = new Report();
		final DatabaseValidator databaseValidator = new DatabaseValidator(report);

		final Database database = new Database();
		final Table table = new Table();
		database.getTables().add(table);

		// When
		databaseValidator.validateDatabase(database);

		// Then
		assertTrue(report.getReportLines().isEmpty());
	}

	@Test
	public void testValidateTable_pk_with_one_column() {
		// Given
		final Report report = new Report();
		final DatabaseValidator databaseValidator = new DatabaseValidator(report);

		final Database database = new Database();
		final Table table = new Table();
		database.getTables().add(table);
		table.getPrimaryKey().getColumnNames().add("c1");

		// When
		databaseValidator.validateDatabase(database);

		// Then
		assertTrue(report.getReportLines().isEmpty());
	}

	@Test
	public void testValidateTable_pk_with_two_columns() {
		// Given
		final Report report = new Report();
		final DatabaseValidator databaseValidator = new DatabaseValidator(report);

		final Database database = new Database();
		final Table table = new Table();
		database.getTables().add(table);
		table.getPrimaryKey().getColumnNames().add("c1");
		table.getPrimaryKey().getColumnNames().add("c2");

		// When
		databaseValidator.validateDatabase(database);

		// Then
		assertEquals(1,report.getReportLines().size());
		final ReportLine reportLine = report.getReportLines().get(0);
		assertEquals(ReportLineStatus.PRIMARY_KEY_MORE_THAN_ONE_COLUMN,reportLine.getReportLineStatus());
	}

	@Test
	public void testValidateTable_fk_with_one_column() {
		// Given
		final Report report = new Report();
		final DatabaseValidator databaseValidator = new DatabaseValidator(report);

		final Database database = new Database();
		final Table table = new Table();
		database.getTables().add(table);
		final ForeignKey foreignKey = new ForeignKey();
		table.getForeignKeys().add(foreignKey);
		foreignKey.getColumnNameOrigins().add("c1");
		foreignKey.getColumnNameTargets().add("c1");

		// When
		databaseValidator.validateDatabase(database);

		// Then
		assertTrue(report.getReportLines().isEmpty());
	}

	@Test
	public void testValidateTable_fk_with_two_columns() {
		// Given
		final Report report = new Report();
		final DatabaseValidator databaseValidator = new DatabaseValidator(report);

		final Database database = new Database();
		final Table table = new Table();
		database.getTables().add(table);
		final ForeignKey foreignKey = new ForeignKey();
		table.getForeignKeys().add(foreignKey);
		foreignKey.getColumnNameOrigins().add("c1");
		foreignKey.getColumnNameOrigins().add("c2");
		foreignKey.getColumnNameTargets().add("c1");
		foreignKey.getColumnNameTargets().add("c2");

		// When
		databaseValidator.validateDatabase(database);

		// Then
		assertEquals(1,report.getReportLines().size());
		final ReportLine reportLine = report.getReportLines().get(0);
		assertEquals(ReportLineStatus.FOREIGN_KEY_MORE_THAN_ONE_COLUMN,reportLine.getReportLineStatus());
	}

}
