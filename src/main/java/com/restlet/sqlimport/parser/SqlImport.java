package com.restlet.sqlimport.parser;


import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportLine;
import com.restlet.sqlimport.report.ReportLineStatus;

public class SqlImport {

	/**
	 * Log activated.
	 */
	public static final boolean LOG_ACTIVATED = false;

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

			final ReportLine reportLine = getReport().getReportLineForQuery(query);
			reportLine.setReportLineStatus(ReportLineStatus.PARSING_ERROR);
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
			reportLine.setMessage(strBuffer.toString());
		}
	}

	/**
	 * Read input stream to get database schema.
	 * 
	 * @param content SQL file content
	 * @return Database schema
	 */
	public Database getDatabase(final String content) {
		if(content == null) {
			return null;
		}

		final GetSqlQuery getSqlQuery = new GetSqlQuery(getReport());
		final List<String> querys = getSqlQuery.getSqlQuerys(content);

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

		// Add database to the report
		report.setDatabase(database);

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

		if(LOG_ACTIVATED) {
			System.out.println("Parse the query : \n"+query);
		}

		// Fill database schema from SQL input stream read by ANTLR
		if(query.toUpperCase().indexOf("CREATE TABLE") == 0) {
			p.addParseListener(new CreateTableParseListener(p, database));
		}
		else if(query.toUpperCase().indexOf("ALTER TABLE") == 0) {
			p.addParseListener(new AlterTableParseListener(p, database));
		}
		else {
			throw new RuntimeException("No parse listener for the query : "+query);
		}

		try {
			p.parse();
			if(!listener.hasError) {
				final ReportLine reportLine = getReport().getReportLineForQuery(query);
				reportLine.setReportLineStatus(ReportLineStatus.PARSED);
			}
		} catch(final Exception e) {
			final ReportLine reportLine = getReport().getReportLineForQuery(query);
			reportLine.setReportLineStatus(ReportLineStatus.PARSING_ERROR);
			reportLine.setMessage(e.getMessage());
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
