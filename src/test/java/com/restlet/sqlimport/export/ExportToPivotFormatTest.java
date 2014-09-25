package com.restlet.sqlimport.export;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.restlet.sqlimport.model.resdef.Resdef;
import com.restlet.sqlimport.model.sql.Column;
import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.model.sql.ForeignKey;
import com.restlet.sqlimport.model.sql.Table;


public class ExportToPivotFormatTest {

	private DatabaseToResdef databaseToResdef = new DatabaseToResdef();
	private ResdefToJson resdefToJson = new ResdefToJson();

	@Test
	public void getLines1() {
		// Given
		final Database database = new Database();

		// When
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		final String content = resdefToJson.resdefToJson(resdef);

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
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		final String content = resdefToJson.resdefToJson(resdef);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"isPrimaryKey\":true}]}]", content);
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
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		final String content = resdefToJson.resdefToJson(resdef);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"isPrimaryKey\":true},{\"name\":\"column 1\",\"nullable\":true}]}]", content);
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
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		final String content = resdefToJson.resdefToJson(resdef);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"column 1\",\"isPrimaryKey\":true,\"nullable\":true}]}]", content);
	}

	@Test
	public void getLines5_primaryKey_with_more_than_one_column() {
		// Given
		final Database database = new Database();
		final Table table1 = new Table();
		database.getTables().add(table1);
		table1.setName("table 1");
		final Column column1 = new Column();
		column1.setName("column 1");
		table1.getColumnByNames().put(column1.getName(), column1);
		table1.getPrimaryKey().getColumnNames().add("column 1");
		table1.getPrimaryKey().getColumnNames().add("column 2");

		// When
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		final String content = resdefToJson.resdefToJson(resdef);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"isPrimaryKey\":true},{\"name\":\"column 1\",\"nullable\":true}]}]", content);
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
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		final String content = resdefToJson.resdefToJson(resdef);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"isPrimaryKey\":true},{\"name\":\"column 1\",\"type\":\"tableNameTarget\",\"nullable\":true,\"minOccurs\":0,\"maxOccurs\":\"*\"}]}]", content);
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
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		final String content = resdefToJson.resdefToJson(resdef);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"isPrimaryKey\":true},{\"name\":\"column 1\",\"type\":\"convertedType\",\"nullable\":false,\"defaultValue\":\"default\"}]}]", content);
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
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		final String content = resdefToJson.resdefToJson(resdef);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"isPrimaryKey\":true},{\"name\":\"column 1\",\"type\":\"convertedType\",\"nullable\":true,\"defaultValue\":\"default\"}]}]", content);
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
		final Resdef resdef = databaseToResdef.databaseToResdef(database);
		final String content = resdefToJson.resdefToJson(resdef);

		// Then
		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"isPrimaryKey\":true},{\"name\":\"column 1\",\"type\":\"tableNameTarget\",\"nullable\":true,\"defaultValue\":\"default\",\"minOccurs\":0,\"maxOccurs\":\"*\"}]}]", content);
	}

}
