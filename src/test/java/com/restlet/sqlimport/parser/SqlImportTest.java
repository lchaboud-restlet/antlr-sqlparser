package com.restlet.sqlimport.parser;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import com.restlet.sqlimport.model.sql.Column;
import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.model.sql.ForeignKey;
import com.restlet.sqlimport.model.sql.Table;
import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLineStatus;
import com.restlet.sqlimport.report.ReportManager;
import com.restlet.sqlimport.util.Util;

/**
 * Test : SQL import.
 */
public class SqlImportTest {

	private Report report = new Report();
	private ReportManager reportManager = new ReportManager();
	private SqlImport sqlImport = new SqlImport(report);
	private Util util = new Util();

	@Test
	public void testGetDatabase_nofile() {

		// When
		final Database database = sqlImport.getDatabase(null);

		assertNull(database);
	}

	@Test
	public void testGetDatabase_standard() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/standard.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		// Then

		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(4, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// the report contains database schema
		assertTrue(database == report.getDatabase());

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
		// default value
		assertEquals(null, t1_id.getDefaultValue());
		assertEquals(null, t1_nom.getDefaultValue());
		assertEquals(null, t1_dt.getDefaultValue());
		assertEquals("1", t1_num.getDefaultValue());
		assertEquals(null, t1_id_table2.getDefaultValue());
		assertEquals(null, t1_id_table3.getDefaultValue());
		// primary key
		assertEquals(1, table1.getPrimaryKey().getColumnNames().size());
		assertEquals("id", table1.getPrimaryKey().getColumnNames().get(0));
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
		assertEquals(1, table2.getPrimaryKey().getColumnNames().size());
		assertEquals("id", table2.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertTrue(t2_id.getIsNotNull());
		assertFalse(t2_nom.getIsNotNull());
		// clé étrangère
		final ForeignKey fk_table2_table3 = table2.getForeignKeys().get(0);
		assertEquals("table2", fk_table2_table3.getTableNameOrigin());
		assertEquals("table3", fk_table2_table3.getTableNameTarget());
		assertEquals("id_table3", fk_table2_table3.getColumnNameOrigins().get(0));
		assertEquals("id", fk_table2_table3.getColumnNameTargets().get(0));
		assertEquals("nom_table3", fk_table2_table3.getColumnNameOrigins().get(1));
		assertEquals("nom", fk_table2_table3.getColumnNameTargets().get(1));

		// Table 3
		assertEquals(2, table3.getColumnByNames().keySet().size());
		final Column t3_id = table3.getColumnByNames().get("id");
		final Column t3_nom = table3.getColumnByNames().get("nom");
		assertEquals("id", t3_id.getName());
		assertEquals("nom", t3_nom.getName());
		// primary key
		assertEquals(1, table3.getPrimaryKey().getColumnNames().size());
		assertEquals("id", table3.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertTrue(t3_id.getIsNotNull());
		assertFalse(t3_nom.getIsNotNull());
	}

	@Test
	public void testGetDatabase_mysql1() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/mysql1.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		// Then
		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(6, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// Database schema
		assertEquals(5, database.getTables().size());

		final Table Company = database.getTables().get(0);
		assertEquals("Company", Company.getName());
		final Table Contact = database.getTables().get(1);
		assertEquals("Contact", Contact.getName());
		final Table table3 = database.getTables().get(2);
		assertEquals("table3", table3.getName());
		final Table table2 = database.getTables().get(3);
		assertEquals("table2", table2.getName());
		final Table table1 = database.getTables().get(4);
		assertEquals("table1", table1.getName());

		// Table Company
		System.out.println(Contact.getColumnByNames().keySet());
		assertEquals(6, Contact.getColumnByNames().keySet().size());
		final Column t1_id = Contact.getColumnByNames().get("id");
		final Column t1_email = Contact.getColumnByNames().get("email");
		final Column t1_age = Contact.getColumnByNames().get("age");
		final Column t1_name = Contact.getColumnByNames().get("name");
		final Column t1_firstname = Contact.getColumnByNames().get("firstname");
		final Column t1_company_id = Contact.getColumnByNames().get("company_id");
		// name
		assertEquals("id", t1_id.getName());
		assertEquals("email", t1_email.getName());
		assertEquals("age", t1_age.getName());
		assertEquals("name", t1_name.getName());
		assertEquals("firstname", t1_firstname.getName());
		assertEquals("company_id", t1_company_id.getName());
		// type
		assertEquals("VARCHAR", t1_id.getType());
		assertEquals("VARCHAR", t1_email.getType());
		assertEquals("INT", t1_age.getType());
		assertEquals("VARCHAR", t1_name.getType());
		assertEquals("VARCHAR", t1_firstname.getType());
		assertEquals("INT", t1_company_id.getType());
		// primary key
		assertEquals(1, Contact.getPrimaryKey().getColumnNames().size());
		assertEquals("id", Contact.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertFalse(t1_id.getIsNotNull());
		assertTrue(t1_email.getIsNotNull());
		assertFalse(t1_age.getIsNotNull());
		assertFalse(t1_name.getIsNotNull());
		assertFalse(t1_firstname.getIsNotNull());
		assertFalse(t1_company_id.getIsNotNull());
		// clé étrangère
		assertEquals(1, Contact.getForeignKeys().size());
		final ForeignKey cle_company_id = Contact.getForeignKeys().get(0);
		assertEquals("Contact", cle_company_id.getTableNameOrigin());
		assertEquals("Company", cle_company_id.getTableNameTarget());
		assertEquals("company_id", cle_company_id.getColumnNameOrigins().get(0));
		assertEquals("id", cle_company_id.getColumnNameTargets().get(0));

		// table2
		// clé étrangère
		final ForeignKey fk_table2_table3 = table2.getForeignKeys().get(0);
		assertEquals("table2", fk_table2_table3.getTableNameOrigin());
		assertEquals("table3", fk_table2_table3.getTableNameTarget());
		assertEquals("id_table3", fk_table2_table3.getColumnNameOrigins().get(0));
		assertEquals("id", fk_table2_table3.getColumnNameTargets().get(0));
		assertEquals("nom_table3", fk_table2_table3.getColumnNameOrigins().get(1));
		assertEquals("nom", fk_table2_table3.getColumnNameTargets().get(1));
	}

	@Test
	public void testQuery_mysql2_with_fk() {
		final String query =
				"CREATE TABLE `contact` ("
						+"		  `id` varchar(255) NOT NULL DEFAULT '',"
						+"		  `email` varchar(255) NOT NULL,"
						+"		  `age` int(11) DEFAULT NULL,"
						+"		  `name` varchar(255) DEFAULT NULL,"
						+"		  `firstname` varchar(255) DEFAULT NULL,"
						+"		  `company_id` int(11) DEFAULT NULL,"
						+"		  PRIMARY KEY (`id`),"
						+"		  UNIQUE KEY `email` (`email`),"
						+"		  KEY `company_id` (`company_id`),"
						+"		  CONSTRAINT `contact_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)"
						+"		) ENGINE=InnoDB DEFAULT CHARSET=latin1";

		final Database database = new Database();

		final ANTLRInputStream in = new ANTLRInputStream(query);
		final SqlLexer l = new SqlLexer(in);
		final SqlParser p = new SqlParser(new CommonTokenStream(l));
		p.addParseListener(new CreateTableParseListener(p, database));
		p.parse();

		final Table table = database.getTables().get(0);
		assertEquals("contact",table.getName());
		assertEquals(1,table.getForeignKeys().size());
	}

	@Test
	public void testGetDatabase_mysql_mysqlworkbench() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/mysql_mysqlworkbench.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		System.out.println(reportManager.toString(report));

		// Then
		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(5, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// Database schema
		assertEquals(5, database.getTables().size());

		final Table Company = database.getTables().get(0);
		assertEquals("company", Company.getName());
		final Table Contact = database.getTables().get(1);
		assertEquals("contact", Contact.getName());
		final Table table1 = database.getTables().get(2);
		assertEquals("table1", table1.getName());
		final Table table2 = database.getTables().get(3);
		assertEquals("table2", table2.getName());
		final Table table3 = database.getTables().get(4);
		assertEquals("table3", table3.getName());

		// Table Company
		System.out.println(Contact.getColumnByNames().keySet());
		assertEquals(6, Contact.getColumnByNames().keySet().size());
		final Column t1_id = Contact.getColumnByNames().get("id");
		final Column t1_email = Contact.getColumnByNames().get("email");
		final Column t1_age = Contact.getColumnByNames().get("age");
		final Column t1_name = Contact.getColumnByNames().get("name");
		final Column t1_firstname = Contact.getColumnByNames().get("firstname");
		final Column t1_company_id = Contact.getColumnByNames().get("company_id");
		// name
		assertEquals("id", t1_id.getName());
		assertEquals("email", t1_email.getName());
		assertEquals("age", t1_age.getName());
		assertEquals("name", t1_name.getName());
		assertEquals("firstname", t1_firstname.getName());
		assertEquals("company_id", t1_company_id.getName());
		// type
		assertEquals("varchar", t1_id.getType());
		assertEquals("varchar", t1_email.getType());
		assertEquals("int", t1_age.getType());
		assertEquals("varchar", t1_name.getType());
		assertEquals("varchar", t1_firstname.getType());
		assertEquals("int", t1_company_id.getType());
		// primary key
		assertEquals(1, Contact.getPrimaryKey().getColumnNames().size());
		assertEquals("id", Contact.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertTrue(t1_id.getIsNotNull());
		assertTrue(t1_email.getIsNotNull());
		assertFalse(t1_age.getIsNotNull());
		assertFalse(t1_name.getIsNotNull());
		assertFalse(t1_firstname.getIsNotNull());
		assertFalse(t1_company_id.getIsNotNull());
		// clé étrangère
		assertEquals(1, Contact.getForeignKeys().size());
		final ForeignKey cle_company_id = Contact.getForeignKeys().get(0);
		assertEquals("contact", cle_company_id.getTableNameOrigin());
		assertEquals("company", cle_company_id.getTableNameTarget());
		assertEquals("company_id", cle_company_id.getColumnNameOrigins().get(0));
		assertEquals("id", cle_company_id.getColumnNameTargets().get(0));

		// table2
		// clé étrangère
		final ForeignKey fk_table2_table3 = table2.getForeignKeys().get(0);
		assertEquals("table2", fk_table2_table3.getTableNameOrigin());
		assertEquals("table3", fk_table2_table3.getTableNameTarget());
		assertEquals("id_table3", fk_table2_table3.getColumnNameOrigins().get(0));
		assertEquals("id", fk_table2_table3.getColumnNameTargets().get(0));
		assertEquals("nom_table3", fk_table2_table3.getColumnNameOrigins().get(1));
		assertEquals("nom", fk_table2_table3.getColumnNameTargets().get(1));
	}

	@Test
	public void testGetDatabase_mysql_mysqldump() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/mysql_mysqldump.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		System.out.println(reportManager.toString(report));

		// Then
		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(5, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// Database schema
		assertEquals(5, database.getTables().size());

		final Table Company = database.getTables().get(0);
		assertEquals("company", Company.getName());
		final Table Contact = database.getTables().get(1);
		assertEquals("contact", Contact.getName());
		final Table table1 = database.getTables().get(2);
		assertEquals("table1", table1.getName());
		final Table table2 = database.getTables().get(3);
		assertEquals("table2", table2.getName());
		final Table table3 = database.getTables().get(4);
		assertEquals("table3", table3.getName());

		// Table Company
		System.out.println(Contact.getColumnByNames().keySet());
		assertEquals(6, Contact.getColumnByNames().keySet().size());
		final Column t1_id = Contact.getColumnByNames().get("id");
		final Column t1_email = Contact.getColumnByNames().get("email");
		final Column t1_age = Contact.getColumnByNames().get("age");
		final Column t1_name = Contact.getColumnByNames().get("name");
		final Column t1_firstname = Contact.getColumnByNames().get("firstname");
		final Column t1_company_id = Contact.getColumnByNames().get("company_id");
		// name
		assertEquals("id", t1_id.getName());
		assertEquals("email", t1_email.getName());
		assertEquals("age", t1_age.getName());
		assertEquals("name", t1_name.getName());
		assertEquals("firstname", t1_firstname.getName());
		assertEquals("company_id", t1_company_id.getName());
		// type
		assertEquals("varchar", t1_id.getType());
		assertEquals("varchar", t1_email.getType());
		assertEquals("int", t1_age.getType());
		assertEquals("varchar", t1_name.getType());
		assertEquals("varchar", t1_firstname.getType());
		assertEquals("int", t1_company_id.getType());
		// primary key
		assertEquals(1, Contact.getPrimaryKey().getColumnNames().size());
		assertEquals("id", Contact.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertTrue(t1_id.getIsNotNull());
		assertTrue(t1_email.getIsNotNull());
		assertFalse(t1_age.getIsNotNull());
		assertFalse(t1_name.getIsNotNull());
		assertFalse(t1_firstname.getIsNotNull());
		assertFalse(t1_company_id.getIsNotNull());
		// clé étrangère
		assertEquals(1, Contact.getForeignKeys().size());
		final ForeignKey cle_company_id = Contact.getForeignKeys().get(0);
		assertEquals("contact", cle_company_id.getTableNameOrigin());
		assertEquals("company", cle_company_id.getTableNameTarget());
		assertEquals("company_id", cle_company_id.getColumnNameOrigins().get(0));
		assertEquals("id", cle_company_id.getColumnNameTargets().get(0));

		// table2
		// clé étrangère
		final ForeignKey fk_table2_table3 = table2.getForeignKeys().get(0);
		assertEquals("table2", fk_table2_table3.getTableNameOrigin());
		assertEquals("table3", fk_table2_table3.getTableNameTarget());
		assertEquals("id_table3", fk_table2_table3.getColumnNameOrigins().get(0));
		assertEquals("id", fk_table2_table3.getColumnNameTargets().get(0));
		assertEquals("nom_table3", fk_table2_table3.getColumnNameOrigins().get(1));
		assertEquals("nom", fk_table2_table3.getColumnNameTargets().get(1));
	}

	@Test
	public void testGetDatabase_postgres_sql() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/postgres.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		assertEquals(9, database.getTables().size());

		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(9, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// Database schema
		assertEquals(9, database.getTables().size());

		final Table users = database.getTables().get(0);
		assertEquals("users", users.getName());
		final Table article = database.getTables().get(1);
		assertEquals("article", article.getName());
		final Table Comment = database.getTables().get(2);
		assertEquals("Comment", Comment.getName());
		final Table Link = database.getTables().get(3);
		assertEquals("Link", Link.getName());
		final Table keyword = database.getTables().get(4);
		assertEquals("keyword", keyword.getName());
		final Table article_keyword = database.getTables().get(5);
		assertEquals("article_keyword", article_keyword.getName());
		final Table table3 = database.getTables().get(6);
		assertEquals("table3", table3.getName());
		final Table table2 = database.getTables().get(7);
		assertEquals("table2", table2.getName());
		final Table table1 = database.getTables().get(8);
		assertEquals("table1", table1.getName());

		// Table table2
		System.out.println(table2.getColumnByNames().keySet());
		assertEquals(4, table2.getColumnByNames().keySet().size());
		final Column t_table2_id = table2.getColumnByNames().get("id");
		final Column t_table2_nom = table2.getColumnByNames().get("nom");
		final Column t_table2_id_table3 = table2.getColumnByNames().get("id_table3");
		final Column t_table2_nom_table3 = table2.getColumnByNames().get("nom_table3");
		// name
		assertEquals("id", t_table2_id.getName());
		assertEquals("nom", t_table2_nom.getName());
		assertEquals("id_table3", t_table2_id_table3.getName());
		assertEquals("nom_table3", t_table2_nom_table3.getName());
		// type
		assertEquals("integer", t_table2_id.getType());
		assertEquals("character varying", t_table2_nom.getType());
		assertEquals("integer", t_table2_id_table3.getType());
		assertEquals("character varying", t_table2_nom_table3.getType());
		// primary key
		assertEquals(1, table2.getPrimaryKey().getColumnNames().size());
		assertEquals("id", table2.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertTrue(t_table2_id.getIsNotNull());
		assertFalse(t_table2_nom.getIsNotNull());
		assertFalse(t_table2_id_table3.getIsNotNull());
		assertFalse(t_table2_nom_table3.getIsNotNull());
		// clé étrangère
		assertEquals(1, table2.getForeignKeys().size());

		final ForeignKey fk_table2_table3 = table2.getForeignKeys().get(0);
		assertEquals("table2", fk_table2_table3.getTableNameOrigin());
		assertEquals("table3", fk_table2_table3.getTableNameTarget());
		assertEquals("id_table3", fk_table2_table3.getColumnNameOrigins().get(0));
		assertEquals("id", fk_table2_table3.getColumnNameTargets().get(0));
		assertEquals("nom_table3", fk_table2_table3.getColumnNameOrigins().get(1));
		assertEquals("nom", fk_table2_table3.getColumnNameTargets().get(1));

		// Table table3
		System.out.println(table3.getColumnByNames().keySet());
		assertEquals(2, table3.getColumnByNames().keySet().size());
		final Column t_table3_id = table3.getColumnByNames().get("id");
		final Column t_table3_nom = table3.getColumnByNames().get("nom");
		// name
		assertEquals("id", t_table3_id.getName());
		assertEquals("nom", t_table3_nom.getName());
		// type
		assertEquals("integer", t_table3_id.getType());
		assertEquals("character varying", t_table3_nom.getType());
		// primary key
		assertEquals(1, table3.getPrimaryKey().getColumnNames().size());
		assertEquals("id", table3.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertTrue(t_table3_id.getIsNotNull());
		assertFalse(t_table3_nom.getIsNotNull());
		// clé étrangère
		assertEquals(0, table3.getForeignKeys().size());

		// Table article_keyword
		System.out.println(article_keyword.getColumnByNames().keySet());
		assertEquals(3, article_keyword.getColumnByNames().keySet().size());
		final Column t1_id = article_keyword.getColumnByNames().get("id");
		final Column t1_article_id = article_keyword.getColumnByNames().get("article_id");
		final Column t1_keyword_id = article_keyword.getColumnByNames().get("keyword_id");
		// name
		assertEquals("id", t1_id.getName());
		assertEquals("article_id", t1_article_id.getName());
		assertEquals("keyword_id", t1_keyword_id.getName());
		// type
		assertEquals("INTEGER", t1_id.getType());
		assertEquals("INTEGER", t1_article_id.getType());
		assertEquals("INTEGER", t1_keyword_id.getType());
		// primary key
		assertEquals(1, article_keyword.getPrimaryKey().getColumnNames().size());
		assertEquals("id", article_keyword.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertFalse(t1_id.getIsNotNull());
		assertFalse(t1_article_id.getIsNotNull());
		assertFalse(t1_keyword_id.getIsNotNull());
		// clé étrangère
		assertEquals(2, article_keyword.getForeignKeys().size());

		final ForeignKey cle_article_id = article_keyword.getForeignKeys().get(0);
		assertEquals("article_keyword", cle_article_id.getTableNameOrigin());
		assertEquals("article", cle_article_id.getTableNameTarget());
		assertEquals("article_id", cle_article_id.getColumnNameOrigins().get(0));
		assertEquals("id", cle_article_id.getColumnNameTargets().get(0));

		final ForeignKey cle_keyword_id = article_keyword.getForeignKeys().get(1);
		assertEquals("article_keyword", cle_keyword_id.getTableNameOrigin());
		assertEquals("keyword", cle_keyword_id.getTableNameTarget());
		assertEquals("keyword_id", cle_keyword_id.getColumnNameOrigins().get(0));
		assertEquals("id", cle_keyword_id.getColumnNameTargets().get(0));

		// Table article
		System.out.println(article.getColumnByNames().keySet());
		assertEquals(3, article.getColumnByNames().keySet().size());
		final Column t_article_id = article.getColumnByNames().get("id");
		final Column t_article_name = article.getColumnByNames().get("name");
		final Column t_article_url = article.getColumnByNames().get("url");
		// name
		assertEquals("id", t_article_id.getName());
		assertEquals("name", t_article_name.getName());
		assertEquals("url", t_article_url.getName());
		// type
		assertEquals("INTEGER", t_article_id.getType());
		assertEquals("varchar", t_article_name.getType());
		assertEquals("varchar", t_article_url.getType());
		// primary key
		assertEquals(1, article.getPrimaryKey().getColumnNames().size());
		assertEquals("id", article.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertFalse(t_article_id.getIsNotNull());
		assertFalse(t_article_name.getIsNotNull());
		assertFalse(t_article_url.getIsNotNull());
		// clé étrangère
		assertEquals(0, article.getForeignKeys().size());

		// Table users
		System.out.println(users.getColumnByNames().keySet());
		assertEquals(4, users.getColumnByNames().keySet().size());
		final Column t1_email = users.getColumnByNames().get("email");
		final Column t1_name = users.getColumnByNames().get("name");
		final Column t1_password = users.getColumnByNames().get("password");
		final Column t1_isadmin = users.getColumnByNames().get("isadmin");
		// name
		assertEquals("email", t1_email.getName());
		assertEquals("name", t1_name.getName());
		assertEquals("password", t1_password.getName());
		assertEquals("isadmin", t1_isadmin.getName());
		// type
		assertEquals("VARCHAR", t1_email.getType());
		assertEquals("VARCHAR", t1_name.getType());
		assertEquals("VARCHAR", t1_password.getType());
		assertEquals("BOOLEAN", t1_isadmin.getType());
		// primary key
		assertEquals(1, users.getPrimaryKey().getColumnNames().size());
		assertEquals("email", users.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertFalse(t1_email.getIsNotNull());
		assertFalse(t1_name.getIsNotNull());
		assertFalse(t1_password.getIsNotNull());
		assertFalse(t1_isadmin.getIsNotNull());
		// clé étrangère
		assertEquals(0, users.getForeignKeys().size());
	}

	@Test
	public void testGetDatabase_postgres_backup() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/postgres.backup");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		assertEquals(5, database.getTables().size());

		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(14, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// Database schema
		assertEquals(5, database.getTables().size());

		final Table Company = database.getTables().get(0);
		assertEquals("company", Company.getName());
		final Table Contact = database.getTables().get(1);
		assertEquals("contact", Contact.getName());
		final Table table1 = database.getTables().get(2);
		assertEquals("table1", table1.getName());
		final Table table2 = database.getTables().get(3);
		assertEquals("table2", table2.getName());
		final Table table3 = database.getTables().get(4);
		assertEquals("table3", table3.getName());

		// Table Company
		System.out.println(Contact.getColumnByNames().keySet());
		assertEquals(6, Contact.getColumnByNames().keySet().size());
		final Column t1_id = Contact.getColumnByNames().get("id");
		final Column t1_email = Contact.getColumnByNames().get("email");
		final Column t1_age = Contact.getColumnByNames().get("age");
		final Column t1_name = Contact.getColumnByNames().get("name");
		final Column t1_firstname = Contact.getColumnByNames().get("firstname");
		final Column t1_company_id = Contact.getColumnByNames().get("company_id");
		// name
		assertEquals("id", t1_id.getName());
		assertEquals("email", t1_email.getName());
		assertEquals("age", t1_age.getName());
		assertEquals("name", t1_name.getName());
		assertEquals("firstname", t1_firstname.getName());
		assertEquals("company_id", t1_company_id.getName());
		// type
		assertEquals("character varying", t1_id.getType());
		assertEquals("character varying", t1_email.getType());
		assertEquals("integer", t1_age.getType());
		assertEquals("character varying", t1_name.getType());
		assertEquals("character varying", t1_firstname.getType());
		assertEquals("integer", t1_company_id.getType());
		// primary key
		assertEquals(1, Contact.getPrimaryKey().getColumnNames().size());
		assertEquals("id", Contact.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertTrue(t1_id.getIsNotNull());
		assertTrue(t1_email.getIsNotNull());
		assertFalse(t1_age.getIsNotNull());
		assertFalse(t1_name.getIsNotNull());
		assertFalse(t1_firstname.getIsNotNull());
		assertFalse(t1_company_id.getIsNotNull());
		// clé étrangère
		assertEquals(1, Contact.getForeignKeys().size());
		final ForeignKey cle_company_id = Contact.getForeignKeys().get(0);
		assertEquals("contact", cle_company_id.getTableNameOrigin());
		assertEquals("company", cle_company_id.getTableNameTarget());
		assertEquals("company_id", cle_company_id.getColumnNameOrigins().get(0));
		assertEquals("id", cle_company_id.getColumnNameTargets().get(0));

		// table2
		// clé étrangère
		final ForeignKey fk_table2_table3 = table2.getForeignKeys().get(0);
		assertEquals("table2", fk_table2_table3.getTableNameOrigin());
		assertEquals("table3", fk_table2_table3.getTableNameTarget());
		assertEquals("id_table3", fk_table2_table3.getColumnNameOrigins().get(0));
		assertEquals("id", fk_table2_table3.getColumnNameTargets().get(0));
		assertEquals("nom_table3", fk_table2_table3.getColumnNameOrigins().get(1));
		assertEquals("nom", fk_table2_table3.getColumnNameTargets().get(1));
	}

	@Test
	public void testGetDatabase_postgres_pg_dump() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/postgres_pg_dump.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		assertEquals(5, database.getTables().size());

		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(14, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// Database schema
		assertEquals(5, database.getTables().size());

		final Table Company = database.getTables().get(0);
		assertEquals("company", Company.getName());
		final Table Contact = database.getTables().get(1);
		assertEquals("contact", Contact.getName());
		final Table table1 = database.getTables().get(2);
		assertEquals("table1", table1.getName());
		final Table table2 = database.getTables().get(3);
		assertEquals("table2", table2.getName());
		final Table table3 = database.getTables().get(4);
		assertEquals("table3", table3.getName());

		// Table Company
		System.out.println(Contact.getColumnByNames().keySet());
		assertEquals(6, Contact.getColumnByNames().keySet().size());
		final Column t1_id = Contact.getColumnByNames().get("id");
		final Column t1_email = Contact.getColumnByNames().get("email");
		final Column t1_age = Contact.getColumnByNames().get("age");
		final Column t1_name = Contact.getColumnByNames().get("name");
		final Column t1_firstname = Contact.getColumnByNames().get("firstname");
		final Column t1_company_id = Contact.getColumnByNames().get("company_id");
		// name
		assertEquals("id", t1_id.getName());
		assertEquals("email", t1_email.getName());
		assertEquals("age", t1_age.getName());
		assertEquals("name", t1_name.getName());
		assertEquals("firstname", t1_firstname.getName());
		assertEquals("company_id", t1_company_id.getName());
		// type
		assertEquals("character varying", t1_id.getType());
		assertEquals("character varying", t1_email.getType());
		assertEquals("integer", t1_age.getType());
		assertEquals("character varying", t1_name.getType());
		assertEquals("character varying", t1_firstname.getType());
		assertEquals("integer", t1_company_id.getType());
		// primary key
		assertEquals(1, Contact.getPrimaryKey().getColumnNames().size());
		assertEquals("id", Contact.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertTrue(t1_id.getIsNotNull());
		assertTrue(t1_email.getIsNotNull());
		assertFalse(t1_age.getIsNotNull());
		assertFalse(t1_name.getIsNotNull());
		assertFalse(t1_firstname.getIsNotNull());
		assertFalse(t1_company_id.getIsNotNull());
		// clé étrangère
		assertEquals(1, Contact.getForeignKeys().size());
		final ForeignKey cle_company_id = Contact.getForeignKeys().get(0);
		assertEquals("contact", cle_company_id.getTableNameOrigin());
		assertEquals("company", cle_company_id.getTableNameTarget());
		assertEquals("company_id", cle_company_id.getColumnNameOrigins().get(0));
		assertEquals("id", cle_company_id.getColumnNameTargets().get(0));

		// table2
		// clé étrangère
		final ForeignKey fk_table2_table3 = table2.getForeignKeys().get(0);
		assertEquals("table2", fk_table2_table3.getTableNameOrigin());
		assertEquals("table3", fk_table2_table3.getTableNameTarget());
		assertEquals("id_table3", fk_table2_table3.getColumnNameOrigins().get(0));
		assertEquals("id", fk_table2_table3.getColumnNameTargets().get(0));
		assertEquals("nom_table3", fk_table2_table3.getColumnNameOrigins().get(1));
		assertEquals("nom", fk_table2_table3.getColumnNameTargets().get(1));
	}

	@Test
	public void testGetDatabase_oracle1() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/oracle1.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		assertEquals(4, database.getTables().size());

		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(10, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// Database schema
		assertEquals(4, database.getTables().size());

		final Table proposal = database.getTables().get(0);
		assertEquals("PROPOSAL", proposal.getName());
		final Table contract = database.getTables().get(1);
		assertEquals("CONTRACT", contract.getName());
		final Table category = database.getTables().get(2);
		assertEquals("CATEGORY", category.getName());
		final Table typeContract = database.getTables().get(3);
		assertEquals("TYPE_CONTRACT", typeContract.getName());

		// Table proposal
		System.out.println(proposal.getColumnByNames().keySet());
		assertEquals(12, proposal.getColumnByNames().keySet().size());
		final Column t1_ID_PROPOSAL = proposal.getColumnByNames().get("ID_PROPOSAL");
		final Column t1_ID_PRODUCT = proposal.getColumnByNames().get("ID_PRODUCT");
		final Column t1_CREATE_DATE = proposal.getColumnByNames().get("CREATE_DATE");
		final Column t1_COD_PROPOSAL_TYPE = proposal.getColumnByNames().get("COD_PROPOSAL_TYPE");
		final Column t1_LIBPROD = proposal.getColumnByNames().get("LIBPROD");
		final Column t1_FLAG_EXP = proposal.getColumnByNames().get("FLAG_EXP");
		// name
		assertEquals("ID_PROPOSAL", t1_ID_PROPOSAL.getName());
		assertEquals("ID_PRODUCT", t1_ID_PRODUCT.getName());
		assertEquals("CREATE_DATE", t1_CREATE_DATE.getName());
		assertEquals("COD_PROPOSAL_TYPE", t1_COD_PROPOSAL_TYPE.getName());
		assertEquals("LIBPROD", t1_LIBPROD.getName());
		assertEquals("FLAG_EXP", t1_FLAG_EXP.getName());
		// type
		assertEquals("NUMBER", t1_ID_PROPOSAL.getType());
		assertEquals("VARCHAR2", t1_ID_PRODUCT.getType());
		assertEquals("DATE", t1_CREATE_DATE.getType());
		assertEquals("VARCHAR2", t1_COD_PROPOSAL_TYPE.getType());
		assertEquals("VARCHAR2", t1_LIBPROD.getType());
		assertEquals("NUMBER", t1_FLAG_EXP.getType());
		// primary key
		assertEquals(0, proposal.getPrimaryKey().getColumnNames().size());
		// not null
		assertFalse(t1_ID_PROPOSAL.getIsNotNull());
		assertFalse(t1_ID_PRODUCT.getIsNotNull());
		assertFalse(t1_CREATE_DATE.getIsNotNull());
		assertFalse(t1_COD_PROPOSAL_TYPE.getIsNotNull());
		assertFalse(t1_LIBPROD.getIsNotNull());
		assertFalse(t1_FLAG_EXP.getIsNotNull());
		// clé étrangère
		assertEquals(0, proposal.getForeignKeys().size());

		// Table contract
		System.out.println(contract.getColumnByNames().keySet());
		assertEquals(13, contract.getColumnByNames().keySet().size());
		final Column t2_ID_CONTRACT = contract.getColumnByNames().get("ID_CONTRACT");
		final Column t2_NB_DURATION_IN_MONTHS = contract.getColumnByNames().get("NB_DURATION_IN_MONTHS");
		final Column t2_TX = contract.getColumnByNames().get("TX");
		final Column t2_MT_ORIG = contract.getColumnByNames().get("MT_ORIG");
		final Column t2_MT_MONTH = contract.getColumnByNames().get("MT_MONTH");
		final Column t2_NB_DURATION_MONTH = contract.getColumnByNames().get("NB_DURATION_MONTH");
		// name
		assertEquals("ID_CONTRACT", t2_ID_CONTRACT.getName());
		assertEquals("NB_DURATION_IN_MONTHS", t2_NB_DURATION_IN_MONTHS.getName());
		assertEquals("TX", t2_TX.getName());
		assertEquals("MT_ORIG", t2_MT_ORIG.getName());
		assertEquals("MT_MONTH", t2_MT_MONTH.getName());
		assertEquals("NB_DURATION_MONTH", t2_NB_DURATION_MONTH.getName());
		// type
		assertEquals("NUMBER", t2_ID_CONTRACT.getType());
		assertEquals("NUMBER", t2_NB_DURATION_IN_MONTHS.getType());
		assertEquals("NUMBER", t2_TX.getType());
		assertEquals("NUMBER", t2_MT_ORIG.getType());
		assertEquals("NUMBER", t2_MT_MONTH.getType());
		assertEquals("NUMBER", t2_NB_DURATION_MONTH.getType());
		// primary key
		assertEquals(1, contract.getPrimaryKey().getColumnNames().size());
		assertEquals("ID_CONTRACT", contract.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertFalse(t2_ID_CONTRACT.getIsNotNull());
		assertFalse(t2_NB_DURATION_IN_MONTHS.getIsNotNull());
		assertFalse(t2_TX.getIsNotNull());
		assertFalse(t2_MT_ORIG.getIsNotNull());
		assertFalse(t2_MT_MONTH.getIsNotNull());
		assertFalse(t2_NB_DURATION_MONTH.getIsNotNull());
		// clé étrangère
		assertEquals(3, contract.getForeignKeys().size());
	}

	@Test
	public void testGetDatabase_oracle2() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/oracle2.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		assertEquals(3, database.getTables().size());

		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(3, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// Database schema
		assertEquals(3, database.getTables().size());

		final Table employees_demo = database.getTables().get(0);
		assertEquals("employees_demo", employees_demo.getName());
		final Table films = database.getTables().get(1);
		assertEquals("films", films.getName());
		final Table distributors = database.getTables().get(2);
		assertEquals("distributors", distributors.getName());

		// Table employees_demo
		System.out.println(employees_demo.getColumnByNames().keySet());
		assertEquals(12, employees_demo.getColumnByNames().keySet().size());
		final Column t1_employee_id = employees_demo.getColumnByNames().get("employee_id");
		final Column t1_first_name = employees_demo.getColumnByNames().get("first_name");
		final Column t1_last_name = employees_demo.getColumnByNames().get("last_name");
		final Column t1_email = employees_demo.getColumnByNames().get("email");
		final Column t1_phone_number = employees_demo.getColumnByNames().get("phone_number");
		final Column t1_hire_date = employees_demo.getColumnByNames().get("hire_date");
		final Column t1_job_id = employees_demo.getColumnByNames().get("job_id");
		final Column t1_salary = employees_demo.getColumnByNames().get("salary");
		final Column t1_commission_pct = employees_demo.getColumnByNames().get("commission_pct");
		final Column t1_manager_id = employees_demo.getColumnByNames().get("manager_id");
		final Column t1_department_id = employees_demo.getColumnByNames().get("department_id");
		final Column t1_dn = employees_demo.getColumnByNames().get("dn");
		// name
		assertEquals("employee_id", t1_employee_id.getName());
		assertEquals("first_name", t1_first_name.getName());
		assertEquals("last_name", t1_last_name.getName());
		assertEquals("email", t1_email.getName());
		assertEquals("phone_number", t1_phone_number.getName());
		assertEquals("hire_date", t1_hire_date.getName());
		assertEquals("job_id", t1_job_id.getName());
		assertEquals("salary", t1_salary.getName());
		assertEquals("commission_pct", t1_commission_pct.getName());
		assertEquals("manager_id", t1_manager_id.getName());
		assertEquals("department_id", t1_department_id.getName());
		assertEquals("dn", t1_dn.getName());
		// type
		assertEquals("NUMBER", t1_employee_id.getType());
		assertEquals("VARCHAR2", t1_first_name.getType());
		assertEquals("VARCHAR2", t1_last_name.getType());
		assertEquals("VARCHAR2", t1_email.getType());
		assertEquals("VARCHAR2", t1_phone_number.getType());
		assertEquals("DATE", t1_hire_date.getType());
		assertEquals("VARCHAR2", t1_job_id.getType());
		assertEquals("NUMBER", t1_salary.getType());
		assertEquals("NUMBER", t1_commission_pct.getType());
		assertEquals("NUMBER", t1_manager_id.getType());
		assertEquals("NUMBER", t1_department_id.getType());
		assertEquals("VARCHAR2", t1_dn.getType());
		// primary key
		assertEquals(0, employees_demo.getPrimaryKey().getColumnNames().size());
		// not null
		assertFalse(t1_employee_id.getIsNotNull());
		assertFalse(t1_first_name.getIsNotNull());
		assertTrue(t1_last_name.getIsNotNull());
		assertTrue(t1_email.getIsNotNull());
		assertFalse(t1_phone_number.getIsNotNull());
		assertTrue(t1_hire_date.getIsNotNull());
		assertTrue(t1_job_id.getIsNotNull());
		assertTrue(t1_salary.getIsNotNull());
		assertFalse(t1_commission_pct.getIsNotNull());
		assertFalse(t1_manager_id.getIsNotNull());
		assertFalse(t1_department_id.getIsNotNull());
		assertFalse(t1_dn.getIsNotNull());
		// clé étrangère
		assertEquals(0, employees_demo.getForeignKeys().size());

		// Table films
		System.out.println(films.getColumnByNames().keySet());
		assertEquals(6, films.getColumnByNames().keySet().size());
		final Column t_films_code = films.getColumnByNames().get("code");
		final Column t_films_title = films.getColumnByNames().get("title");
		final Column t_films_did = films.getColumnByNames().get("did");
		final Column t_films_date_prod = films.getColumnByNames().get("date_prod");
		final Column t_films_kind = films.getColumnByNames().get("kind");
		final Column t_films_len = films.getColumnByNames().get("len");
		// name
		assertEquals("code", t_films_code.getName());
		assertEquals("title", t_films_title.getName());
		assertEquals("did", t_films_did.getName());
		assertEquals("date_prod", t_films_date_prod.getName());
		assertEquals("kind", t_films_kind.getName());
		assertEquals("len", t_films_len.getName());
		// type
		assertEquals("char", t_films_code.getType());
		assertEquals("varchar", t_films_title.getType());
		assertEquals("integer", t_films_did.getType());
		assertEquals("date", t_films_date_prod.getType());
		assertEquals("varchar", t_films_kind.getType());
		assertEquals("interval hour to minute", t_films_len.getType());
		// primary key
		assertEquals(1, films.getPrimaryKey().getColumnNames().size());
		assertEquals("code", films.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertFalse(t_films_code.getIsNotNull());
		assertTrue(t_films_title.getIsNotNull());
		assertTrue(t_films_did.getIsNotNull());
		assertFalse(t_films_date_prod.getIsNotNull());
		assertFalse(t_films_kind.getIsNotNull());
		assertFalse(t_films_len.getIsNotNull());
		// clé étrangère
		assertEquals(0, films.getForeignKeys().size());

		// Table distributors
		System.out.println(distributors.getColumnByNames().keySet());
		assertEquals(2, distributors.getColumnByNames().keySet().size());
		final Column t1_did = distributors.getColumnByNames().get("did");
		final Column t1_name = distributors.getColumnByNames().get("name");
		// name
		assertEquals("did", t1_did.getName());
		assertEquals("name", t1_name.getName());
		// type
		assertEquals("integer", t1_did.getType());
		assertEquals("varchar", t1_name.getType());
		// primary key
		assertEquals(1, distributors.getPrimaryKey().getColumnNames().size());
		assertEquals("did", distributors.getPrimaryKey().getColumnNames().get(0));
		// not null
		assertFalse(t1_did.getIsNotNull());
		assertTrue(t1_name.getIsNotNull());
		// clé étrangère
		assertEquals(0, distributors.getForeignKeys().size());
	}

	@Test
	public void testGetDatabase_oracle_sqldeveloper() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/oracle_sqldeveloper.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final Database database = sqlImport.getDatabase(sqlContent);

		System.out.println(reportManager.toString(report));

		// Then
		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(14, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		// Database schema
		assertEquals(5, database.getTables().size());

		final Table table1 = database.getTables().get(0);
		assertEquals("TABLE1", table1.getName());
		final Table table2 = database.getTables().get(1);
		assertEquals("TABLE2", table2.getName());
		final Table table3 = database.getTables().get(2);
		assertEquals("TABLE3", table3.getName());
		final Table Company = database.getTables().get(3);
		assertEquals("COMPANY", Company.getName());
		final Table Contact = database.getTables().get(4);
		assertEquals("CONTACT", Contact.getName());

		// Table Company
		System.out.println(Contact.getColumnByNames().keySet());
		assertEquals(6, Contact.getColumnByNames().keySet().size());
		final Column t1_id = Contact.getColumnByNames().get("ID");
		final Column t1_email = Contact.getColumnByNames().get("EMAIL");
		final Column t1_age = Contact.getColumnByNames().get("AGE");
		final Column t1_name = Contact.getColumnByNames().get("NAME");
		final Column t1_firstname = Contact.getColumnByNames().get("FIRSTNAME");
		final Column t1_company_id = Contact.getColumnByNames().get("COMPANY_ID");
		// name
		assertEquals("ID", t1_id.getName());
		assertEquals("EMAIL", t1_email.getName());
		assertEquals("AGE", t1_age.getName());
		assertEquals("NAME", t1_name.getName());
		assertEquals("FIRSTNAME", t1_firstname.getName());
		assertEquals("COMPANY_ID", t1_company_id.getName());
		// type
		assertEquals("VARCHAR2", t1_id.getType());
		assertEquals("VARCHAR2", t1_email.getType());
		assertEquals("NUMBER", t1_age.getType());
		assertEquals("VARCHAR2", t1_name.getType());
		assertEquals("VARCHAR2", t1_firstname.getType());
		assertEquals("NUMBER", t1_company_id.getType());
		// primary key
		assertEquals(1, Contact.getPrimaryKey().getColumnNames().size());
		assertEquals("ID", Contact.getPrimaryKey().getColumnNames().get(0));
		// not null
		/*
		assertTrue(t1_id.getIsNotNull());
		assertTrue(t1_email.getIsNotNull());
		assertFalse(t1_age.getIsNotNull());
		assertFalse(t1_name.getIsNotNull());
		assertFalse(t1_firstname.getIsNotNull());
		assertFalse(t1_company_id.getIsNotNull());
		 */
		// clé étrangère
		assertEquals(1, Contact.getForeignKeys().size());
		final ForeignKey cle_company_id = Contact.getForeignKeys().get(0);
		assertEquals("CONTACT", cle_company_id.getTableNameOrigin());
		assertEquals("COMPANY", cle_company_id.getTableNameTarget());
		assertEquals("COMPANY_ID", cle_company_id.getColumnNameOrigins().get(0));
		assertEquals("ID", cle_company_id.getColumnNameTargets().get(0));

		// table2
		// clé étrangère
		final ForeignKey fk_table2_table3 = table2.getForeignKeys().get(0);
		assertEquals("TABLE2", fk_table2_table3.getTableNameOrigin());
		assertEquals("TABLE3", fk_table2_table3.getTableNameTarget());
		assertEquals("ID_TABLE3", fk_table2_table3.getColumnNameOrigins().get(0));
		assertEquals("ID", fk_table2_table3.getColumnNameTargets().get(0));
		assertEquals("NOM_TABLE3", fk_table2_table3.getColumnNameOrigins().get(1));
		assertEquals("NOM", fk_table2_table3.getColumnNameTargets().get(1));
	}

}
