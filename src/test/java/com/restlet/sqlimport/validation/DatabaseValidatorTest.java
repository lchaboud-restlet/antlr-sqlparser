package com.restlet.sqlimport.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.model.ForeignKey;
import com.restlet.sqlimport.model.Table;
import com.restlet.sqlimport.report.Report;


public class DatabaseValidatorTest {

	@Test
	public void testValidateTable_empty() {
		// Given
		final Report report = new Report();
		final DatabaseValidator databaseValidator = new DatabaseValidator(report);

		final Database database = new Database();
		final Table table = new Table();
		database.getTables().add(table);

		// When
		final boolean isValid = databaseValidator.validateDatabase(database);

		// Then
		assertTrue(isValid);
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
		final boolean isValid = databaseValidator.validateDatabase(database);

		// Then
		assertTrue(isValid);
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
		final boolean isValid = databaseValidator.validateDatabase(database);

		// Then
		assertFalse(isValid);
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
		final boolean isValid = databaseValidator.validateDatabase(database);

		// Then
		assertTrue(isValid);
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
		final boolean isValid = databaseValidator.validateDatabase(database);

		// Then
		assertFalse(isValid);
	}

}
