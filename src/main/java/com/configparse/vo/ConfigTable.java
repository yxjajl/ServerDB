package com.configparse.vo;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetProperty;

@ObjectCreate(pattern = "project/tables/table")
public class ConfigTable {
	@BeanPropertySetter(pattern = "project/tables/table")
	private String columns;
	@SetProperty(pattern = "project/tables/table", attributeName = "schema")
	private String schema;
	@SetProperty(pattern = "project/tables/table", attributeName = "name")
	private String name;
	@SetProperty(pattern = "project/tables/table", attributeName = "primarykey")
	private String primarykey;

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public String getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(String primarykey) {
		this.primarykey = primarykey;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table ");
		sb.append(" schema=").append(schema);
		sb.append(" name=").append(name);
		sb.append(" primarykey=").append(primarykey);
		sb.append(" >");
		sb.append(columns);
		sb.append("</table>").append("\n");
		
		return sb.toString();
	}

}
