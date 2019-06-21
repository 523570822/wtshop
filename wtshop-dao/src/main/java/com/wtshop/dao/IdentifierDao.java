package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.Brand;
import com.wtshop.model.Identifier;
import com.wtshop.model.ProductCategory;
import com.wtshop.model.SpecialPersonnel;

import java.util.List;

/**
 * Dao - 品牌
 * 
 * 
 */
public class IdentifierDao extends   BaseDao<Identifier> {

	/**
	 * 构造方法
	 */
	public IdentifierDao() {
		super(Identifier.class);
	}

	/**
	 * 查询品牌列表
	 */
	public List<Record> findBrandSort(){

		String sql = " select id , name from brand order by name";
		return Db.find(sql);

	}
}