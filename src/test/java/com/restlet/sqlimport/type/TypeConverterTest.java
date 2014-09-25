package com.restlet.sqlimport.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLineStatus;
import com.restlet.sqlimport.type.TypeConverter;


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
		assertEquals(ReportLineStatus.UNKNOWN_SQL_TYPE, report.getReportLines().get(0).getReportLineStatus());
		assertEquals("UNKNOWN", report.getReportLines().get(0).getMessage());

		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" bfile "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" bigint "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" bigserial "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" binary "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" binary_double "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" binary_float "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" bit "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" bit varying "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" blob "));
		assertEquals("Boolean", typeConverter.convertTypeFromSQLToEntityStore(" bool "));
		assertEquals("Boolean", typeConverter.convertTypeFromSQLToEntityStore(" boolean "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" bytea "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" char "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" character "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" character varying "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" clob "));
		assertEquals("Date", typeConverter.convertTypeFromSQLToEntityStore(" date "));
		assertEquals("Date", typeConverter.convertTypeFromSQLToEntityStore(" datetime "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" dec "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" decimal "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" double "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" double precision "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" enum "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" fixed "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" float "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" int "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" integer "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" interval "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" interval day [day_precision] to second [fractional seconds] "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" interval year [year_precision] to month "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" long "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" long raw "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" longblob "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" longtext "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" mediumblob "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" mediumint "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" mediumtext "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" money "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" national character "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" national character varying "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" nchar "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" nclob "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" number "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" numeric "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" nvarchar "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" nvarchar2 "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" raw "));
		assertEquals("Float", typeConverter.convertTypeFromSQLToEntityStore(" real "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" rowid "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" serial "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" set "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" smallint "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" smallserial "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" text "));
		assertEquals("Date", typeConverter.convertTypeFromSQLToEntityStore(" time "));
		assertEquals("Date", typeConverter.convertTypeFromSQLToEntityStore(" time with time zone "));
		assertEquals("Date", typeConverter.convertTypeFromSQLToEntityStore(" timestamp "));
		assertEquals("Date", typeConverter.convertTypeFromSQLToEntityStore(" timestamp with time zone "));
		assertEquals("Date", typeConverter.convertTypeFromSQLToEntityStore(" timestamptz "));
		assertEquals("Date", typeConverter.convertTypeFromSQLToEntityStore(" timetz "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" tinyblob "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" tinyint "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" tinytext "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" urowid "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" uuid "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" varbinary "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" varchar "));
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore(" varchar2 "));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore(" year "));

		assertEquals(1, report.getReportLines().size());
	}

}
