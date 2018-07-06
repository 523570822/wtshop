package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Member;
import com.wtshop.model.PointLog;

/**
 * Dao - 积分记录
 * 
 * 
 */
public class PointLogDao extends BaseDao<PointLog> {
	
	/**
	 * 构造方法
	 */
	public PointLogDao() {
		super(PointLog.class);
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
	public Page<PointLog> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return null;
		}
		String sqlExceptSelect = "FROM point_log WHERE member_id = " + member.getId();
		return super.findPage(sqlExceptSelect, pageable);
	}

	public PointLog findLogByMemberId(Long memberId){
		if (memberId == null) {
			return null;
		}
		String sql = " select * from point_log where type = 1 and member_id = " + memberId;
        return modelManager.findFirst(sql);
	}

}