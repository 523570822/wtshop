package com.wtshop.dao;

import com.wtshop.model.ShippingMethod;

import java.util.List;


/**
 * Dao - 配送方式
 * 
 * 
 */
public class ShippingMethodDao extends OrderEntity<ShippingMethod> {
	
	/**
	 * 构造方法
	 */
	public ShippingMethodDao() {
		super(ShippingMethod.class);
	}


	/**
	 * 获取所有物流信息
	 */
	public List<ShippingMethod> findMethodList(){

		String sql = " select * from  shipping_method where 1 = 1 order by orders";
		return modelManager.find(sql);
	}
}