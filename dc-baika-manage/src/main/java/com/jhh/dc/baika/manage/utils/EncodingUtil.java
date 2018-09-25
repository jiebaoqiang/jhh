package com.jhh.dc.baika.manage.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public abstract class EncodingUtil {

	@SuppressWarnings("all")
	/**
	 * @see sun.io.CharacterEncoding 
	 */
	public static final String ENCODING_GB18030 = "GB18030";
	public static final String ENCODING_GBK = "GBK";
	public static final String ENCODING_GB2312 = "GB2312";
	public static final String ENCODING_EUC_CN = "EUC_CN";
	public static final String ENCODING_ISO8859_1 = "ISO8859_1";
	public static final String ENCODING_UTF8 = "UTF-8";
	
	public static boolean isWindowsPlatform() {
		return "\\".equals(File.separator);
	}
	
	public static boolean isLinux() {
		String osName = System.getProperty("os.name");
		return StringUtils.containsIgnoreCase(osName, "Linux");
	}
	
	public static boolean isSimplfiedChineseEncoding(String encoding) {
		return ENCODING_GB18030.equals(encoding) || //
				ENCODING_GBK.equals(encoding) || //
				ENCODING_GB2312.equals(encoding) || //
				ENCODING_EUC_CN.equals(encoding);
	}
	
	public static String getSystemEncoding() {
		String fileEncoding = System.getProperty("file.encoding");
		fileEncoding = fileEncoding != null ? fileEncoding.toUpperCase() : null;
		if (isWindowsPlatform()) {
			if (ENCODING_UTF8.equals(fileEncoding)) {
				// Windows上如果编码是UTF-8，需要转成GB2312，否则压缩会有乱码
				return ENCODING_GB2312;
			} else {
				// Windows上非UTF-8，用系统默认的编码
				return null;
			}
		} else {
			if (isLinux()) {
				//Linux
				return ENCODING_GB2312;
			}
			// Unix，用系统默认的编码
			return null;
		}
	}
}
