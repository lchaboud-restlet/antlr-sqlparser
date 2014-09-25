package com.restlet.sqlimport.export;

import com.restlet.sqlimport.model.resdef.Entity;
import com.restlet.sqlimport.model.resdef.Field;
import com.restlet.sqlimport.model.resdef.Resdef;
import com.restlet.sqlimport.model.sql.Column;
import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.model.sql.ForeignKey;
import com.restlet.sqlimport.model.sql.Table;

/**
 * Transform Database schema to Resdef bean.
 */
public class DatabaseToResdef {

	/**
	 * Get Resdef from database schema
	 * @param database Database schema
	 * @return Resdef
	 */
	public Resdef databaseToResdef(final Database database) {
		final Resdef resdef = new Resdef();
		for(final Table table : database.getTables()) {
			final Entity entity = new Entity();
			entity.setName(table.getName());
			entity.setPkPolicy("user_generated_value");
			// if there is no primary key or there is a primary key with more than one column in the table,
			// we define a new column named "id" which will be the unique primary key of the entity
			if(table.getPrimaryKey().getColumnNames().size() != 1) {
				final Field field = new Field();
				field.setName("id");
				field.setType("string");
				field.setIsPrimaryKey(true);
				entity.getFields().add(field);
			}
			// columns
			for(final String columnName : table.getColumnByNames().keySet()) {
				final Column column = table.getColumnByNames().get(columnName);
				final Field field = new Field();
				field.setName(column.getName());
				// Foreign key
				final ForeignKey foreignKey = table.getForeignKeyForColumnNameOrigin(column);
				if(foreignKey == null) {
					// converted type
					if(column.getConvertedType() != null) {
						field.setType(column.getConvertedType());
					}
				} else {
					// foreign key : type is in the name of the foreign table
					field.setType(foreignKey.getTableNameTarget());
					// number of occurences for the relation
					field.setMinOccurs(0);
					field.setMaxOccurs("*");
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
					field.setIsPrimaryKey(true);
				}
				// nullable
				if((column.getIsNotNull() != null) && column.getIsNotNull()) {
					field.setNullable(false);
				} else {
					field.setNullable(true);
				}
				field.setDefaultValue(column.getDefaultValue());
				entity.getFields().add(field);
			}
			resdef.getEntities().add(entity);
		}
		return resdef;
	}

}
