package com.restlet.sqlimport.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportStatus;


public class TypeConverterTest {

	@Test
	public void testConvertTypeFromSQLToEntityStore() {
		final Report report = new Report();
		final TypeConverter typeConverter = new TypeConverter(report);

		// null
		final String sqlType = null;
		assertNull(typeConverter.convertTypeFromSQLToEntityStore(sqlType));

		// unknown
		assertEquals(0, report.getReportLines().size());
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore("UNKNOWN"));
		assertEquals(1, report.getReportLines().size());
		assertEquals(ReportStatus.UNKNOWN_SQL_TYPE, report.getReportLines().get(0).getReportStatus());
		assertEquals("UNKNOWN", report.getReportLines().get(0).getMessage());

		// String
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore("VARCHAR"));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore("VARCHAR2"));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore("CHAR"));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore("character"));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore("character varying"));

		// Float
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore("NUMBER"));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore("DECIMAL"));

		// Integer
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore("INTEGER"));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore("INT"));

		// Boolean
		assertEquals("Boolean", typeConverter.convertTypeFromSQLToEntityStore("BOOLEAN"));
		assertEquals("Boolean", typeConverter.convertTypeFromSQLToEntityStore("BOOL"));

		assertEquals(1, report.getReportLines().size());
	}

}
