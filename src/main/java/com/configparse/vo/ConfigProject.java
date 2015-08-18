package com.configparse.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

@ObjectCreate(pattern = "project")
public class ConfigProject {
	private List<ConfigTable> tables;
	private ConfigRedis configRedis;
	private ConfigDB configDB;

	public List<ConfigTable> getTables() {
		return tables;
	}

	public void setTables(List<ConfigTable> tables) {
		this.tables = tables;
	}

	@SetNext
	public void addTable(ConfigTable configTable) {
		if (tables == null) {
			tables = new ArrayList<ConfigTable>();
		}
		tables.add(configTable);
	}

	public ConfigRedis getConfigRedis() {
		return configRedis;
	}

	@SetNext
	public void setConfigRedis(ConfigRedis configRedis) {
		this.configRedis = configRedis;
	}

	public ConfigDB getConfigDB() {
		return configDB;
	}

	@SetNext
	public void setConfigDB(ConfigDB configDB) {
		this.configDB = configDB;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<project>").append("\n");
		sb.append(configRedis);
		sb.append(configDB);
		if (tables != null) {
			for (ConfigTable ct : tables) {
				sb.append(ct);
			}
		}
		sb.append("</project>").append("\n");
		return sb.toString();
	}

}
