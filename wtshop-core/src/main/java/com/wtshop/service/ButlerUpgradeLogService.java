package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.model.ButlerUpgradeLog;


/**
 * Service - 日志
 * 
 * 
 */
public class ButlerUpgradeLogService extends BaseService<ButlerUpgradeLog> {

	/**
	 * 构造方法
	 */
	public ButlerUpgradeLogService() {
		super(ButlerUpgradeLog.class);
	}
	
	private ButlerUpgradeLog logDao = Enhancer.enhance(ButlerUpgradeLog.class);


}