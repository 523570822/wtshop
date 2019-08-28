package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.dao.IntegralLogDao;
import com.wtshop.dao.IntegralStoreLogDao;
import com.wtshop.model.IntegralLog;
import com.wtshop.model.IntegralStoreLog;
import com.wtshop.model.Member;

/**
 * Service - 积分记录
 * 
 * 
 */
public class IntegralStoreLogService extends BaseService<IntegralStoreLog> {

	/**
	 * 构造方法
	 */
	public IntegralStoreLogService() {
		super(IntegralStoreLog.class);
	}
	
	private IntegralStoreLogDao integralLogDao = Enhancer.enhance(IntegralStoreLogDao.class);
	
	/**
	 * 查找积分记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	public Page<IntegralStoreLog> findPage(Member member, Pageable pageable) {
		return integralLogDao.findPage(member, pageable);
	}

	/**
	 * 喵币分页
	 * @param pageable
	 * @param type
	 * @return
	 */
	public Page<Record> findPages(Pageable pageable , Integer type){
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		String name = null;
		if("name".equals(searchProperty)){
			name = searchValue;
		}
		return integralLogDao.findPages(pageable, name ,type);
	}
	/**
	 * 根据会员id 获取首次赠送记录
	 */
	public IntegralStoreLog findLogByMemberId(Long memberId){
		return  integralLogDao.findLogByMemberId(memberId);
	}

}