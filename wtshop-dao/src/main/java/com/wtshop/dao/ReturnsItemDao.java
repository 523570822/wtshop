package com.wtshop.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.wtshop.model.Member;
import com.wtshop.model.OrderItem;
import com.wtshop.model.ReturnsItem;

/**
 * Dao - 退货单项
 * 
 * 
 */
public class ReturnsItemDao extends BaseDao<ReturnsItem> {

	/**
	 * 构造方法
	 */
	public ReturnsItemDao() {
		super(ReturnsItem.class);
	}

	/**
	 * 根据订单号 商品名称搜索订单
	 */
	public List<ReturnsItem> findReturnsItems(Long id){
		if(id == null){
			return null;
		}else {
			String sql = "SELECT g.image,i.`name`,i.quantity , i.id from `returns` r LEFT JOIN returns_item i on r.id = i.return_id LEFT JOIN product p on i.product_id = p.id LEFT JOIN goods g on p.goods_id = g.id  where r.id= " + id ;
			return modelManager.find(sql);

		}
	}



	/**
	 * 根据订单号 商品名称搜索订单
	 */
	public List<ReturnsItem> findReturnsItems(String name , String sn ,Member member){
		if(name == null && sn ==null){
			return null;
		}else {
			String sql = "SELECT i.create_date , i.quantity , i.status ,r.sn ,i.id ,i.product_id FROM `returns` r LEFT JOIN returns_item i on r.id = i.return_id  WHERE r.is_delete = 0 " +
					" AND i.member_id = " + member.getId() +
					" AND (r.sn LIKE '%" + sn +"%' " +
					" OR r.sn LIKE '%" + name +"%' " +
					" OR i.`name` LIKE '%" + name +"%' " +
					" OR i.`name` LIKE '%" + sn +"%') ";
			return modelManager.find(sql);

		}
	}



	/**
	 * 退货单项
	 *
	 * @param member
	 * @return 退货单项，若不存在则返回null
	 */
	public List<ReturnsItem> findReturnsItems(Member member, Integer status) {
		if (member == null) {
			return null;
		}
		try {
			String sql = "SELECT i.create_date , i.quantity , i.status ,r.sn ,i.id ,i.product_id FROM returns_item i left join returns r on i.return_id = r.id  WHERE i.member_id = ? ";
			if (status == ReturnsItem.Status.refund.ordinal()) {
				sql += " AND i.status = " + ReturnsItem.Status.refund.ordinal();
			}
			sql += " order by i.status asc, i.create_date desc";
			return modelManager.find(sql, member.getId());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据sn查找退货项
	 */
	public ReturnsItem findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		try {
			String sql = "SELECT * FROM returns_item WHERE sn = ? ";
			return modelManager.findFirst(sql, sn);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 根据sn查找退货项
	 */
	public List<ReturnsItem> findByReturnId(Long return_id) {
		if (return_id == null) {
			return null;
		}
		try {
			String sql = "SELECT i.quantity,g.image,i.name,i.id ,i.amount,i.product_id,g.id goods_id,i.special_goods_id FROM returns_item i left join product p on i.product_id = p.id left join goods g on p.goods_id = g.id WHERE return_id = ? ";
			return modelManager.find(sql, return_id);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 获取金额
	 */
	public ReturnsItem findMoneyById(Long return_id) {
		if (return_id == null) {
			return null;
		}
		try {
			String sql = "SELECT amount from returns_item  WHERE return_id = " + return_id;
			return modelManager.findFirst(sql);
		} catch (Exception e) {
			return null;
		}

	}


}
	

