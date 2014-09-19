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
 * Write database schema in a file.
 */
public class ExportToText {

	public void write(final Database database, final OutputStream os) {

		final List<String> lines = getLines(database);

		final Util util = new Util();
		util.write(lines, os);
	}

	public List<String> getLines(final Database database) {
		final List<String> lines = new ArrayList<String>();

		lines.add("Database schema : ");

		for(final Table table : database.getTables()) {
			lines.add("---");
			lines.add("Table : "+table.getName());
			for(final String columnName : table.getColumnByNames().keySet()) {
				final Column column = table.getColumnByNames().get(columnName);
				lines.add(" - Column : "+column.getName());
				lines.add("   - type : "+column.getType());
			}
			if(table.getPrimaryKey() != null) {
				lines.add(" - Primary key : "+table.getPrimaryKey().getName());
			} else {
				lines.add(" - Primary key : none");
			}
			for(final ForeignKey foreignKey : table.getForeignKeys()) {
				String txt = " - Foreign key : ";
				txt += foreignKey.getTableNameOrigin()+"."+foreignKey.getColumnNameOrigins().get(0);
				txt += " -> ";
				txt += foreignKey.getTableNameTarget()+"."+foreignKey.getColumnNameTargets().get(0);
				lines.add(txt);
			}
		}

		return lines;
	}

}
