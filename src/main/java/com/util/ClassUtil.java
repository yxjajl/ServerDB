package com.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import com.configparse.vo.ConfigDB;

public class ClassUtil {
	public static final String PREFIX = "<";
	public static final String PREFIX_SLASH = "</";
	public static final String SUBFIX = ">";
	public static final String ENTER = "\n";

	public static String getToString(Object obj, String rootName) {
		StringBuilder sb = new StringBuilder();
		sb.append(PREFIX).append(rootName).append(SUBFIX).append(ENTER);
		Field[] arrField = obj.getClass().getDeclaredFields();
		for (Field field : arrField) {
			sb.append(PREFIX).append(field.getName()).append(SUBFIX);
			field.setAccessible(true);
			try {
				sb.append(field.get(obj));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			field.setAccessible(false);
			sb.append(PREFIX_SLASH).append(field.getName()).append(SUBFIX).append(ENTER);
		}
		sb.append(PREFIX_SLASH).append(rootName).append(SUBFIX).append(ENTER);
		return sb.toString();
	}

	public static String getToString2(Object obj, String rootName) {
		StringBuilder sb = new StringBuilder();
		sb.append(PREFIX).append(rootName).append(SUBFIX).append(ENTER);

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				sb.append(PREFIX).append(pd.getName()).append(SUBFIX);
				sb.append(pd.getReadMethod().invoke(obj));
				sb.append(PREFIX_SLASH).append(pd.getName()).append(SUBFIX).append(ENTER);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		sb.append(PREFIX_SLASH).append(rootName).append(SUBFIX).append(ENTER);
		return sb.toString();
	}

	public static void main(String[] args) {
		ConfigDB dd = new ConfigDB();
		dd.setUrl("mysql://192.168.1.11:&asdfasdf");
		dd.setUsername("wenda");
		dd.setPassword("asdfasdf");
		System.out.println(getToString2(dd, "db"));
	}
}
