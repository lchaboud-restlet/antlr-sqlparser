package com.restlet.sqlimport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.restlet.sqlimport.report.Report;
import com.restlet.sqlimport.report.ReportManager;
import com.restlet.sqlimport.util.Util;

/**
 * Main class to parse SQL file and export to a file in the pivot format.
 */
public class Main {

	public static void main(final String[] args) throws FileNotFoundException {

		if(args.length != 2) {
			System.out.println("Please define these two arguments :");
			System.out.println(" 1: input file name");
			System.out.println(" 2: output file name");
			System.exit(1);
		}

		final String input = args[0];
		final String output = args[1];

		final Util util = new Util();

		InputStream in = null;
		OutputStream os = null;

		try {
			in = util.getInputStream(input);
			os = util.getOutputStream(output);

			final MainProcess mainProcess = new MainProcess();

			final String inputContent = util.read(in);
			final String outputContent = mainProcess.process(inputContent);
			util.write(outputContent, os);

			final Report report = mainProcess.getReport();

			final ReportManager reportManager = new ReportManager();
			System.out.println(reportManager.toString(report));
			System.out.println("\n\n==========================\n\n");
			System.out.println(reportManager.toStringSchema(report));

			System.exit(0);
		}
		catch (final Exception e) {
			System.err.println(e.getMessage());
			System.err.println(e);
			System.exit(1);
		}
		finally {
			if(in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					System.err.println(e.getMessage());
					System.err.println(e);
				}
			}
			if(os != null) {
				try {
					os.close();
				} catch (final IOException e) {
					System.err.println(e.getMessage());
					System.err.println(e);
				}
			}
		}
	}

}
