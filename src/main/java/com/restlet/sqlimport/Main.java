package com.restlet.sqlimport;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import com.restlet.sqlimport.export.ExportToPivotFormat;
import com.restlet.sqlimport.model.Database;
import com.restlet.sqlimport.parser.SqlImport;
import com.restlet.sqlimport.util.Util;

public class Main {

	public static void main(final String[] args) throws FileNotFoundException {

		if(args.length != 2) {
			System.out.println("Please define these two arguments :");
			System.out.println(" 1: input file name");
			System.out.println(" 2: output file name");
			System.exit(0);
		}

		final String input = args[0];
		final String output = args[1];

		final Util util = new Util();

		final InputStream in = util.getInputStream(input);
		final OutputStream os = util.getOutputStream(output);

		final SqlImport sqlImport = new SqlImport();
		final Database database = sqlImport.read(in);

		final ExportToPivotFormat sqlExport = new ExportToPivotFormat();
		sqlExport.write(database, os);
	}

}
