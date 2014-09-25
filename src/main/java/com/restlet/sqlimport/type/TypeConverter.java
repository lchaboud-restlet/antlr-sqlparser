package com.restlet.sqlimport.type;

import com.restlet.sqlimport.model.sql.Column;
import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.model.sql.Table;
import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLineStatus;

/**
 * Conversion of types from SQL to Entity store
 */
public class TypeConverter {

	public static final String TYPE_STRING = "String";
	public static final String TYPE_FLOAT = "Float";
	public static final String TYPE_INTEGER = "Integer";
	public static final String TYPE_BOOLEAN = "Boolean";
	public static final String TYPE_DATE = "Date";

	private final Report report;

	/**
	 * Constructor.
	 * @param report Report (must not be null)
	 */
	public TypeConverter(final Report report) {
		this.report = report;
	}

	/**
	 * Convert SQL type to Entity store type for all columns of the database.
	 * @param database Database
	 */
	public void convertTypeFromSQLToEntityStore(final Database database) {
		for(final Table table : database.getTables()) {
			for(final Column column : table.getColumnByNames().values()) {
				if(column.getType() == null) {
					column.setConvertedType("");
				} else {
					final String sqlType = column.getType().trim().toUpperCase();
					column.setConvertedType(
							convertTypeFromSQLToEntityStore(sqlType));
				}
			}
		}
	}

