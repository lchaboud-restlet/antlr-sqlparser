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

		// Integer
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore("INTEGER"));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore("INT"));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore("NUMBER"));
		assertEquals("Integer", typeConverter.convertTypeFromSQLToEntityStore("DECIMAL"));

		// Boolean
		assertEquals("Boolean", typeConverter.convertTypeFromSQLToEntityStore("BOOLEAN"));
		assertEquals("Boolean", typeConverter.convertTypeFromSQLToEntityStore("BOOL"));
	}

}
