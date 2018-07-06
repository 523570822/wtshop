package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.dao.StockLogDao;
import com.wtshop.model.StockLog;

/**
 * Service - 库存记录
 * 
 * 
 */
public class StockLogService extends BaseService<StockLog> {

	/**
	 * 构造方法
	 */
	public StockLogService() {
		super(StockLog.class);
	}
	
	private StockLogDao stockLogDao = Enhancer.enhance(StockLogDao.class);
	
	/**
	 * 查找实体对象分页
	 * 
	 * @param pageable
	 *            分页信息
	 * @return 实体对象分页
	 */
	public Page<StockLog> findPageList(Pageable pageable) {

		if( "name".equals(pageable.getSearchProperty()) ){
			pageable.setSearchProperty("g.name");
		}
		if( "sn".equals(pageable.getSearchProperty()) ){
			pageable.setSearchProperty("p.sn");
		}
		return stockLogDao.findPage(pageable);
	}
}