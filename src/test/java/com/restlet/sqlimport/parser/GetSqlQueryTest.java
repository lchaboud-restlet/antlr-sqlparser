package com.restlet.sqlimport.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportStatus;
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

		// When
		final List<String> lines = getSqlQuery.getSqlQuerys(in);

		// Then
		assertEquals(3, lines.size());

		assertEquals(ReportStatus.TO_PARSE, report.getReportLineByQuerys().get(lines.get(0)).getReportStatus());
		assertEquals(ReportStatus.TO_PARSE, report.getReportLineByQuerys().get(lines.get(1)).getReportStatus());
		assertEquals(ReportStatus.TO_PARSE, report.getReportLineByQuerys().get(lines.get(2)).getReportStatus());

	}


}
