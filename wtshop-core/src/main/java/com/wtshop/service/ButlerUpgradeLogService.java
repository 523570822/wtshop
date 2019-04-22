package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.dao.ButlerUpgradeLogDao;
import com.wtshop.dao.CartItemDao;
import com.wtshop.model.ButlerUpgradeLog;
import com.wtshop.model.Member;

import java.util.List;


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
	private ButlerUpgradeLogDao butlerUpgradeLogDao = Enhancer.enhance(ButlerUpgradeLogDao.class);
	private ButlerUpgradeLog logDao = Enhancer.enhance(ButlerUpgradeLog.class);


    public List<ButlerUpgradeLog> findByMemberId(Long id) {
		return butlerUpgradeLogDao.findByMemberId(id);
    }
}