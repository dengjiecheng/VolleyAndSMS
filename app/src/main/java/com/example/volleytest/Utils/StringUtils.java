/**
 * Copyright 2014 Zhenguo Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.volleytest.Utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包 结合android.text.TextUtils使用
 * 
 * @author jingle1267@163.com
 */
public final class StringUtils {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private StringUtils() {
		throw new Error("Do not need instantiate!");
	}

	/**
	 * 字符串小7为前面补0
	 * 
	 * @param alias
	 */
	public static String appendZero(String alias) {
		StringBuffer sb = new StringBuffer();
		if (alias != null && alias.length() > 0) {
			alias = alias.trim();
			int aliasLeng = alias.length();
			for (int i = 0; i < 7 - aliasLeng; i++) {
				sb.append(0);
			}
			sb.append(alias);
		}
		return sb.toString();
	}

	/**
	 * Returns true if the string is null or 0-length.
	 * 
	 * @param str
	 *            the string to be examined
	 * @return true if str is null or zero length
	 */
	public static boolean isEmpty(CharSequence str) {
		return TextUtils.isEmpty(str);
	}

	public static boolean isNotEmpty(CharSequence str) {
		return !TextUtils.isEmpty(str);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param text
	 * @return true null false !null
	 */
	public static boolean isNull(String text) {
		if (TextUtils.isEmpty(text) || "".equals(text.trim())
				|| "null".equals(text))
			return true;
		return false;
	}

	public static boolean isNotNull(String text) {
		return !isNull(text);
	}

	public static boolean haveDatas(String data) {
		return data != null && !data.equals("") && data.length() > 0;
	}

	/**
	 * byte[]数组转换为16进制的字符串
	 * 
	 * @param data
	 *            要转换的字节数组
	 * @return 转换后的结果
	 */
	public static final String byteArrayToHexString(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length * 2);
		for (byte b : data) {
			int v = b & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase(Locale.getDefault());
	}

	/**
	 * 16进制表示的字符串转换为字节数组
	 * 
	 * @param s
	 *            16进制表示的字符串
	 * @return byte[] 字节数组
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] d = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return d;
	}

	/**
	 * 将给定的字符串中所有给定的关键字标红
	 * 
	 * @param sourceString
	 *            给定的字符串
	 * @param keyword
	 *            给定的关键字
	 * @return 返回的是带Html标签的字符串，在使用时要通过Html.fromHtml()转换为Spanned对象再传递给TextView对象
	 */
	public static String keywordMadeRed(String sourceString, String keyword) {
		String result = "";
		if (sourceString != null && !"".equals(sourceString.trim())) {
			if (keyword != null && !"".equals(keyword.trim())) {
				result = sourceString.replaceAll(keyword,
						"<font color=\"red\">" + keyword + "</font>");
			} else {
				result = sourceString;
			}
		}
		return result;
	}

	public static String convertToUinicode(String str) {
		str = (str == null ? "" : str);
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8); // 取出�?�?
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			j = (c & 0xFF); // 取出�?�?
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);

		}
		return (new String(sb));
	}

	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	public static boolean isJsonNull(String object) {
		return object == null || "".equals(object) || "[]".equals(object)
				|| "{}".equals(object) || "null".equals(object);
	}

	public static boolean isSoapObjectNull(Object response) {
		if (response == null) {
			return true;
		}
		String object = response.toString();

		return object == null || "".equals(object) || "anyType{}".equals(object)
				|| "{}".equals(object) || "null".equals(object);
	}

	public static String replaceNullValue(Object response) {

		String object = response.toString();

		if (object == null || "".equals(object) || "anyType{}".equals(object)
				|| "{}".equals(object) || "null".equals(object)) {
			return "";
		}
		return object;
	}

	public static String replaceNullValue(JSONObject object, String key)
			throws JSONException {

		if (object == null || !object.has(key)) {
			return "";
		}

		return object.get(key).toString();
	}

	public static long Object2Long(Object object) {
		String tmp = String.valueOf(object);
		tmp = replaceNullValue(tmp);

		if (tmp.equals("")) {
			return 0;
		}
		return Long.valueOf(tmp);
	}

	public static float Object2Float(Object object) {
		String tmp = String.valueOf(object);
		tmp = replaceNullValue(tmp);
		if (tmp.equals("")) {
			return 0.0f;
		}
		return Float.valueOf(tmp);
	}

	public static int Object2Int(Object object) {
		String tmp = String.valueOf(object);
		tmp = replaceNullValue(tmp);

		if (tmp.equals("")) {
			return 0;
		}
		return Integer.valueOf(tmp);
	}

	public static boolean Object2Boolean(Object object) {
		String tmp = String.valueOf(object);
		tmp = replaceNullValue(tmp);

		if (tmp.equals("")) {
			return false;
		}
		return Boolean.valueOf(tmp);
	}

	public static String Object2String(Object object) {
		String tmp = String.valueOf(object);
		tmp = replaceNullValue(tmp);

		return tmp;
	}

	/***
	 * 查找字符串中是否有匹配正则表达式的字符/字符串
	 * 
	 * @param regEx
	 *            "综合办公室",
	 * @param compareWord
	 *            "关综合办公室所有灯";
	 * @return
	 */
	public static boolean isMatchString(String regEx, String compareWord) {
		if (StringUtils.isEmpty(regEx)) {
			if (StringUtils.isEmpty(compareWord)) {
				return true;
			}
			return false;
		}
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx,
		// Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(compareWord);
		// 查找字符串中是否有匹配正则表达式的字符/字符串
		return matcher.find();
	}

	/***
	 * 判断一个字符串是否都为数字
	 * 
	 * @param strNum
	 * @return
	 */

	public static boolean isDigit(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	/***
	 * 截取阿拉伯数字
	 * 
	 * @param content
	 * @return
	 */
	public static String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/***
	 * 截取中文的数字
	 * 
	 * @param content
	 * @return
	 */
	public static String getFirstGroupChineseNumbers(String content) {
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("(千|百|十|九|八|七|六|五|四|三|二|一|零){1,}");
		matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/***
	 * 截取中文的数字
	 * 
	 * @param content
	 * @return
	 */
	public static List<String> getChineseNumbers(String content) {
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("(千|百|十|九|八|七|六|五|四|三|二|一|零){1,}");
		matcher = pattern.matcher(content);
		List<String> result = new ArrayList<String>();
		while (matcher.find()) {
			result.add(matcher.group(0));
		}
		return result;
	}

	/***
	 * 判断是否全为中文数字
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isChineseNumbers(String content) {
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("^(千|百|十|九|八|七|六|五|四|三|二|一|零){1,}$");
		matcher = pattern.matcher("十九");
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * 判断一个字符串含有多少组阿拉伯数字
	 * 
	 * @param content
	 * @return
	 */
	public static int getNumberSize(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		int i = 0;
		while (matcher.find()) {
			i++;
		}
		return i;
	}

	/***
	 * 判断一个字符串含有多少组中文数字
	 * 
	 * @param content
	 * @return
	 */
	public static int getChineseNumberSize(String content) {
		Pattern pattern = Pattern.compile("(千|百|十|九|八|七|六|五|四|三|二|一|零){1,}");
		Matcher matcher = pattern.matcher(content);
		int i = 0;
		while (matcher.find()) {
			i++;
		}
		return i;
	}

	/***
	 * 截取非数字
	 * 
	 * @param content
	 * @return
	 */
	public static String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/***
	 * 判断一个字符串是否含有数字(包括中文数字和阿拉伯数字)
	 * 
	 * @param content
	 * @return
	 */

	public static boolean hasDigit(String content) {
		Pattern p = Pattern.compile(".*\\d+.*");

		Matcher m = p.matcher(content);

		if (m.matches())
			return true;

		p = Pattern.compile(".*(千|百|十|九|八|七|六|五|四|三|二|一|零){1,}.*");
		m = p.matcher(content);
		if (m.matches())
			return true;

		return false;

	}
}
