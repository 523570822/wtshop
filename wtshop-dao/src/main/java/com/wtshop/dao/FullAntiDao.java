package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Order;
import com.wtshop.model.FullAnti;
import com.wtshop.model.MemberRank;
import com.wtshop.model.ProductCategory;
import com.wtshop.util.DateUtils;

import java.util.List;

/**
 * Dao - 促销
 * 
 * 
 */
public class FullAntiDao extends OrderEntity<FullAnti> {

	/**
	 * 构造方法
	 */
	public FullAntiDao() {
		super(FullAnti.class);
	}
	
	/**
	 * 查找促销
	 * 
	 * @param memberRank
	 *            会员等级
	 * @param productCategory
	 *            商品分类
	 * @param hasBegun
	 *            是否已开始
	 * @param hasEnded
	 *            是否已结束
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 促销
	 */
	public List<FullAnti> findList(MemberRank memberRank, ProductCategory productCategory, Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<Order> orders) {
		String sql = "SELECT * FROM promotion WHERE 1 = 1 ";
		if (memberRank != null) {
			sql = "SELECT p.* FROM promotion p left JOIN promotion_member_rank r ON p.id = r.promotions WHERE r.member_ranks = " + memberRank.getId();
		}
		if (productCategory != null) {
			sql = "SELECT p.* FROM promotion p left JOIN product_category_promotion c ON p.id = c.promotions WHERE c.product_categories = " + productCategory.getId();
		}
		if (hasBegun != null) {
			if (hasBegun) {
				sql += " AND (begin_date IS NULL OR begin_date <= '" + DateUtils.getNowTime()+ "')";
			} else {
				sql += " AND (begin_date IS NOT NULL OR begin_date > '" + DateUtils.getNowTime()+ "')";
			}
		}
		if (hasEnded != null) {
			if (hasEnded) {
				sql += " AND (end_date IS NULL OR end_date <= '" + DateUtils.getNowTime()+ "')";
			} else {
				sql += " AND (end_date IS NULL OR end_date > '" + DateUtils.getNowTime()+ "')";
			}
		}
		return super.findList(sql, null, count, filters, orders);
	}


	/**
	 * 根据goodsId查找返现金额
	 */

	public FullAnti findProm(Long goodsId){

		if(goodsId == null){
			return  null;
		}else {

			String sql = " SELECT * FROM goods_promotion g LEFT JOIN promotion p on g.promotions = p.id where 1 = 1 AND g.promotions =7  AND g.goods = " + goodsId;
            return modelManager.findFirst(sql);

		}


	}
	public List<Record>	findTotalMoney(){
		String sql = "select fr.total_money  from  full_anti fr GROUP BY fr.total_money  " ;
		return Db.find(sql);
	}
	public List<FullAnti>	findTotalMoneyList(Object totalMoney){
		if(totalMoney == null){
			return  null;
		}else {
			String sql = "select *  from  full_anti fr where fr.total_money="+totalMoney+"  " ;
			return modelManager.find(sql);
		}


	}
}