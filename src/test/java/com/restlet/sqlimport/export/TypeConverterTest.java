package com.restlet.sqlimport.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class TypeConverterTest {

	@Test
	public void testConvertTypeFromSQLToEntityStore() {
		final TypeConverter typeConverter = new TypeConverter();

		// null
		final String sqlType = null;
		assertNull(typeConverter.convertTypeFromSQLToEntityStore(sqlType));

		// unknown
		assertEquals("String", typeConverter.convertTypeFromSQLToEntityStore("UNKOWN"));

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
	}

}
