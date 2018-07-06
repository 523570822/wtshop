package com.wtshop;

/**
 * 公共参数
 * 
 * 
 */
public final class CommonAttributes {

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** shopxx.xml文件路径 */
	public static final String wtshop_XML_PATH = "/wtshop.xml";

	/** wtshop.properties文件路径 */
	public static final String wtshop_PROPERTIES_PATH = "/wtshop.properties";
	
	/** 主题占位符 */
	public static final String THEME_NAME = "${theme}";

	/**
	 * 不可实例化
	 */
	private CommonAttributes() {
	}

}