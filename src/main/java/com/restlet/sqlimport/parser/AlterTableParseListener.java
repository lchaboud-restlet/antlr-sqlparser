package com.restlet.sqlimport.parser;

import com.restlet.sqlimport.model.sql.Column;
import com.restlet.sqlimport.model.sql.Database;
import com.restlet.sqlimport.model.sql.ForeignKey;
import com.restlet.sqlimport.model.sql.Table;
import com.restlet.sqlimport.parser.SqlParser.Alter_table_add_constraintContext;
import com.restlet.sqlimport.parser.SqlParser.Alter_table_stmtContext;
import com.restlet.sqlimport.parser.SqlParser.Any_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Fk_origin_column_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Fk_target_column_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Foreign_tableContext;
import com.restlet.sqlimport.parser.SqlParser.Indexed_columnContext;
import com.restlet.sqlimport.parser.SqlParser.Source_table_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Table_constraint_foreign_keyContext;
import com.restlet.sqlimport.parser.SqlParser.Table_constraint_primary_keyContext;
import com.restlet.sqlimport.util.Util;

/**
 * Parse Listener only for ALTER TABLE statements parsing.
 */
public class AlterTableParseListener extends SqlBaseListener {


	/**
	 * Debug mode to display ANTLR v4 contexts.
	 */
	private static boolean DEBUG = false;

	/**
	 * ANTLR Parser
	 */
	private final SqlParser sqlParser;

	/**
	 * Database schema
	 */
	private final Database database;

	Table table;
	Column column;
	ForeignKey foreignKey;

	boolean inAlter_table_stmt = false; // ALTER TABLE
	boolean inAlter_table_add_constraint = false; // ALTER TABLE with ADD CONSTRAINT
	boolean inTable_constraint_primary_key = false; // PRIMARY KEY in ALTER TABLE
	boolean inTable_constraint_foreign_key = false; // FOREIGN KEY in ALTER TABLE

	/**
	 * Utils methods.
	 */
	Util util = new Util();

	/**
	 * Constructor.
	 * @param sqlParser SQL parser
	 * @param database Database
	 */
	public AlterTableParseListener(final SqlParser sqlParser, final Database database) {
		this.sqlParser = sqlParser;
		this.database = database;
	}

	/**
	 * Used only for debug, its called for each token based on the token "name".
	 */
	@Override
	public void exitAny_name(final Any_nameContext ctx) {
		if(DEBUG) {
			System.out.println(ctx.getText() + " - ctx : " + ctx.toInfoString(sqlParser));
		}
	}

	//--- ALTER TABLE

	@Override
	public void enterAlter_table_stmt(final Alter_table_stmtContext ctx) {
		inAlter_table_stmt = true;
	}

	@Override
	public void exitAlter_table_stmt(final Alter_table_stmtContext ctx) {
		inAlter_table_stmt = false;
	}

	@Override
	public void exitSource_table_name(final Source_table_nameContext ctx) {
		if(inAlter_table_stmt) {
			table = database.getTableForName(util.unformatSqlName(ctx.getText()));
		}
	}

	//--- Add constraint

	@Override
	public void enterAlter_table_add_constraint(
			final Alter_table_add_constraintContext ctx) {
		inAlter_table_add_constraint = true;
	}

	@Override
	public void exitAlter_table_add_constraint(
			final Alter_table_add_constraintContext ctx) {
		inAlter_table_add_constraint = false;
	}

	//--- Constraints

	//--- Primary Key in ALTER TABLE

	@Override
	public void enterTable_constraint_primary_key(
			final Table_constraint_primary_keyContext ctx) {
		inTable_constraint_primary_key = true;
	}

	@Override
	public void exitTable_constraint_primary_key(
			final Table_constraint_primary_keyContext ctx) {
		inTable_constraint_primary_key = false;
	}

	@Override
	public void exitIndexed_column(final Indexed_columnContext ctx) {
		if(inAlter_table_stmt && inTable_constraint_primary_key) {
			final String columnName = util.unformatSqlName(ctx.getText());
			table.getPrimaryKey().getColumnNames().add(columnName);
		}
	}

	//--- Foreign Key in CREATE TABLE or in ALTER TABLE

	@Override
	public void enterTable_constraint_foreign_key(
			final Table_constraint_foreign_keyContext ctx) {
		inTable_constraint_foreign_key = true;
		if(inAlter_table_stmt) {
			foreignKey = new ForeignKey();
			foreignKey.setTableNameOrigin(table.getName());
		}
	}

	@Override
	public void exitTable_constraint_foreign_key(
			final Table_constraint_foreign_keyContext ctx) {
		if(inAlter_table_stmt) {
			foreignKey.setTableNameOrigin(table.getName());
			table.getForeignKeys().add(foreignKey);
			foreignKey = null;
		}
		inTable_constraint_foreign_key = false;
	}

	@Override
	public void exitForeign_table(final Foreign_tableContext ctx) {
		if(inTable_constraint_foreign_key) {
			foreignKey.setTableNameTarget(util.unformatSqlName(ctx.getText()));
		}
	}

	@Override
	public void exitFk_origin_column_name(
			final Fk_origin_column_nameContext ctx) {
		if(inTable_constraint_foreign_key) {
			foreignKey.getColumnNameOrigins().add(util.unformatSqlName(ctx.getText()));
		}
	}

	@Override
	public void exitFk_target_column_name(
			final Fk_target_column_nameContext ctx) {
		if(inTable_constraint_foreign_key) {
			foreignKey.getColumnNameTargets().add(util.unformatSqlName(ctx.getText()));
		}
	}

}
