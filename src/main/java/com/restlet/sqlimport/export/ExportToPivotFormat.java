package com.restlet.sqlimport.export;

import java.io.OutputStream;

import org.json.JSONStringer;

import com.restlet.sqlimport.model.Column;
import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.model.ForeignKey;
import com.restlet.sqlimport.model.Table;
import com.restlet.sqlimport.util.Util;

/**
 * Write database schema in a file in the pivot format.
 */
public class ExportToPivotFormat {

	/**
	 * Write database in the file in the pivot format.
	 * @param database Database
	 * @param os Output stream
	 */
	public void write(final Database database, final OutputStream os) {

		final String content = getLines(database);

		final Util util = new Util();
		util.write(content, os);
	}

	/**
	 * Get lines to write in the output file.
	 * @param database Database schema
	 * @return lines
	 */
	public String getLines(final Database database) {

		final JSONStringer jsonStringer = new JSONStringer();

		// Convert SQL types to Entity store types
		final TypeConverter typeConverter = new TypeConverter();
		typeConverter.convertTypeFromSQLToEntityStore(database);

		jsonStringer.array();
		for(final Table table : database.getTables()) {
			jsonStringer.object();
			jsonStringer.key("name").value(table.getName());
			jsonStringer.key("fields").array();
			for(final String columnName : table.getColumnByNames().keySet()) {
				final Column column = table.getColumnByNames().get(columnName);
				jsonStringer.object();
				jsonStringer.key("name").value(column.getName());
				// Foreign key
				final ForeignKey foreignKey = table.getForeignKeyForColumnNameOrigin(column);
				if(foreignKey == null) {
					// converted type
					jsonStringer.key("type").value(column.getConvertedType());
				} else {
					// foreign key : type is in the name of the foreign table
					jsonStringer.key("type").value(foreignKey.getTableNameTarget());
				}
				jsonStringer.key("minOccurs").value(1);
				jsonStringer.key("maxOccurs").value(1);
				if(column.getIsNotNull()) {
					jsonStringer.key("nullable").value(false);
				} else {
					jsonStringer.key("nullable").value(true);
				}
				jsonStringer.endObject();
			}
			jsonStringer.endArray();
			jsonStringer.endObject();
		}
		jsonStringer.endArray();

		return jsonStringer.toString();
	}

}
