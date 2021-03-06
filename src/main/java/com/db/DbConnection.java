package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cache.RedisMap;
import com.configparse.vo.ConfigProject;
import com.configparse.vo.ConfigTable;
import com.util.KeyRule;

public class DbConnection {
	private static Logger LOGGER = LoggerFactory.getLogger(DbConnection.class);
	private static final String SELECT = " select ";
	private static final String FROM = " from ";

	public static void loadTable(ConfigProject configProject) throws Exception {
		String url = configProject.getConfigDB().getUrl();
		String username = configProject.getConfigDB().getUsername();
		String password = configProject.getConfigDB().getPassword();

		Connection conn = getConnection("com.mysql.jdbc.Driver", url, username, password);
		try {
			for (ConfigTable ct : configProject.getTables()) {
				try {
					List<String> list = loadTable(conn, ct.getSchema(), ct.getName(), ct.getColumns());
					for (String str : list) {
						String key = JSON.parseObject(str).get(ct.getPrimarykey()).toString();
						RedisMap.hset(KeyRule.schemaTable(ct.getSchema(), ct.getName()), key, str);
					}
				} catch (Exception e) {
					LOGGER.error("load table {}.{} Error ", ct.getSchema(), ct.getName());
					throw e;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static Connection getConnection(String driver, String url, String username, String password) {
		try {
			Properties props = new Properties();
		    props.put("remarksReporting","true");//注意这里 oracle加这个才能取到字段注释
		    
		    props.put("user", username);
		    props.put("password", password);
		    
			Class.forName(driver);
			//Connection con = DriverManager.getConnection(url, username, password);
			Connection con = DriverManager.getConnection(url, props);
			return con;
		} catch (ClassNotFoundException cfe) {
			LOGGER.error("数据库连接失败！{} ", cfe);
		} catch (SQLException se) {
			LOGGER.error("数据库连接失败！{} ", se);
		}
		return null;
	}

	public static List<String> loadTable(Connection con, String schema, String tableName, String columns) throws SQLException {
		Statement stmt = con.createStatement();

		StringBuilder strSql = new StringBuilder();
		strSql.append(SELECT).append(columns).append(FROM).append(schema).append(".").append(tableName);

		ResultSet rs = stmt.executeQuery(strSql.toString());
		ResultSetMetaData rsmd = rs.getMetaData();
		List<String> result = new ArrayList<String>();
		while (rs.next()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int m = 1; m <= rsmd.getColumnCount(); m++) {
				map.put(rsmd.getColumnLabel(m), getValue(rs, rsmd.getColumnType(m), m));
			}
			result.add(JSON.toJSONString(map));
		}
		return result;
	}

	public static Object getValue(ResultSet rs, int type, int columnName) throws SQLException {
		if (type == Types.INTEGER) {
			return rs.getInt(columnName);
		} else if (type == Types.BIGINT) {
			return rs.getLong(columnName);
		} else if (type == Types.VARCHAR) {
			return rs.getString(columnName);
		} else if (type == Types.DATE) {
			return rs.getTimestamp(columnName);
		}
		return null;
	}

	public static String getColumns(Connection conn, String tableName) throws Exception {
		StringJoiner sj = new StringJoiner(",");
		String sql = "select * from " + tableName + " where rownum <=1";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int m = 1; m <= rsmd.getColumnCount(); m++) {
			//			System.out.println(rsmd.getColumnName(m) + "," + rsmd.getColumnLabel(m));
			sj.add(rsmd.getColumnName(m));
		}
		return sj.toString();
	}

	public static void copyData(Connection srcconn, Connection dirconn, String tableName) throws Exception {
		String sql = "select * from " + tableName + " ";
		Statement stmt = srcconn.createStatement();

		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();

		dirconn.setAutoCommit(false);
		StringJoiner sj = new StringJoiner(",");
		for (int m = 1; m <= rsmd.getColumnCount(); m++) {
			sj.add("?");
		}
		PreparedStatement pstmt = dirconn.prepareStatement("insert into " + tableName + " values (" + sj.toString() + ")");
		pstmt.clearBatch();
		for (; rs.next();) {
			for (int m = 1; m <= rsmd.getColumnCount(); m++) {
				//System.out.println(m);
				pstmt.setObject(m, rs.getObject(m));

			}
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		dirconn.commit();
	}

	public static void main(String[] args) throws Exception {
		//		String url = "jdbc:mysql://10.1.0.3:3306/wenda?useUnicode=true&characterEncoding=UTF-8";
		//		String username = "wenda";
		//		String password = "wenda";
		//		Connection conn = DbConnection.getConnection("com.mysql.jdbc.Driver",url, username, password);
		//
		//		List<String> td = loadTable(conn, "`dzq_job`", "`company`", "company_id,name");
		//		for (String str : td) {
		//			CompanyVO cv = JSON.parseObject(str, CompanyVO.class);
		//			System.out.println("str = " + cv.getCompany_id() + "," + cv.getName());
		//		}
		//		CopyTable();
	}

	public static void CopyTable() {
		Connection srcConn = DbConnection.getConnection("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@10.140.161.12:1521:ORCL", "ECSAPP2", "ECSAPP2");
		//		System.out.println(getColumns(srcConnection, "GE_USER"));
		Connection dirConn = DbConnection.getConnection("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@10.140.161.12:1521:ORCL", "epaydev",
				"Epaydev123");
		//		for (String tt : getTableList()) {
		//			try {
		//				copyData(srcConn, dirConn, tt);
		//			} catch (Exception e) {
		//				e.printStackTrace();
		//			}
		//		}
	}

	public static String[] getTableList() {
		return new String[] {
				//				"ge_activiti_variable", "ge_area", "ge_company", "ge_daily_record", "ge_department", "ge_dict_data", "ge_dict_type",
				//				"ge_permission", "ge_permission_resources", "ge_resources", "ge_resources_icon", "ge_role", "ge_role_permission", "ge_sale_area",
				//				"ge_sale_area_def", "ge_sale_area_free", "ge_user", "ge_usergroup", "ge_usergroup_role", "ge_user_dep", "ge_user_role", "ge_user_usergroup"
				//				"GE_AREA_DATA"

		};
	}
}

class CompanyVO {
	int company_id;
	String name;

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
