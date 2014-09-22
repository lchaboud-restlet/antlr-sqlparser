package com.restlet.sqlimport.parser;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import com.restlet.sqlimport.model.Column;
import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.model.ForeignKey;
import com.restlet.sqlimport.model.Table;
import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.util.Util;

/**
 * Test : SQL import.
 */
public class SqlImportTest {

	private Report report = new Report();
	private SqlImport sqlImport = new SqlImport(report);
	private Util util = new Util();

	// @Test
	public void testRead_nofile() {

		final InputStream is = null;

		// When
		final Database database = sqlImport.read(is);

		assertNull(database);
	}

	// @Test
	public void testRead_file() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/test.txt");
		final InputStream in = new FileInputStream(file);

		// When
		final String content = util.read(in);

		assertEquals("Ligne 1\nLigne 2", content);
	}

	// @Test
	public void testRead_import1() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/import1.sql");
		final InputStream in = new FileInputStream(file);

		// When
		final Database database = sqlImport.read(in);

		assertEquals(2, database.getTables().size());
		final Table table1 = database.getTables().get(0);
		assertEquals("films", table1.getName());
		assertEquals(6, table1.getColumnByNames().keySet().size());
		final Table col2 = database.getTables().get(1);
		assertEquals("distributors", col2.getName());
		assertEquals(2, col2.getColumnByNames().keySet().size());
		assertNotNull(col2.getColumnByNames().get("did"));
		final Column column = col2.getColumnByNames().get("did");
		assertEquals("did",column.getName());
	}

	@Test
	public void testRead_standard() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/standard.sql");
		final InputStream in = new FileInputStream(file);

		// When
		final Database database = sqlImport.read(in);

		// Database schema
		assertEquals(3, database.getTables().size());
		final Table table3 = database.getTables().get(0);
		assertEquals("table3", table3.getName());
		final Table table2 = database.getTables().get(1);
		assertEquals("table2", table2.getName());
		final Table table1 = database.getTables().get(2);
		assertEquals("table1", table1.getName());

		// Table 1
		System.out.println(table1.getColumnByNames().keySet());
		assertEquals(6, table1.getColumnByNames().keySet().size());
		final Column t1_id = table1.getColumnByNames().get("id");
		final Column t1_nom = table1.getColumnByNames().get("nom");
		final Column t1_dt = table1.getColumnByNames().get("dt");
		final Column t1_num = table1.getColumnByNames().get("num");
		final Column t1_id_table2 = table1.getColumnByNames().get("id_table2");
		final Column t1_id_table3 = table1.getColumnByNames().get("id_table3");
		// name
		assertEquals("id", t1_id.getName());
		assertEquals("nom", t1_nom.getName());
		assertEquals("dt", t1_dt.getName());
		assertEquals("num", t1_num.getName());
		assertEquals("id_table2", t1_id_table2.getName());
		assertEquals("id_table3", t1_id_table3.getName());
		// type
		assertEquals("INTEGER", t1_id.getType());
		assertEquals("VARCHAR", t1_nom.getType());
		assertEquals("DATE", t1_dt.getType());
		assertEquals("INTEGER", t1_num.getType());
		assertEquals("INTEGER", t1_id_table2.getType());
		assertEquals("INTEGER", t1_id_table3.getType());
		// primary key
		assertEquals("id", table1.getPrimaryKey().getName());
		// not null
		assertTrue(t1_id.getIsNotNull());
		assertFalse(t1_nom.getIsNotNull());
		assertTrue(t1_dt.getIsNotNull());
		assertFalse(t1_num.getIsNotNull());
		assertFalse(t1_id_table2.getIsNotNull());
		assertFalse(t1_id_table3.getIsNotNull());
		//  clé étrangère
		final ForeignKey cle_id_table2 = table1.getForeignKeys().get(0);
		assertEquals("table1", cle_id_table2.getTableNameOrigin());
		assertEquals("table2", cle_id_table2.getTableNameTarget());
		assertEquals("id_table2", cle_id_table2.getColumnNameOrigins().get(0));
		assertEquals("id", cle_id_table2.getColumnNameTargets().get(0));
		final ForeignKey cle_id_table3 = table1.getForeignKeys().get(1);
		assertEquals("table1", cle_id_table3.getTableNameOrigin());
		assertEquals("table3", cle_id_table3.getTableNameTarget());
		assertEquals("id_table3", cle_id_table3.getColumnNameOrigins().get(0));
		assertEquals("id", cle_id_table3.getColumnNameTargets().get(0));

		// Table 2
		assertEquals(4, table2.getColumnByNames().keySet().size());
		final Column t2_id = table2.getColumnByNames().get("id");
		final Column t2_nom = table2.getColumnByNames().get("nom");
		final Column t2_id_table3 = table2.getColumnByNames().get("id_table3");
		final Column t2_nom_table3 = table2.getColumnByNames().get("nom_table3");
		assertEquals("id", t2_id.getName());
		assertEquals("nom", t2_nom.getName());
		assertEquals("id_table3", t2_id_table3.getName());
		assertEquals("nom_table3", t2_nom_table3.getName());
		// primary key
		assertEquals("id", table2.getPrimaryKey().getName());
		// not null
		assertTrue(t2_id.getIsNotNull());
		assertFalse(t2_nom.getIsNotNull());

		// Table 3
		assertEquals(2, table3.getColumnByNames().keySet().size());
		final Column t3_id = table3.getColumnByNames().get("id");
		final Column t3_nom = table3.getColumnByNames().get("nom");
		assertEquals("id", t3_id.getName());
		assertEquals("nom", t3_nom.getName());
		// primary key
		assertEquals("id", table3.getPrimaryKey().getName());
		// not null
		assertTrue(t3_id.getIsNotNull());
		assertFalse(t3_nom.getIsNotNull());
	}

	@Test
	public void testRead_mysql() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/mysql.sql");
		final InputStream in = new FileInputStream(file);

		// When
		final Database database = sqlImport.read(in);

		assertEquals(5, database.getTables().size());


	}

	// @Test
	public void testRead_postgres() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/postgres.sql");
		final InputStream in = new FileInputStream(file);

		// When
		final Database database = sqlImport.read(in);

		assertEquals(2, database.getTables().size());
	}

	// @Test
	public void testRead_oracle1() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/oracle1.sql");
		final InputStream in = new FileInputStream(file);

		// When
		final Database database = sqlImport.read(in);

		assertEquals(2, database.getTables().size());
	}

	// @Test
	public void testRead_oracle2() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/oracle2.sql");
		final InputStream in = new FileInputStream(file);

		// When
		final Database database = sqlImport.read(in);

		assertEquals(2, database.getTables().size());
	}

}
