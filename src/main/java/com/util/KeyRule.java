package com.util;

public class KeyRule {
	private static final String LINE = "_";

	public static String schemaTable(String schema, String table) {
		return schema + LINE + table;
	}
}
