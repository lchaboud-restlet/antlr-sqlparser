package com.restlet.sqlimport.model;

import java.util.ArrayList;
import java.util.List;

public class Database {

	private List<Table> tables = new ArrayList<Table>();

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(final List<Table> tables) {
		this.tables = tables;
	}

}
