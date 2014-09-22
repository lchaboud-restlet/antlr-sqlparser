package com.restlet.sqlimport.parser;


import java.io.InputStream;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import com.restlet.sqlimport.model.Column;
import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.model.ForeignKey;
import com.restlet.sqlimport.model.Table;
import com.restlet.sqlimport.parser.SqlParser.Any_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Column_constraint_not_nullContext;
import com.restlet.sqlimport.parser.SqlParser.Column_constraint_primary_keyContext;
import com.restlet.sqlimport.parser.SqlParser.Column_defContext;
import com.restlet.sqlimport.parser.SqlParser.Column_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Create_table_stmtContext;
import com.restlet.sqlimport.parser.SqlParser.Fk_origin_column_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Fk_target_column_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Foreign_key_clauseContext;
import com.restlet.sqlimport.parser.SqlParser.Foreign_tableContext;
import com.restlet.sqlimport.parser.SqlParser.NameContext;
import com.restlet.sqlimport.parser.SqlParser.Table_constraint_foreign_keyContext;
import com.restlet.sqlimport.parser.SqlParser.Table_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Type_nameContext;
import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLine;
import com.restlet.sqlimport.report.ReportStatus;

public class SqlImport {

	/**
	 * Debug : display context information during ANTLR v4 parsing.
	 */
	public static final boolean DEBUG = false;

	/**
	 * Log activated.
	 */
	public static final boolean LOG_ACTIVATED = true;

	/**
	 * Report.
	 */
	private final Report report;

	/**
	 * Constructor.
	 * @param report Report (must not be null)
	 */
	public SqlImport(final Report report) {
		this.report = report;
	}


	/**
	 * Errors listener which display SQL query.
	 */
	public class SqlImportErrorListener extends BaseErrorListener {
		public String query;
		public boolean hasError;
		@Override
		public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line, final int charPositionInLine, final String msg, final RecognitionException e) {

			hasError = true;

			if(LOG_ACTIVATED) {
				System.out.println("------------");
				System.out.println("Error on query : \n"+query);
				System.out.println("=> line " + line + " : " + msg);
				if(e != null) {
					if(e.getMessage() != null) {
						System.out.println(e.getMessage());
					}
					if(e.getCtx() != null) {
						System.out.println("Context : "+e.getCtx());
					}
				}
			}

			final ReportLine reportLine = getReport().getReportLineByQuerys().get(query);
			reportLine.setReportStatus(ReportStatus.PARSING_ERROR);
			final StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("=> line ").append(line).append(" : ").append(msg);
			if(e != null) {
				if(e.getMessage() != null) {
					strBuffer.append(e.getMessage());
				}
				if(e.getCtx() != null) {
					strBuffer.append("Context : "+e.getCtx());
				}
			}
			reportLine.setMsg(strBuffer.toString());
		}
	}

	/**
	 * Read input stream to get database schema.
	 * 
	 * @param is input stream
	 * @return Database schema
	 */
	public Database read(final InputStream is) {
		if(is == null) {
			return null;
		}

		final GetSqlQuery getSqlQuery = new GetSqlQuery(getReport());
		final List<String> querys = getSqlQuery.getSqlQuerys(is);
		final Database database = read(querys);

		return database;
	}

	/**
	 * 
	 * @param querys
	 * @return
	 */
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
			ForeignKey foreignKey;

			boolean inCreateTable = false;
			boolean inColumnDef = false;
			boolean inTypeName = false;

			/**
			 * Used only for debug, its called for each token based on the token "name".
			 */
			@Override
			public void exitAny_name(final Any_nameContext ctx) {
				if(DEBUG) {
					System.out.println(ctx.getText() + " - ctx : " + ctx.toInfoString(p));
				}
			}

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
			public void enterTable_constraint_foreign_key(
					final Table_constraint_foreign_keyContext ctx) {
				if(inCreateTable) {
					foreignKey = new ForeignKey();
					foreignKey.setTableNameOrigin(table.getName());
				}
			}

			@Override
			public void exitTable_constraint_foreign_key(
					final Table_constraint_foreign_keyContext ctx) {
				if(inCreateTable) {
					foreignKey.setTableNameOrigin(table.getName());
					table.getForeignKeys().add(foreignKey);
					foreignKey = null;
				}
			}

			@Override
			public void enterForeign_key_clause(final Foreign_key_clauseContext ctx) {
				if(inColumnDef) {
					foreignKey = new ForeignKey();
					foreignKey.setTableNameOrigin(table.getName());
					foreignKey.getColumnNameOrigins().add(column.getName());
				}
			}

			@Override
			public void exitForeign_key_clause(final Foreign_key_clauseContext ctx) {
				if(inColumnDef) {
					foreignKey.setTableNameOrigin(table.getName());
					table.getForeignKeys().add(foreignKey);
					foreignKey = null;
				}
			}

			@Override
			public void exitForeign_table(final Foreign_tableContext ctx) {
				if(inCreateTable) {
					foreignKey.setTableNameTarget(ctx.getText());
				}
			}

			@Override
			public void exitFk_origin_column_name(
					final Fk_origin_column_nameContext ctx) {
				if(foreignKey != null) {
					foreignKey.getColumnNameOrigins().add(ctx.getText());
				}
			}

			@Override
			public void exitFk_target_column_name(
					final Fk_target_column_nameContext ctx) {
				if(inCreateTable) {
					foreignKey.getColumnNameTargets().add(ctx.getText());
				}
			}

		});
		p.parse();

		if(!listener.hasError) {
			final ReportLine reportLine = getReport().getReportLineByQuerys().get(query);
			reportLine.setReportStatus(ReportStatus.SUCCESS);
		}
	}

	/**
	 * Get report.
	 * @return report.
	 */
	public Report getReport() {
		return report;
	}

}
