package com.restlet.sqlimport;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLineStatus;
import com.restlet.sqlimport.report.ReportStatus;
import com.restlet.sqlimport.util.Util;

/**
 * Test : SQL import.
 */
public class MainProcessTest {

	private MainProcess mainProcess = new MainProcess();
	private Util util = new Util();

	@Test
	public void testGetDatabase_nofile() {

		// When
		final String out = mainProcess.process(null);

		// Then
		final Report report = mainProcess.getReport();
		assertEquals(ReportStatus.EMPTY_DATABASE, report.getReportStatus());
		assertNull(out);
	}

	@Test
	public void testGetDatabase_standard() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/standard.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final String out = mainProcess.process(sqlContent);

		// Then
		final Report report = mainProcess.getReport();
		assertEquals(ReportStatus.SUCCESS, report.getReportStatus());
		assertNotNull(out);
	}

	@Test
	public void testGetDatabase_mysql() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/mysql1.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final String out = mainProcess.process(sqlContent);

		final Report report = mainProcess.getReport();

		assertEquals(ReportStatus.SUCCESS, report.getReportStatus());
		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(6, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		assertNotNull(out);
	}

	@Test
	public void testGetDatabase_postgres() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/postgres.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final String out = mainProcess.process(sqlContent);

		final Report report = mainProcess.getReport();

		assertEquals(ReportStatus.SUCCESS, report.getReportStatus());
		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(9, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		assertNotNull(out);
	}

	@Test
	public void testGetDatabase_oracle1() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/oracle1.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final String out = mainProcess.process(sqlContent);

		final Report report = mainProcess.getReport();

		assertEquals(ReportStatus.SUCCESS, report.getReportStatus());
		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(10, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		assertNotNull(out);
	}

	@Test
	public void testGetDatabase_oracle2() throws FileNotFoundException {
		// Given
		final File file = util.getFileByClassPath("/oracle2.sql");
		final InputStream in = new FileInputStream(file);
		final String sqlContent = util.read(in);

		// When
		final String out = mainProcess.process(sqlContent);

		final Report report = mainProcess.getReport();

		assertEquals(ReportStatus.SUCCESS, report.getReportStatus());
		assertEquals(0, report.getReportLinesForStatus(ReportLineStatus.PARSING_ERROR).size());
		assertEquals(3, report.getReportLinesForStatus(ReportLineStatus.PARSED).size());

		assertNotNull(out);
	}

}
