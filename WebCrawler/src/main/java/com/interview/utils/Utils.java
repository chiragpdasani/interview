package com.interview.utils;

public class Utils {
	public static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
	public static String getFormattedString(String msg, Object... args) {
		return String.format(msg, args);
	}
}
