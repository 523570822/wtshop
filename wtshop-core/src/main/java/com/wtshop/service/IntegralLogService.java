package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.PointLogDao;
import com.wtshop.model.IntegralLog;
import com.wtshop.model.Member;
import com.wtshop.model.PointLog;

/**
 * Service - 积分记录
 * 
 * 
 */
public class IntegralLogService extends BaseService<IntegralLog> {

	/**
	 * 构造方法
	 */
	public IntegralLogService() {
		super(IntegralLog.class);
	}
	
	private PointLogDao pointLogDao = Enhancer.enhance(PointLogDao.class);
	
	/**
	 * 查找积分记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	public Page<PointLog> findPage(Member member, Pageable pageable) {
		return pointLogDao.findPage(member, pageable);
	}


	/**
	 * 根据会员id 获取首次赠送记录
	 */
	public PointLog findLogByMemberId(Long memberId){
		return  pointLogDao.findLogByMemberId(memberId);
	}

}