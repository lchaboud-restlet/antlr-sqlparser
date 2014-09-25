package com.restlet.sqlimport.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.restlet.sqlimport.model.resdef.Entity;
import com.restlet.sqlimport.model.resdef.Field;
import com.restlet.sqlimport.model.resdef.Resdef;
import com.restlet.sqlimport.model.sql.Column;
import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.model.sql.ForeignKey;
import com.restlet.sqlimport.model.sql.Table;


public class DatabaseToResdefTest {

	private DatabaseToResdef databaseToResdef = new DatabaseToResdef();

	@Test
	public void getLines1() {
		// Given
		final Database database = new Database();

		// When
		final Resdef resdef = databaseToResdef.databaseToResdef(database);

		// Then
		assertTrue(resdef.getEntities().isEmpty());
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

		// Then
		assertEquals(1, resdef.getEntities().size());
		final Entity entity = resdef.getEntities().get(0);
		assertEquals("table 1", entity.getName());
		assertEquals("user_generated_value", entity.getPkPolicy());
		assertEquals(1, entity.getFields().size());
		final Field id = entity.getFields().get(0);
		assertEquals("id", id.getName());
		assertEquals("string", id.getType());
		assertEquals(true, id.getIsPrimaryKey());
		assertNull(id.getNullable());
		assertNull(id.getDefaultValue());
		assertNull(id.getMinOccurs());
		assertNull(id.getMaxOccurs());
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

		// Then
		assertEquals(1, resdef.getEntities().size());
		final Entity entity = resdef.getEntities().get(0);
		assertEquals("table 1", entity.getName());
		assertEquals("user_generated_value", entity.getPkPolicy());
		assertEquals(2, entity.getFields().size());
		final Field id = entity.getFields().get(0);
		assertEquals("id", id.getName());
		assertEquals("string", id.getType());
		assertEquals(true, id.getIsPrimaryKey());
		assertNull(id.getNullable());
		assertNull(id.getDefaultValue());
		assertNull(id.getMinOccurs());
		assertNull(id.getMaxOccurs());
		final Field field1 = entity.getFields().get(1);
		assertEquals("column 1", field1.getName());
		assertNull(field1.getType());
		assertNull(field1.getIsPrimaryKey());
		assertTrue(field1.getNullable());
		assertNull(field1.getDefaultValue());
		assertNull(field1.getMinOccurs());
		assertNull(field1.getMaxOccurs());
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

		// Then
		assertEquals(1, resdef.getEntities().size());
		final Entity entity = resdef.getEntities().get(0);
		assertEquals("table 1", entity.getName());
		assertEquals("user_generated_value", entity.getPkPolicy());
		assertEquals(1, entity.getFields().size());
		final Field field1 = entity.getFields().get(0);
		assertEquals("column 1", field1.getName());
		assertNull(field1.getType());
		assertEquals(true, field1.getIsPrimaryKey());
		assertTrue(field1.getNullable());
		assertNull(field1.getDefaultValue());
		assertNull(field1.getMinOccurs());
		assertNull(field1.getMaxOccurs());
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

		// Then
		assertEquals(1, resdef.getEntities().size());
		final Entity entity = resdef.getEntities().get(0);
		assertEquals("table 1", entity.getName());
		assertEquals("user_generated_value", entity.getPkPolicy());
		assertEquals(2, entity.getFields().size());
		final Field id = entity.getFields().get(0);
		assertEquals("id", id.getName());
		assertEquals("string", id.getType());
		assertEquals(true, id.getIsPrimaryKey());
		assertNull(id.getNullable());
		assertNull(id.getDefaultValue());
		assertNull(id.getMinOccurs());
		assertNull(id.getMaxOccurs());
		final Field field1 = entity.getFields().get(1);
		assertEquals("column 1", field1.getName());
		assertNull(field1.getType());
		assertNull(field1.getIsPrimaryKey());
		assertTrue(field1.getNullable());
		assertNull(field1.getDefaultValue());
		assertNull(field1.getMinOccurs());
		assertNull(field1.getMaxOccurs());
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

		// Then
		//assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"key\":\"string\",\"isPrimaryKey\":true},{\"name\":\"column 1\",\"type\":\"tableNameTarget\",\"minOccurs\":0,\"maxOccurs\":\"*\",\"nullable\":true}]}]", content);
		assertEquals(1, resdef.getEntities().size());
		final Entity entity = resdef.getEntities().get(0);
		assertEquals("table 1", entity.getName());
		assertEquals("user_generated_value", entity.getPkPolicy());
		assertEquals(2, entity.getFields().size());
		final Field id = entity.getFields().get(0);
		assertEquals("id", id.getName());
		assertEquals("string", id.getType());
		assertEquals(true, id.getIsPrimaryKey());
		assertNull(id.getNullable());
		assertNull(id.getDefaultValue());
		assertNull(id.getMinOccurs());
		assertNull(id.getMaxOccurs());
		final Field field1 = entity.getFields().get(1);
		assertEquals("column 1", field1.getName());
		assertEquals("tableNameTarget",field1.getType());
		assertNull(field1.getIsPrimaryKey());
		assertTrue(field1.getNullable());
		assertNull(field1.getDefaultValue());
		assertEquals(Integer.valueOf(0),field1.getMinOccurs());
		assertEquals("*",field1.getMaxOccurs());
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

		// Then
		assertEquals(1, resdef.getEntities().size());
		final Entity entity = resdef.getEntities().get(0);
		assertEquals("table 1", entity.getName());
		assertEquals("user_generated_value", entity.getPkPolicy());
		assertEquals(2, entity.getFields().size());
		final Field id = entity.getFields().get(0);
		assertEquals("id", id.getName());
		assertEquals("string", id.getType());
		assertEquals(true, id.getIsPrimaryKey());
		assertNull(id.getNullable());
		assertNull(id.getDefaultValue());
		assertNull(id.getMinOccurs());
		assertNull(id.getMaxOccurs());
		final Field field1 = entity.getFields().get(1);
		assertEquals("column 1", field1.getName());
		assertEquals("convertedType",field1.getType());
		assertNull(field1.getIsPrimaryKey());
		assertEquals(false,field1.getNullable());
		assertEquals("default",field1.getDefaultValue());
		assertNull(field1.getMinOccurs());
		assertNull(field1.getMaxOccurs());
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

		// Then
		//assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"key\":\"string\",\"isPrimaryKey\":true},{\"name\":\"column 1\",\"type\":\"convertedType\",\"nullable\":true,\"defaultValue\":\"default\"}]}]", content);
		assertEquals(1, resdef.getEntities().size());
		final Entity entity = resdef.getEntities().get(0);
		assertEquals("table 1", entity.getName());
		assertEquals("user_generated_value", entity.getPkPolicy());
		assertEquals(2, entity.getFields().size());
		final Field id = entity.getFields().get(0);
		assertEquals("id", id.getName());
		assertEquals("string", id.getType());
		assertEquals(true, id.getIsPrimaryKey());
		assertNull(id.getNullable());
		assertNull(id.getDefaultValue());
		assertNull(id.getMinOccurs());
		assertNull(id.getMaxOccurs());
		final Field field1 = entity.getFields().get(1);
		assertEquals("column 1", field1.getName());
		assertEquals("convertedType",field1.getType());
		assertNull(field1.getIsPrimaryKey());
		assertEquals(true,field1.getNullable());
		assertEquals("default",field1.getDefaultValue());
		assertNull(field1.getMinOccurs());
		assertNull(field1.getMaxOccurs());
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

		// Then
		//		assertEquals("[{\"name\":\"table 1\",\"pkPolicy\":\"user_generated_value\",\"fields\":[{\"name\":\"id\",\"key\":\"string\",\"isPrimaryKey\":true},{\"name\":\"column 1\",\"type\":\"tableNameTarget\",\"minOccurs\":0,\"maxOccurs\":\"*\",\"nullable\":true,\"defaultValue\":\"default\"}]}]", content);
		assertEquals(1, resdef.getEntities().size());
		final Entity entity = resdef.getEntities().get(0);
		assertEquals("table 1", entity.getName());
		assertEquals("user_generated_value", entity.getPkPolicy());
		assertEquals(2, entity.getFields().size());
		final Field id = entity.getFields().get(0);
		assertEquals("id", id.getName());
		assertEquals("string", id.getType());
		assertEquals(true, id.getIsPrimaryKey());
		assertNull(id.getNullable());
		assertNull(id.getDefaultValue());
		assertNull(id.getMinOccurs());
		assertNull(id.getMaxOccurs());
		final Field field1 = entity.getFields().get(1);
		assertEquals("column 1", field1.getName());
		assertEquals("tableNameTarget",field1.getType());
		assertNull(field1.getIsPrimaryKey());
		assertEquals(true,field1.getNullable());
		assertEquals("default",field1.getDefaultValue());
		assertEquals(Integer.valueOf(0),field1.getMinOccurs());
		assertEquals("*",field1.getMaxOccurs());
	}

}
