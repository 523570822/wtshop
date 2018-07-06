package com.wtshop.dao;

import com.wtshop.model.OrderLog;

/**
 * Dao - 订单记录
 * 
 * 
 */
public class OrderLogDao extends BaseDao<OrderLog> {
	
	/**
	 * 构造方法
	 */
	public OrderLogDao() {
		super(OrderLog.class);
	}
}