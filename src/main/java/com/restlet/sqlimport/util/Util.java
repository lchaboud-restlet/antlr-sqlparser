package com.restlet.sqlimport.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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

}
