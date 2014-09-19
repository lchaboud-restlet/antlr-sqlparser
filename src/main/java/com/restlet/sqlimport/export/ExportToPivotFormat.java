package com.restlet.sqlimport.export;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

		final List<String> lines = getLines(database);

		final Util util = new Util();
		util.write(lines, os);
	}

	/**
	 * Get lines to write in the output file.
	 * @param database Database schema
	 * @return lines
	 */
	public List<String> getLines(final Database database) {

		// Convert SQL types to Entity store types
		final TypeConverter typeConverter = new TypeConverter();
		typeConverter.convertTypeFromSQLToEntityStore(database);

		// Output lines
		final List<String> lines = new ArrayList<String>();

		boolean isFirstTable = true;
		lines.add("[");
		for(final Table table : database.getTables()) {
			if(isFirstTable) {
				isFirstTable = false;
			} else {
				// Add comma
				final String lastLine = lines.get(lines.size()-1);
				lines.set(lines.size()-1, lastLine+",");
			}
			lines.add("  { \"name\": \""+table.getName()+"\",");
			lines.add("    \"fields\": [");
			boolean isFirstColumn = true;
			for(final String columnName : table.getColumnByNames().keySet()) {
				if(isFirstColumn) {
					isFirstColumn = false;
				} else {
					// Add comma
					final String lastLine = lines.get(lines.size()-1);
					lines.set(lines.size()-1, lastLine+",");
				}
				final Column column = table.getColumnByNames().get(columnName);
				final StringBuffer txt = new StringBuffer();
				txt.append("      {");
				txt.append(" \"name\": \""+column.getName()+"\"");
				// Foreign key
				final ForeignKey foreignKey = table.getForeignKeyForColumnNameOrigin(column);
				if(foreignKey == null) {
					// converted type
					txt.append(", \"type\": \""+column.getConvertedType()+"\"");
				} else {
					// foreign key : type is in the name of the foreign table
					txt.append(", \"type\": \""+foreignKey.getTableNameTarget()+"\"");
				}
				txt.append(", \"minOccurs\": ");
				if(column.getIsNotNull()) {
					txt.append("1");
				} else {
					txt.append("0");
				}
				txt.append(", \"maxOccurs\": 1");
				txt.append(" }");
				lines.add(txt.toString());
			}
			lines.add("    ]");
			lines.add("  }");
		}
		lines.add("]");

		return lines;
	}

}
