package com.wtshop.dao;

import com.wtshop.model.Specification;

/**
 * Dao - 规格
 * 
 * 
 */
public class SpecificationDao extends OrderEntity<Specification> {
	
	/**
	 * 构造方法
	 */
	public SpecificationDao() {
		super(Specification.class);
	}
}