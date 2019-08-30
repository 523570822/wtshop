package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.dao.IntegralStoreDao;
import com.wtshop.model.IntegralStore;
import com.wtshop.model.Member;

import java.util.List;

/**
 * Service - 积分记录
 * 
 * 
 */
public class IntegralStoreService extends BaseService<IntegralStore> {

	/**
	 * 构造方法
	 */
	public IntegralStoreService() {
		super(IntegralStore.class);
	}
	
	private IntegralStoreDao integralLogDao = Enhancer.enhance(IntegralStoreDao.class);
	
	/**
	 * 查找积分记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	public Page<IntegralStore> findPage(Member member, Pageable pageable) {
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
	public List<IntegralStore> findLogByMemberId(Long memberId){
		return  integralLogDao.findLogByMemberId(memberId);
	}
	/**
	 * 根据会员id 和门店ID判断是否已经有该门店记录
	 */
	public IntegralStore findStoreByMemberId(Long memberId,Long storeId){
		return  integralLogDao.findStoreByMemberId(memberId,storeId);
	}
}