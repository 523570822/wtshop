package com.wtshop.shiro.core;

import java.util.Map;

import com.wtshop.shiro.core.handler.AuthzHandler;

/**
 * Created by wangrenhui on 14-1-7.
 */
public interface JdbcAuthzService {
	public Map<String, AuthzHandler> getJdbcAuthz();
}
