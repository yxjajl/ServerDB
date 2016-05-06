package com.util;

public class WordUtil {
	public static String toTuof(String str) {
		int sd = 'A' - 'a';
		StringBuilder sb = new StringBuilder();
		String[] arr = str.split("_");
		boolean isFirst = true;
		for (String tmp : arr) {
			char head = tmp.charAt(0);
			if ((!isFirst) && Character.isLowerCase(head)) {
				sb.append((char) (head + sd));
			} else {
				sb.append(head);
			}

			isFirst = false;

			sb.append(tmp.substring(1));
		}

		return sb.toString();
	}

	public static String recTuof(String str) {
		return null;
	}

	public static void main(String[] args) {
		System.out.println(toTuof("s_abc_Abc"));
		System.out.println(toTuof("s_abc_1"));
		System.out.println(toTuof("s_abc_"));
		System.out.println(toTuof("s_abc_"));
		System.out.println(toTuof("m_update_time"));
	}
}
