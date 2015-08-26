package com.apusic.adxp.agent.netty.adxp.util;

public class StringUtil {

	public static String join(String[] array, String regex) {
		StringBuffer sb = new StringBuffer();
		for (String item : array) {
			sb.append(item);
			sb.append(regex);
		}
		return sb.toString().substring(0,sb.length()-regex.length());
	}
}
