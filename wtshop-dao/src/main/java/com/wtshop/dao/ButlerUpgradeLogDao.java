package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.wtshop.model.ButlerUpgradeLog;
import com.wtshop.model.Log;
import com.wtshop.model.Member;

import java.util.List;

/**
 * Dao - 日志
 * 
 * 
 */
public class ButlerUpgradeLogDao extends BaseDao<ButlerUpgradeLog> {

	/**
	 * 构造方法
	 */
	public ButlerUpgradeLogDao() {
		super(ButlerUpgradeLog.class);
	}
	
	/**
	 * 删除所有日志
	 */
	public void removeAll() {
		String sql = "DELETE FROM log";
		Db.update(sql);
	}

    public List<ButlerUpgradeLog> findByMemberId(Long id) {
		String sql = " SELECT f.* FROM butler_upgrade_log f  \n" +
				" where 1 = 1 AND member_id = "+id+" ";
		return modelManager.find(sql);
    }
}