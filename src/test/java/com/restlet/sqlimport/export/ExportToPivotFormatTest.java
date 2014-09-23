package com.restlet.sqlimport.export;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.restlet.sqlimport.model.Column;
import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.model.ForeignKey;
import com.restlet.sqlimport.model.Table;


public class ExportToPivotFormatTest {

	private ExportToPivotFormat exportToPivotFormat = new ExportToPivotFormat();

	@Test
	public void getLines1() {
		// Given
		final Database database = new Database();

		// When
		final String content = exportToPivotFormat.getLines(database);

		// Then
		assertEquals("[]", content);
	}

	@Test
	public void getLines2() {
		// Given
		final Database database = new Database();
		final Table table1 = new Table();
		database.getTables().add(table1);
		table1.setName("table 1");

		// When
		final String content = exportToPivotFormat.getLines(database);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[]}]", content);
	}

	@Test
	public void getLines3() {
		// Given
		final Database database = new Database();
		final Table table1 = new Table();
		database.getTables().add(table1);
		table1.setName("table 1");
		final Column column1 = new Column();
		column1.setName("column 1");
		table1.getColumnByNames().put(column1.getName(), column1);

		// When
		final String content = exportToPivotFormat.getLines(database);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"column 1\",\"nullable\":true}]}]", content);
	}

	@Test
	public void getLines4_primaryKey() {
		// Given
		final Database database = new Database();
		final Table table1 = new Table();
		database.getTables().add(table1);
		table1.setName("table 1");
		final Column column1 = new Column();
		column1.setName("column 1");
		table1.getColumnByNames().put(column1.getName(), column1);
		table1.getPrimaryKey().getColumnNames().add("column 1");

		// When
		final String content = exportToPivotFormat.getLines(database);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"column 1\",\"nullable\":true}]}]", content);
	}

	@Test
	public void getLines_foreignKey() {
		// Given
		final Database database = new Database();
		final Table table1 = new Table();
		database.getTables().add(table1);
		table1.setName("table 1");
		final Column column1 = new Column();
		column1.setName("column 1");
		table1.getColumnByNames().put(column1.getName(), column1);
		final ForeignKey foreignKey = new ForeignKey();
		foreignKey.setTableNameOrigin("tableNameOrigin");
		foreignKey.setTableNameTarget("tableNameTarget");
		foreignKey.getColumnNameOrigins().add("column 1");
		foreignKey.getColumnNameTargets().add("columnNameTarget");
		table1.getForeignKeys().add(foreignKey);

		// When
		final String content = exportToPivotFormat.getLines(database);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"column 1\",\"type\":\"tableNameTarget\",\"minOccurs\":0,\"maxOccurs\":\"*\",\"nullable\":true}]}]", content);
	}

	@Test
	public void getLines6() {
		// Given
		final Database database = new Database();
		final Table table1 = new Table();
		database.getTables().add(table1);
		table1.setName("table 1");
		final Column column1 = new Column();
		table1.getColumnByNames().put(column1.getName(), column1);
		column1.setName("column 1");
		column1.setType("type");
		column1.setConvertedType("convertedType");
		column1.setLength("length");
		column1.setIsNotNull(true);
		column1.setDefaultValue("default");

		// When
		final String content = exportToPivotFormat.getLines(database);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"column 1\",\"type\":\"convertedType\",\"nullable\":false,\"defaultValue\":\"default\"}]}]", content);
	}

	@Test
	public void getLines7() {
		// Given
		final Database database = new Database();
		final Table table1 = new Table();
		database.getTables().add(table1);
		table1.setName("table 1");
		final Column column1 = new Column();
		table1.getColumnByNames().put(column1.getName(), column1);
		column1.setName("column 1");
		column1.setType("type");
		column1.setConvertedType("convertedType");
		column1.setLength("length");
		column1.setIsNotNull(false);
		column1.setDefaultValue("default");

		// When
		final String content = exportToPivotFormat.getLines(database);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"column 1\",\"type\":\"convertedType\",\"nullable\":true,\"defaultValue\":\"default\"}]}]", content);
	}

	@Test
	public void getLines8() {
		// Given
		final Database database = new Database();
		final Table table1 = new Table();
		database.getTables().add(table1);
		table1.setName("table 1");
		final Column column1 = new Column();
		table1.getColumnByNames().put(column1.getName(), column1);
		column1.setName("column 1");
		column1.setType("type");
		column1.setConvertedType("convertedType");
		column1.setLength("length");
		column1.setIsNotNull(false);
		column1.setDefaultValue("default");
		final ForeignKey foreignKey = new ForeignKey();
		foreignKey.setTableNameOrigin("tableNameOrigin");
		foreignKey.setTableNameTarget("tableNameTarget");
		foreignKey.getColumnNameOrigins().add("column 1");
		foreignKey.getColumnNameTargets().add("columnNameTarget");
		table1.getForeignKeys().add(foreignKey);

		// When
		final String content = exportToPivotFormat.getLines(database);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"column 1\",\"type\":\"tableNameTarget\",\"minOccurs\":0,\"maxOccurs\":\"*\",\"nullable\":true,\"defaultValue\":\"default\"}]}]", content);
	}

}
