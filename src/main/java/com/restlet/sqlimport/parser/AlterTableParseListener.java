package com.restlet.sqlimport.parser;

import com.restlet.sqlimport.model.Column;
import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.model.ForeignKey;
import com.restlet.sqlimport.model.Table;
import com.restlet.sqlimport.parser.SqlParser.Alter_table_add_constraintContext;
import com.restlet.sqlimport.parser.SqlParser.Alter_table_stmtContext;
import com.restlet.sqlimport.parser.SqlParser.Any_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Fk_origin_column_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Fk_target_column_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Foreign_key_clauseContext;
import com.restlet.sqlimport.parser.SqlParser.Foreign_tableContext;
import com.restlet.sqlimport.parser.SqlParser.Indexed_columnContext;
import com.restlet.sqlimport.parser.SqlParser.Source_table_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Table_constraint_foreign_keyContext;
import com.restlet.sqlimport.parser.SqlParser.Table_constraint_primary_keyContext;
import com.restlet.sqlimport.util.Util;

public class AlterTableParseListener extends SqlBaseListener {

	private static boolean DEBUG = false;

	private final SqlParser sqlParser;

	private final Database database;

	Table table;
	Column column;
	ForeignKey foreignKey;

	boolean inAlter_table_stmt = false; // ALTER TABLE
	boolean inAlter_table_add_constraint = false; // ALTER TABLE with ADD CONSTRAINT
	boolean inTable_constraint_primary_key = false; // PRIMARY KEY in CREATE TABLE
	boolean inTable_constraint_foreign_key = false; // FOREIGN KEY
	boolean inForeign_key_clause = false;

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
	public void enterForeign_key_clause(final Foreign_key_clauseContext ctx) {
		inForeign_key_clause  = true;
		if(inAlter_table_stmt && !inTable_constraint_foreign_key) {
			foreignKey = new ForeignKey();
			foreignKey.setTableNameOrigin(table.getName());
		}
	}

	@Override
	public void exitForeign_key_clause(final Foreign_key_clauseContext ctx) {
		if(inAlter_table_stmt && !inTable_constraint_foreign_key) {
			foreignKey.setTableNameOrigin(table.getName());
			table.getForeignKeys().add(foreignKey);
			foreignKey = null;
		}
		inForeign_key_clause = false;
	}

	@Override
	public void exitForeign_table(final Foreign_tableContext ctx) {
		if(inTable_constraint_foreign_key || inForeign_key_clause) {
			foreignKey.setTableNameTarget(util.unformatSqlName(ctx.getText()));
		}
	}

	@Override
	public void exitFk_origin_column_name(
			final Fk_origin_column_nameContext ctx) {
		if(inTable_constraint_foreign_key || inForeign_key_clause) {
			foreignKey.getColumnNameOrigins().add(util.unformatSqlName(ctx.getText()));
		}
	}

	@Override
	public void exitFk_target_column_name(
			final Fk_target_column_nameContext ctx) {
		if(inTable_constraint_foreign_key || inForeign_key_clause) {
			foreignKey.getColumnNameTargets().add(util.unformatSqlName(ctx.getText()));
		}
	}

}
