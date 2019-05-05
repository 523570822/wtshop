package com.wtshop.dao;

import com.wtshop.model.Account;
import com.wtshop.model.Specification;

import java.util.List;

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


	/**
	 *  获取用户信息集合
	 */
	public List<Specification> findByCategoryId(Long categoryId){

		String sql = "select * from specification s where 1=1 ";
		if(categoryId != null){

			sql += " AND s.product_category_id= "+ categoryId;

		}
		return modelManager.find(sql);

	}

    public List<Specification> findByName(Long id,String name,Long cdId) {
		String sql = "select * from specification s where 1=1 ";
		if(name != null){

			sql += " AND s.name= '"+ name+"'";

		}
		if(id != null){

			sql += " AND s.id<>"+ id+"";

		}if(cdId != null){

			sql += " AND s.product_category_id="+ cdId+"";

		}
		return modelManager.find(sql);
    }
}