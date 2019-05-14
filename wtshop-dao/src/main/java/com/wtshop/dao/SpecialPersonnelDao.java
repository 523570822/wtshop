package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.model.Brand;
import com.wtshop.model.ProductCategory;
import com.wtshop.model.SpecialPersonnel;

import java.util.List;

/**
 * Dao - 品牌
 * 
 * 
 */
public class SpecialPersonnelDao extends OrderEntity<SpecialPersonnel> {

	/**
	 * 构造方法
	 */
	public SpecialPersonnelDao() {
		super(SpecialPersonnel.class);
	}

	/**
	 * 查询品牌列表
	 */
	public List<SpecialPersonnel> findBrandList(){

		String sql = " select id , name from brand where is_delete='0' order by name";
		return modelManager.find(sql);

	}

	/**
	 * 查询品牌列表
	 */
	public List<Record> findBrandSort(){

		String sql = " select id , name from brand order by name";
		return Db.find(sql);

	}


	/**
	 * 查找品牌
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 品牌
	 */
	public List<SpecialPersonnel> findList(ProductCategory productCategory, Integer count, List<Filter> filters, List<Order> orders) {
		String sql = "SELECT * FROM brand WHERE 1 = 1 ";
		if (productCategory != null) {
			sql = "SELECT b.* FROM product_category_brand pcb INNER JOIN brand b ON pcb.`brands`= b.`id` WHERE pcb.`product_categories` = " + productCategory.getId() + " ";
		}
		return super.findList(sql, null, count, filters, orders);
	}

    public Boolean findSpByPhone(String phone) {
		String sql ="select * from special_personnel s where s.phone ="+phone+" and s.status=1 ";
		SpecialPersonnel ssss = super.findBySql(sql);
		Boolean bool=false;
		if(ssss==null){
			bool=false;
		}else{
			bool=true;
		}
		return  bool;
    }
}