	/**
	 * Convert SQL type to Entity store type.
	 * @param sqlType SQL type
	 * @return Entity store type
	 */
	public String convertTypeFromSQLToEntityStore(String sqlType) {
		if(sqlType == null) {
			return null;
		}

		// format sql type for string comparaison
		sqlType = sqlType.trim().toUpperCase();

		if(sqlType.equals("BFILE") || (sqlType.indexOf("BFILE ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("BIGINT") || (sqlType.indexOf("BIGINT ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("BIGSERIAL") || (sqlType.indexOf("BIGSERIAL ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("BINARY") || (sqlType.indexOf("BINARY ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("BINARY_DOUBLE") || (sqlType.indexOf("BINARY_DOUBLE ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("BINARY_FLOAT") || (sqlType.indexOf("BINARY_FLOAT ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("BIT VARYING") || (sqlType.indexOf("BIT VARYING ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("BIT") || (sqlType.indexOf("BIT ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("BLOB") || (sqlType.indexOf("BLOB ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("BOOL") || (sqlType.indexOf("BOOL ") == 0)) {
			return TYPE_BOOLEAN;
		}
		if(sqlType.equals("BOOLEAN") || (sqlType.indexOf("BOOLEAN ") == 0)) {
			return TYPE_BOOLEAN;
		}
		if(sqlType.equals("BYTEA") || (sqlType.indexOf("BYTEA ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("CHAR") || (sqlType.indexOf("CHAR ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("CHARACTER VARYING") || (sqlType.indexOf("CHARACTER VARYING ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("CHARACTER") || (sqlType.indexOf("CHARACTER ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("CLOB") || (sqlType.indexOf("CLOB ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("DATE") || (sqlType.indexOf("DATE ") == 0)) {
			return TYPE_DATE;
		}
		if(sqlType.equals("DATETIME") || (sqlType.indexOf("DATETIME ") == 0)) {
			return TYPE_DATE;
		}
		if(sqlType.equals("DEC") || (sqlType.indexOf("DEC ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("DECIMAL") || (sqlType.indexOf("DECIMAL ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("DOUBLE PRECISION") || (sqlType.indexOf("DOUBLE PRECISION ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("DOUBLE") || (sqlType.indexOf("DOUBLE ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("ENUM") || (sqlType.indexOf("ENUM ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("FIXED") || (sqlType.indexOf("FIXED ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("FLOAT") || (sqlType.indexOf("FLOAT ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("INT") || (sqlType.indexOf("INT ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("INTEGER") || (sqlType.indexOf("INTEGER ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("INTERVAL DAY") || (sqlType.indexOf("INTERVAL DAY ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("INTERVAL YEAR") || (sqlType.indexOf("INTERVAL YEAR ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("INTERVAL") || (sqlType.indexOf("INTERVAL ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("LONG RAW") || (sqlType.indexOf("LONG RAW ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("LONG") || (sqlType.indexOf("LONG ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("LONGBLOB") || (sqlType.indexOf("LONGBLOB ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("LONGTEXT") || (sqlType.indexOf("LONGTEXT ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("MEDIUMBLOB") || (sqlType.indexOf("MEDIUMBLOB ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("MEDIUMINT") || (sqlType.indexOf("MEDIUMINT ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("MEDIUMTEXT") || (sqlType.indexOf("MEDIUMTEXT ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("MONEY") || (sqlType.indexOf("MONEY ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("NATIONAL CHARACTER VARYING") || (sqlType.indexOf("NATIONAL CHARACTER VARYING ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("NATIONAL CHARACTER") || (sqlType.indexOf("NATIONAL CHARACTER ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("NCHAR") || (sqlType.indexOf("NCHAR ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("NCLOB") || (sqlType.indexOf("NCLOB ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("NUMBER") || (sqlType.indexOf("NUMBER ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("NUMERIC") || (sqlType.indexOf("NUMERIC ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("NVARCHAR") || (sqlType.indexOf("NVARCHAR ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("NVARCHAR2") || (sqlType.indexOf("NVARCHAR2 ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("RAW") || (sqlType.indexOf("RAW ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("REAL") || (sqlType.indexOf("REAL ") == 0)) {
			return TYPE_FLOAT;
		}
		if(sqlType.equals("ROWID") || (sqlType.indexOf("ROWID ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("SERIAL") || (sqlType.indexOf("SERIAL ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("SET") || (sqlType.indexOf("SET ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("SMALLINT") || (sqlType.indexOf("SMALLINT ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("SMALLSERIAL") || (sqlType.indexOf("SMALLSERIAL ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("TEXT") || (sqlType.indexOf("TEXT ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("TIME WITH TIME ZONE") || (sqlType.indexOf("TIME WITH TIME ZONE ") == 0)) {
			return TYPE_DATE;
		}
		if(sqlType.equals("TIME") || (sqlType.indexOf("TIME ") == 0)) {
			return TYPE_DATE;
		}
		if(sqlType.equals("TIMESTAMP WITH TIME ZONE") || (sqlType.indexOf("TIMESTAMP WITH TIME ZONE ") == 0)) {
			return TYPE_DATE;
		}
		if(sqlType.equals("TIMESTAMP") || (sqlType.indexOf("TIMESTAMP ") == 0)) {
			return TYPE_DATE;
		}
		if(sqlType.equals("TIMESTAMPTZ") || (sqlType.indexOf("TIMESTAMPTZ ") == 0)) {
			return TYPE_DATE;
		}
		if(sqlType.equals("TIMETZ") || (sqlType.indexOf("TIMETZ ") == 0)) {
			return TYPE_DATE;
		}
		if(sqlType.equals("TINYBLOB") || (sqlType.indexOf("TINYBLOB ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("TINYINT") || (sqlType.indexOf("TINYINT ") == 0)) {
			return TYPE_INTEGER;
		}
		if(sqlType.equals("TINYTEXT") || (sqlType.indexOf("TINYTEXT ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("UROWID") || (sqlType.indexOf("UROWID ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("UUID") || (sqlType.indexOf("UUID ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("VARBINARY") || (sqlType.indexOf("VARBINARY ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("VARCHAR") || (sqlType.indexOf("VARCHAR ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("VARCHAR2") || (sqlType.indexOf("VARCHAR2 ") == 0)) {
			return TYPE_STRING;
		}
		if(sqlType.equals("YEAR") || (sqlType.indexOf("YEAR ") == 0)) {
			return TYPE_INTEGER;
		}

		report.addMessage(ReportLineStatus.UNKNOWN_SQL_TYPE, sqlType);
		return TYPE_STRING;
	}

}
