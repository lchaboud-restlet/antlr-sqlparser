package com.restlet.sqlimport.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Util {

	/**
	 * Search the given file (or folder) using the ClassPath
	 * @param fileName file name or folder name
	 * @return
	 */
	public File getFileByClassPath(final String fileName) {
		final URL url = Util.class.getResource(fileName);
		if ( url != null ) {
			URI uri = null ;
			try {
				uri = url.toURI();
			} catch (final URISyntaxException e) {
				throw new RuntimeException("Cannot convert URL to URI (file '" + fileName + "')");
			}
			return new File(uri);
		}
		else {
			throw new RuntimeException("File '" + fileName + "' not found");
		}
	}

	/**
	 * Get input stream for file writing.
	 * @param filename File name
	 * @return Input stream
	 */
	public InputStream getInputStream(final String filename) {
		try {
			final File file = new File(filename);
			final InputStream in = new FileInputStream(file);
			return in;
		} catch (final FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.err.println(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Return output stream for file writing and create directory if not exists.
	 * @param filename Filename
	 * @return output stream
	 */
	public OutputStream getOutputStream(final String filename) {
		try {

			final String path = filename.substring(0, filename.lastIndexOf(File.separatorChar));
			final File dir = new File(path);
			dir.mkdirs();

			final File file = new File(filename);
			return new FileOutputStream(file);

		} catch (final Exception e) {
			System.err.println(e.getMessage());
			System.err.println(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Read input stream and return content
	 * @param is Input stream
	 * @return content
	 */
	public String read(final InputStream is) {
		BufferedReader br = null;
		final StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			boolean isFirst = true;
			while ((line = br.readLine()) != null) {
				if(isFirst) {
					isFirst = false;
				} else {
					sb.append("\n");
				}
				sb.append(line);
			}

		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Write lines to a output stream.
	 * @param lines Lines
	 * @param os Output stream
	 */
	public void write(final List<String> lines, final OutputStream os) {
		BufferedWriter bw = null;

		bw = new BufferedWriter(new OutputStreamWriter(os));

		try {
			boolean isFirst = true;
			for(final String line : lines) {
				if(isFirst) {
					isFirst = false;
				} else {
					bw.write("\n");
				}
				bw.write(line);
			}
			bw.close();
		} catch (final IOException e) {
			System.err.println(e.getMessage());
			System.err.println(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Write content to a output stream.
	 * @param content Content
	 * @param os Output stream
	 */
	public void write(final String content, final OutputStream os) {
		BufferedWriter bw = null;

		bw = new BufferedWriter(new OutputStreamWriter(os));

		try {
			if(content != null) {
				bw.write(content);
			}
			bw.close();
		} catch (final IOException e) {
			System.err.println(e.getMessage());
			System.err.println(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Format name from SQL file.
	 * @param sqlName Name in SQL file
	 * @return name
	 */
	public String unformatSqlName(final String sqlName) {
		if(sqlName == null) {
			return null;
		}
		String name = sqlName;
		// escape character : "
		if(name.length() >= 2) {
			if((name.charAt(0) == '"') && (name.charAt(name.length()-1) == '"')) {
				name = name.substring(1,name.length()-1);
			}
		}
		// escape character : `
		if(name.length() >= 2) {
			if((name.charAt(0) == '`') && (name.charAt(name.length()-1) == '`')) {
				name = name.substring(1,name.length()-1);
			}
		}
		return name;
	}

}
