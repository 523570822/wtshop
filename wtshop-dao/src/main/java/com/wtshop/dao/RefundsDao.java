package com.wtshop.dao;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.Refunds;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Dao - 退款单
 * 
 * 
 */
public class RefundsDao extends BaseDao<Refunds> {
	
	/**
	 * 构造方法
	 */
	public RefundsDao() {
		super(Refunds.class);
	}


	/**
	 * 查询是否存在退款单
	 */
	public Refunds findByReturnsId(Long returnsId){
		if(returnsId == null){
			return null;
		}else {
			String sql = " SELECT * FROM refunds WHERE returns_id = " + returnsId;
			return modelManager.findFirst(sql);
		}
	}



	/**
	 * 查找退款单
	 */
	public Page<Record> findPages(Pageable pageable, String order, String returns, String sn){

		String sql = " from refunds r left JOIN `order` o on r.order_id = o.id LEFT JOIN `returns` t ON r.returns_id = t.id  WHERE 1 =1 ";


		String select = "select r.id,r.balance_return,r.sn,o.sn order_sn,t.sn returns_sn,r.create_date ";

		if(order  != null){
			sql += " and o.sn LIKE '%" + order +"%' " ;
		}
		if(returns != null){
			sql += " and t.sn LIKE '%" + returns +"%' " ;
		}
		if(sn != null){
			sql += " and r.sn LIKE '%" + sn +"%' " ;
		}


		// 排序属性、方向
		String ordersSQL = getOrders(pageable.getOrders());
		String orderProperty = pageable.getOrderProperty();
		Order.Direction orderDirection = pageable.getOrderDirection();
		if (StringUtils.isNotEmpty(orderProperty) && orderDirection != null) {
			switch (orderDirection) {
				case asc:
					sql += " ORDER BY " + orderProperty + " ASC ";
					break;
				case desc:
					sql += " ORDER BY " + orderProperty + " DESC ";
					break;
			}
		} else if (StrKit.isBlank(ordersSQL)) {
			ordersSQL = "ORDER BY r.create_date DESC";
		}
		sql += ordersSQL;

		return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);


	}

	/**
	 * 转换为Order
	 *
	 *
	 *            Root
	 * @param orders
	 *            排序
	 * @return Order
	 */
	private String getOrders(List<Order> orders) {
		String orderSql = "";
		if (CollectionUtils.isNotEmpty(orders)) {
			orderSql = " ORDER BY ";
			for (Order order : orders) {
				String property = order.getProperty();
				Order.Direction direction = order.getDirection();
				switch (direction) {
					case asc:
						orderSql += property + " ASC, ";
						break;
					case desc:
						orderSql += property + " DESC,";
						break;
				}
			}
			orderSql = StringUtils.substring(orderSql, 0, orderSql.length() - 1);
		}
		return orderSql;
	}
}