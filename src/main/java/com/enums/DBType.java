package com.enums;

public enum DBType {
	ORACLE {
		public String getStrutsSql(String tableName) {
			return "SELECT * FROM " + tableName + " WHERE ROWNUM <=1";
		}
	},
	MYSQL {
		@Override
		public String getStrutsSql(String tableName) {
			// TODO Auto-generated method stub
			return "SELECT * FROM " + tableName + " limit 1";
		}
	};

	public abstract String getStrutsSql(String tableName);
}
