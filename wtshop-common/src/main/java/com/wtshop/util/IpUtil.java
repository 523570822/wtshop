package com.wtshop.util;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StrKit;

import java.io.IOException;

/**
 * 类功能说明 TODO:IP工具类
 * 类修改者	修改日期
 * 修改说明
 */
public class IpUtil {

	/**
	 * 获取访问用户的客户端IP（适用于公网与局域网）.
	 */
	public static final String getIpAddr(final HttpServletRequest request) {
	    String ipString = request.getHeader("x-forwarded-for");
	    if (StrKit.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
	        ipString = request.getHeader("Proxy-Client-IP");
	    }
	    if (StrKit.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
	        ipString = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (StrKit.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
	        ipString = request.getRemoteAddr();
	    }
	 
	    // 多个路由时，取第一个非unknown的ip
	    final String[] arr = ipString.split(",");
	    for (final String str : arr) {
	        if (!"unknown".equalsIgnoreCase(str)) {
	            ipString = str;
	            break;
	        }
	    }
	 
	    return ipString;
	}

	/**
	 * 获取id地址
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getIpAddress(final HttpServletRequest request) throws IOException {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}
}
