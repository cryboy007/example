package com.github.cryboy007.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 字符串工具类
 * 
 * @author hangwen
 *
 */
public class StringUtil {

	public static final String EMPTY = "";

	/**
	 * 判断字符串是否是 空字符串 或者 是null
	 * 
	 * @param str
	 * @return null、""、"null","[]" 返回true，否则false
	 */
	public static boolean isEmptyOrNull(String str) {
		if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.equals("[]")) {
			return true;
		}

		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 为字符串添加下划线，营造一种超链接的假象
	 * 
	 * @param str
	 * @return
	 */
	public static String genUnderline(String str, String fontColor) {
		if (StringUtil.isEmptyOrNull(str)) {
			return "";
		}

		if (StringUtil.isEmptyOrNull(fontColor)) {
			return StringUtil.format("<html><u>{0}</u></html>", str);
		}

		return StringUtil.format("<html><font color=\"{1}\"><u>{0}</u></font></html>", str, fontColor);
	}

	public static boolean isEmptyOrNull(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmptyOrNull(value);
			}
		}
		return result;
	}

	/**
	 * {@link StringUtil#isEmptyOrNull(String)}取反
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmptyOrNull(String str) {
		return !isEmptyOrNull(str);
	}

	/**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toFirstCharUpper(String str) {
		if (StringUtil.isEmptyOrNull(str)) {
			return StringUtil.EMPTY;
		}
		char c = str.charAt(0);
		if (Character.isUpperCase(c)) {
			return str;
		}
		StringBuffer sb = new StringBuffer(str);
		sb.setCharAt(0, Character.toUpperCase(c));
		return sb.toString();
	}
	
	/**
	 * 首字母小写
	 * 
	 * @param str
	 * @return
	 */
	public static String toFirstCharLower(String str) {
		if (StringUtil.isEmptyOrNull(str)) {
			return StringUtil.EMPTY;
		}
		char c = str.charAt(0);
		if (Character.isLowerCase(c)) {
			return str;
		}
		StringBuffer sb = new StringBuffer(str);
		sb.setCharAt(0, Character.toLowerCase(c));
		return sb.toString();
	}

	/**
	 * 格式化字符串
	 * 
	 * @param value
	 *            字符串中可以存在变量占位符{0}...{n}
	 * @param paras
	 *            对应占位符的变量值
	 * @return
	 */
	public static String format(String value, Object... paras) {
		return MessageFormat.format(value, paras);
	}

	public static String replace(String value) {
		String src = new String(value);
		src = replace(src, "*", "//*");
		src = replace(src, "?", "//?");
		src = replace(src, ")", "//)");
		src = replace(src, "(", "//(");
		src = replace(src, "{", "//{");
		src = replace(src, "}", "//}");
		src = replace(src, "|", "//|");
		src = replace(src, "$", "//$");
		src = replace(src, "+", "//+");
		src = replace(src, ".", "//.");
		return src;
	}

	public static String replace(String value, String old, String newChar) {
		return value.replace(old, newChar);
	}

	public static String getCode(Object... args) {
		StringBuilder sb = new StringBuilder();
		for (Object arg : args) {
			sb.append(arg);
		}

		int length = sb.length();
		int start = sb.indexOf("[");
		int end = sb.indexOf("]");
		if (length > 0 && start < end && end <= length) {
			return sb.substring(start + 1, end);
		}

		return sb.toString();
	}

	public static String getKey(Object... args) {
		return getKeyBySplit("_", args);
	}

	public static String getKeyBySplit(String split, Object... args) {
		StringBuilder sb = new StringBuilder();

		for (Object arg : args) {
			sb.append(arg);
			sb.append(split);
		}

		int length = sb.length();
		if (length > 0) {
			sb.delete(length - split.length(), length);
		}

		return sb.toString();
	}

	public static String conStringArray(String... array) {
		int size = 0;
		for (String string : array) {
			size += string.length();
		}

		StringBuilder sb = new StringBuilder(size);

		for (String string : array) {
			sb.append(string);
		}

		return sb.toString();
	}

	public static String inputStreamToString(InputStream in) {
		try {
			return IOUtils.toString(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 判断字符串是否全是数字
	 * 
	 * @param str
	 * @return 返回true，否则false
	 */
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 去掉开头和结尾的"[]"
	 * 
	 * @param list
	 * @return
	 */
	public static String toString(List<?> list) {
		String v = list.toString();
		return v.substring(1, v.length() - 1);
	}

	/**
	 * 去掉开头和结尾的"[]"
	 * 
	 * @param list
	 * @return
	 */
	public static String toString(Object[] list) {
		return toString(Arrays.asList(list));
	}

	/**
	 * 字符串转大写
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpper(String s) {
		if (StringUtil.isEmptyOrNull(s)) {
			return s;
		}
		return s.toUpperCase();
	}
}
