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

		final String content = getContent(database);

		final Util util = new Util();
		util.write(content, os);
	}

	/**
	 * Get lines to write in the output file.
	 * @param database Database schema
	 * @return lines
	 */
	public String getContent(final Database database) {

		final JSONStringer jsonStringer = new JSONStringer();

		jsonStringer.array();
		for(final Table table : database.getTables()) {
			jsonStringer.object();
			jsonStringer.key("name").value(table.getName());
			jsonStringer.key("pkPolicy").value("user_generated_value");
			jsonStringer.key("fields").array();
			// if there is no primary key or there is a primary key with more than one column in the table,
			// we define a new column named "id" which will be the unique primary key of the entity
			if(table.getPrimaryKey().getColumnNames().size() != 1) {
				jsonStringer.object();
				jsonStringer.key("name").value("id");
				jsonStringer.key("key").value("string");
				jsonStringer.key("isPrimaryKey").value(true);
				jsonStringer.endObject();
			}
			// columns
			for(final String columnName : table.getColumnByNames().keySet()) {
				final Column column = table.getColumnByNames().get(columnName);
				jsonStringer.object();
				jsonStringer.key("name").value(column.getName());
				// Foreign key
				final ForeignKey foreignKey = table.getForeignKeyForColumnNameOrigin(column);
				if(foreignKey == null) {
					// converted type
					if(column.getConvertedType() != null) {
						jsonStringer.key("type").value(column.getConvertedType());
					}
				} else {
					// foreign key : type is in the name of the foreign table
					jsonStringer.key("type").value(foreignKey.getTableNameTarget());
					// number of occurences for the relation
					jsonStringer.key("minOccurs").value(0);
					jsonStringer.key("maxOccurs").value("*");
				}
				// primary key
				boolean isPrimaryKey = false;
				// define only primary key with only one column
				if(table.getPrimaryKey().getColumnNames().size() == 1) {
					for(final String columnNameInPrimaryKey : table.getPrimaryKey().getColumnNames()) {
						if(columnName.equals(columnNameInPrimaryKey)) {
							isPrimaryKey = true;
						}
					}
				}
				if(isPrimaryKey) {
					jsonStringer.key("isPrimaryKey").value(true);
				}
				// nullable
				if(column.getIsNotNull()) {
					jsonStringer.key("nullable").value(false);
				} else {
					jsonStringer.key("nullable").value(true);
				}
				if(column.getDefaultValue() != null) {
					jsonStringer.key("defaultValue").value(column.getDefaultValue());
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
