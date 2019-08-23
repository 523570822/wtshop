package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.IntegralLog;
import com.wtshop.model.Member;
import com.wtshop.model.PointLog;

/**
 * Dao - 积分记录
 * 
 * 
 */
public class IntegralLogDao extends BaseDao<IntegralLog> {

	/**
	 * 构造方法
	 */
	public IntegralLogDao() {
		super(IntegralLog.class);
	}
	
	/**
	 * 查找积分记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	public Page<IntegralLog> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return null;
		}
		String sqlExceptSelect = "FROM integral_log WHERE member_id = " + member.getId();
		return super.findPage(sqlExceptSelect, pageable);
	}

	public IntegralLog findLogByMemberId(Long memberId){
		if (memberId == null) {
			return null;
		}
		String sql = " select * from integral_log where type = 1 and member_id = " + memberId;
        return modelManager.findFirst(sql);
	}

}