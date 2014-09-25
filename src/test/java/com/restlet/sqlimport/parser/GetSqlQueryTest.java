package com.restlet.sqlimport.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLineStatus;
import com.restlet.sqlimport.util.Util;


public class GetSqlQueryTest {

	private Report report = new Report();
	private GetSqlQuery getSqlQuery = new GetSqlQuery(report);
	private Util util = new Util();

	@Test
	public void testRead_standard() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/standard.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final List<String> lines = getSqlQuery.getSqlQuerys(sqlContent);

		// Then
		assertEquals(4, lines.size());

		assertEquals(ReportLineStatus.TO_PARSE, report.getReportLineForQuery(lines.get(0)).getReportLineStatus());
		assertEquals(ReportLineStatus.TO_PARSE, report.getReportLineForQuery(lines.get(1)).getReportLineStatus());
		assertEquals(ReportLineStatus.TO_PARSE, report.getReportLineForQuery(lines.get(2)).getReportLineStatus());

	}

	@Test
	public void testRead_mysql() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/mysql1.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final List<String> lines = getSqlQuery.getSqlQuerys(sqlContent);

		// Then
		assertEquals(6, lines.size());
	}

	@Test
	public void testRead_postgres() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/postgres.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final List<String> lines = getSqlQuery.getSqlQuerys(sqlContent);

		// Then
		assertEquals(9, lines.size());
	}

	@Test
	public void testRead_oracle1() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/oracle1.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final List<String> lines = getSqlQuery.getSqlQuerys(sqlContent);

		// Then
		assertEquals(10, lines.size());
	}

	@Test
	public void testRead_oracle2() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/oracle2.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final List<String> lines = getSqlQuery.getSqlQuerys(sqlContent);

		// Then
		assertEquals(3, lines.size());
	}


}
