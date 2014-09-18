package com.restlet.sqlimport.parser;


import java.io.InputStream;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import com.restlet.sqlimport.model.CleEtrangere;
import com.restlet.sqlimport.model.Column;
import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.model.Table;
import com.restlet.sqlimport.parser.SqlParser.Column_constraint_not_nullContext;
import com.restlet.sqlimport.parser.SqlParser.Column_constraint_primary_keyContext;
import com.restlet.sqlimport.parser.SqlParser.Column_defContext;
import com.restlet.sqlimport.parser.SqlParser.Column_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Create_table_stmtContext;
import com.restlet.sqlimport.parser.SqlParser.Foreign_key_clauseContext;
import com.restlet.sqlimport.parser.SqlParser.Foreign_tableContext;
import com.restlet.sqlimport.parser.SqlParser.NameContext;
import com.restlet.sqlimport.parser.SqlParser.Table_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Type_nameContext;

public class SqlImport {

	public class SqlImportErrorListener extends BaseErrorListener {
		public String query;
		@Override
		public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line, final int charPositionInLine, final String msg, final RecognitionException e) {
			System.out.println("------------");
			System.out.println("Error on query : \n"+query);
			System.out.println("=> line " + line + " : " + msg);
			if(e != null) {
				if(e.getMessage() != null) {
					System.out.println(e.getMessage());
				}
				//System.out.println(e);
			}
		}
	}

	public Database read(final InputStream is) {
		if(is == null) {
			return null;
		}

		final GetSqlQuery getSqlQuery = new GetSqlQuery();
		final List<String> querys = getSqlQuery.getSqlQuerys(is);
		final Database database = read(querys);

		return database;
	}

	public Database read(final List<String> querys) {

		final Database database = new Database();

		for(final String query : querys) {
			readOneQuery(database, query);
		}

		return database;
	}

	/**
	 * Read SQL statements from string value
	 * @param database Database schema
	 * @param txt SQL statements as string value
	 * @return Database schema
	 */
	public void readOneQuery(final Database database, final String query) {
		if(query == null) {
			return;
		}
		final ANTLRInputStream in = new ANTLRInputStream(query);

		final SqlLexer l = new SqlLexer(in);
		final SqlParser p = new SqlParser(new CommonTokenStream(l));

		// Errors catching
		final SqlImportErrorListener listener = new SqlImportErrorListener();
		listener.query = query;
		p.addErrorListener(listener);

		// Fill database schema from SQL input stream read by ANTLR
		p.addParseListener(new SqlBaseListener() {

			Table table;
			Column column;
			CleEtrangere cleEtrangere;

			boolean inCreateTable = false;
			boolean inColumnDef = false;
			boolean inTypeName = false;
			boolean inForeignKey = false;

			//--- CREATE TABLE

			/**
			 * enter CREATE TABLE
			 */
			@Override
			public void enterCreate_table_stmt(final Create_table_stmtContext ctx) {
				inCreateTable = true;
				table = new Table();
			}

			/**
			 * exit CREATE TABLE
			 */
			@Override
			public void exitCreate_table_stmt(final Create_table_stmtContext ctx) {
				database.getTables().add(table);
				table = null;
				inCreateTable = false;
			}

			/**
			 * Table name
			 */
			@Override
			public void exitTable_name(final Table_nameContext ctx) {
				if(inCreateTable) {
					table.setName(ctx.getText());
				}
			}

			//--- Column definition

			/**
			 * enter Column definition
			 */
			@Override
			public void enterColumn_def(final Column_defContext ctx) {
				inColumnDef = true;
				if(inCreateTable) {
					column = new Column();
				}
			}

			/**
			 * exit Column definition
			 */
			@Override
			public void exitColumn_def(final Column_defContext ctx) {
				if(inCreateTable) {
					if((column != null) && (column.getName() != null)) {
						table.getColumnByNames().put(column.getName(), column);
					}
					column = null;
				}
				inColumnDef = false;
			}

			/**
			 * Column name
			 */
			@Override
			public void exitColumn_name(final Column_nameContext ctx) {
				if(inCreateTable && inColumnDef) {
					column.setName(ctx.getText());
				}
			}

			//--- Column type

			/**
			 * enter Column type
			 */
			@Override
			public void enterType_name(final Type_nameContext ctx) {
				inTypeName = true;
			}

			/**
			 * exit column type
			 */
			@Override
			public void exitType_name(final Type_nameContext ctx) {
				inTypeName = false;
			}

			/**
			 * Name. It could be : <br/>
			 * - type name
			 */
			@Override
			public void exitName(final NameContext ctx) {
				if(inCreateTable && inColumnDef && inTypeName) {
					if(column.getType() == null) {
						column.setType(ctx.getText());
					} else {
						column.setType(column.getType() + " " + ctx.getText());
					}
				}
			}

			//--- Constraints

			//--- Not Null

			@Override
			public void exitColumn_constraint_not_null(
					final Column_constraint_not_nullContext ctx) {
				if(inCreateTable && inColumnDef) {
					column.setIsNotNull(true);
				}
			}

			//--- Primary Key

			@Override
			public void exitColumn_constraint_primary_key(
					final Column_constraint_primary_keyContext ctx) {
				if(inCreateTable && inColumnDef) {
					table.setPrimaryKey(column);
				}
			}

			//--- Foreign Key

			@Override
			public void enterForeign_key_clause(final Foreign_key_clauseContext ctx) {
				inForeignKey = true;
				cleEtrangere = new CleEtrangere();
			}

			@Override
			public void exitForeign_key_clause(final Foreign_key_clauseContext ctx) {
				inForeignKey = false;
				if(inCreateTable) {
					table.getCleEtrangeres().add(cleEtrangere);
				}
			}

			@Override
			public void exitForeign_table(final Foreign_tableContext ctx) {
				cleEtrangere.setTableTarget(ctx.getText());
			}

		});
		p.parse();
	}

}
