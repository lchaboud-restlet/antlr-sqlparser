package com.restlet.sqlimport.parser;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import com.restlet.sqlimport.model.Column;
import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.model.Table;
import com.restlet.sqlimport.parser.SqlParser.Column_defContext;
import com.restlet.sqlimport.parser.SqlParser.Column_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Create_table_stmtContext;
import com.restlet.sqlimport.parser.SqlParser.NameContext;
import com.restlet.sqlimport.parser.SqlParser.Table_nameContext;
import com.restlet.sqlimport.parser.SqlParser.Type_nameContext;

public class SqlImport {

	public Database read(final InputStream is) {
		if(is == null) {
			return null;
		}

		ANTLRInputStream in;
		try {
			in = new ANTLRInputStream(is);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		return read(in);
	}


	public Database read(final String txt) {
		if(txt == null) {
			return null;
		}
		final ANTLRInputStream in = new ANTLRInputStream(txt);

		return read(in);
	}

	public Database read(final ANTLRInputStream in) {
		if(in == null) {
			return null;
		}
		final SqlLexer l = new SqlLexer(in);
		final SqlParser p = new SqlParser(new CommonTokenStream(l));
		p.addErrorListener(new BaseErrorListener() {
			@Override
			public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line, final int charPositionInLine, final String msg, final RecognitionException e) {
				throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
			}
		});

		final AtomicReference<String> name = new AtomicReference<String>();
		final Map<String, Integer> points = new HashMap<String, Integer>();
		final String[][] grid = new String[5][5];

		final Database database = new Database();

		p.addParseListener(new SqlBaseListener() {
			Table table;
			Column column;
			boolean inCreateTable = false;
			boolean inColumnDef = false;
			boolean inTypeName = false;

			@Override
			public void exitParse(final SqlParser.ParseContext ctx) {
				System.out.println("exit parse : "+ctx.invokingState);
			}

			//--- CREATE TABLE statement

			@Override
			public void enterCreate_table_stmt(final Create_table_stmtContext ctx) {
				inCreateTable = true;
				table = new Table();
			}

			@Override
			public void exitCreate_table_stmt(final Create_table_stmtContext ctx) {
				database.getTables().add(table);
				table = null;
				inCreateTable = false;
			}

			@Override
			public void exitTable_name(final Table_nameContext ctx) {
				if(inCreateTable) {
					table.setName(ctx.getText());
				}
			}

			//--- Column definition

			@Override
			public void enterColumn_def(final Column_defContext ctx) {
				inColumnDef = true;
				if(inCreateTable) {
					column = new Column();
				}
			}

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

			@Override
			public void enterType_name(final Type_nameContext ctx) {
				inTypeName = true;
			}

			@Override
			public void exitType_name(final Type_nameContext ctx) {
				inTypeName = false;
			}

			@Override
			public void exitName(final NameContext ctx) {
				if(inCreateTable && inColumnDef && inTypeName) {
					column.setType(ctx.getText());
				}
			}

		});
		p.parse();

		return database;
	}

}
