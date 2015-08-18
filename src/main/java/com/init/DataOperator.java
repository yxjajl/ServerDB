package com.init;

import java.util.List;

import com.cache.RedisMap;

public class DataOperator {
	public String getTableRecord(String schemaTable, String key) {
		return RedisMap.hget(schemaTable, key);
	}

	public List<String> getTableAllRecord(String schemaTable) {
		return RedisMap.hvals(schemaTable);
	}

}
