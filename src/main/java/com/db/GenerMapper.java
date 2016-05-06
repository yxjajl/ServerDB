package com.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import com.enums.DBType;

public class GenerMapper {
	public static String filePath = GenerMapper.class.getResource("").getPath().replaceAll("%20", " ");

	public static List<DBColumnVO> getColumns2(Connection conn, String Schema, String tableName, DBType dbType) throws Exception {
		List<DBColumnVO> list = new ArrayList<DBColumnVO>();
		DatabaseMetaData dbmd = conn.getMetaData();
		//ResultSet rs = dbmd.getTables(null, "ECSAPP2", "%", new String[] {  "TABLE"  });
		ResultSet rs = dbmd.getColumns(null, Schema, tableName.toUpperCase(), "%");
		//printResultSet(rs);

		while (rs.next()) {
			DBColumnVO dbColumnVO = new DBColumnVO();
			dbColumnVO.comments = rs.getString("REMARKS");

			dbColumnVO.columnName = rs.getString("COLUMN_NAME");
			dbColumnVO.columnType = rs.getInt("DATA_TYPE");
			dbColumnVO.columnTypeName = rs.getString("TYPE_NAME");
			//COLUMN_SIZE
			dbColumnVO.javaName = getJavaName(dbColumnVO.columnName);
			dbColumnVO.javaTypeName = getJavaTypeName(dbColumnVO.columnType, dbColumnVO.columnTypeName);
			list.add(dbColumnVO);
		}

		return list;
	}

	public static void printJavaBean(List<DBColumnVO> list) {
		String str = "private %s %s; // %s";
		for (DBColumnVO dbColumnVO : list) {
			System.out.println(String.format(str, dbColumnVO.javaTypeName, dbColumnVO.javaName, dbColumnVO.comments));
		}
	}

	public static void printMapper(List<DBColumnVO> list) {
		String path = filePath + "MapperTemp.txt";
		StringBuilder resultMap = new StringBuilder();
		StringJoiner columns = new StringJoiner(",");

		resultMap.append("<resultMap id=\"BaseResultMap\" type=\"com.cignacmb.epay.domain.BankInfoModel\">\n");

		String template = "<result column=\"%s\" property=\"%s\" jdbcType=\"%s\" /> \n";
		for (DBColumnVO dbColumnVO : list) {
			columns.add(dbColumnVO.columnName);
			resultMap.append(String.format(template, dbColumnVO.columnName, dbColumnVO.javaName, dbColumnVO.columnTypeName));
		}

		resultMap.append("</resultMap>\n");

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("@Namespace", "namespace");
		param.put("@resultMap", resultMap.toString());
		param.put("@Columns", columns.toString());

		FileReader fi = null;
		BufferedReader bs = null;
		try {
			fi = new FileReader(path);
			bs = new BufferedReader(fi);
			while (true) {
				String line = bs.readLine();
				if (line == null) {
					break;
				}
				System.out.println(myreplace(line, param));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				bs.close();
			} catch (Exception e) {
			}

		}
	}

	public static String myreplace(String str, Map<String, String> param) {
		String tmp = str;
		for (Entry<String, String> entry : param.entrySet()) {
			tmp = tmp.replace(entry.getKey(), entry.getValue());
		}
		return tmp;
	}

	public static void printResultSet(ResultSet rs) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			System.out.print(rsmd.getColumnName(i + 1) + "\t");
		}
		System.out.println();
		while (rs.next()) {
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				System.out.print(rs.getString(i + 1) + "\t");
			}
			System.out.println("Remark: " + rs.getString("REMARKS"));
			System.out.println();
		}
		rs.close();
	}

	public static void main(String[] args) throws Exception {
		Connection conn = DbConnection.getConnection("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@10.140.161.12:1521:ORCL", "ECSAPP2", "ECSAPP2");
		//		List<DBColumnVO> list = getColumns(conn, "ge_user", DBType.ORACLE);
		List<DBColumnVO> list = getColumns2(conn, "ECSAPP2", "ge_user", DBType.ORACLE);
		printJavaBean(list);
		printMapper(list);
		//ge_user
	}

	public static String getJavaName(String column) {
		column = column.toLowerCase();
		String[] arr = column.split("_");
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (String tmp : arr) {
			if (isFirst) {
				sb.append(tmp);
				isFirst = false;
			} else {
				sb.append(tmp.substring(0, 1).toUpperCase());
				sb.append(tmp.substring(1));
			}
		}
		return sb.toString();
	}

	public static String getJavaTypeName(int jdbcType, String jdbcTypeName) {
		String result = "";
		switch (jdbcType) {
		case Types.NUMERIC:
			result = "long";
			break;
		case Types.VARCHAR:
			result = "String";
			break;
		case Types.DATE:
			result = "Date";
			break;
		default:
			System.out.println("Unknow jdbcType " + jdbcTypeName);
			result = "";
			break;
		}
		return result;
	}

	//	public static List<DBColumnVO> getColumns(Connection conn, String tableName, DBType dbType) throws Exception {
	//		String sql = dbType.getStrutsSql(tableName);
	//		Statement stmt = conn.createStatement();
	//		ResultSet rs = stmt.executeQuery(sql);
	//		ResultSetMetaData rsmd = rs.getMetaData();
	//
	//		List<DBColumnVO> list = new ArrayList<DBColumnVO>();
	//		for (int m = 1; m <= rsmd.getColumnCount(); m++) {
	//			DBColumnVO dbColumnVO = new DBColumnVO();
	//			dbColumnVO.columnName = rsmd.getColumnName(m);
	//			dbColumnVO.columnType = rsmd.getColumnType(m);
	//			dbColumnVO.columnTypeName = rsmd.getColumnTypeName(m);
	//			dbColumnVO.javaName = getJavaName(dbColumnVO.columnName);
	//			dbColumnVO.javaTypeName = getJavaTypeName(dbColumnVO.columnType, dbColumnVO.columnTypeName);
	//			list.add(dbColumnVO);
	//		}
	//		return list;
	//	}
}

class DBColumnVO {
	public String columnName;
	public int columnType;
	public String columnTypeName;
	public String javaTypeName;
	public String javaName;
	public String comments = "";
}
