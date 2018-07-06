package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.LogDao;
import com.wtshop.model.Log;


/**
 * Service - 日志
 * 
 * 
 */
public class LogService extends BaseService<Log> {

	/**
	 * 构造方法
	 */
	public LogService() {
		super(Log.class);
	}
	
	private LogDao logDao = Enhancer.enhance(LogDao.class);
	
	/**
	 * 清空日志
	 */
	public void clear() {
		logDao.removeAll();
	}

}