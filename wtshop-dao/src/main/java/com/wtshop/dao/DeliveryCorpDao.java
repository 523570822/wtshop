package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.DeliveryCorp;

/**
 * Dao - 物流公司
 * 
 * 
 */
public class DeliveryCorpDao extends OrderEntity<DeliveryCorp> {
	
	/**
	 * 构造方法
	 */
	public DeliveryCorpDao() {
		super(DeliveryCorp.class);
	}


	public Page<DeliveryCorp> findPageBySearch(Pageable pageable){

		String sql = " from delivery_corp  where 1 = 1  ";
		if( pageable.getSearchValue() != null){
			sql += " AND name like '%" + pageable.getSearchValue() + "%'";
		}

		String select = "SELECT * ";

		sql +=  " ORDER BY orders DESC";

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

	}
	
}