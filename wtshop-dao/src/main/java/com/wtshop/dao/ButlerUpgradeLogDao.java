package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.wtshop.model.Log;

/**
 * Dao - 日志
 * 
 * 
 */
public class ButlerUpgradeLogDao extends BaseDao<Log> {

	/**
	 * 构造方法
	 */
	public ButlerUpgradeLogDao() {
		super(Log.class);
	}
	
	/**
	 * 删除所有日志
	 */
	public void removeAll() {
		String sql = "DELETE FROM log";
		Db.update(sql);
	}

}