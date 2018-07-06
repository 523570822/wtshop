package com.wtshop.dao;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.CartItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.wtshop.model.Cart;
import com.wtshop.util.DateUtils;

/**
 * Dao - 购物车
 * 
 * 
 */
public class CartDao extends BaseDao<Cart> {
	
	/**
	 * 构造方法
	 */
	public CartDao() {
		super(Cart.class);
	}



	/**
	 * 购物车商品列表
	 */

	public Page<Record> findCartGoods(Pageable pageable, String id, String name ){
		String sql = " FROM cart_item i LEFT JOIN cart c on i.cart_id = c.id LEFT JOIN product p ON i.product_id = p.id LEFT JOIN goods g ON p.goods_id = g.id WHERE 1 = 1   ";
		if( name != null ){
			sql += "AND g.`name` LIKE '%" + name +"%' " ;
		}
		if( id != null ){
			sql += "AND g.id LIKE '%" + id +"%' " ;
		}

		String select = "SELECT g.id, g.`name` ,p.stock ,COUNT(*) number ,p.id productId ";

		sql += " GROUP BY i.product_id ";

	//	sql += " GROUP BY i.product_id ORDER BY number DESC";

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
			ordersSQL = "ORDER BY number DESC";
		}

		sql += ordersSQL;

		return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
	}


	/**
	 * 购物车中商品所在会员的详情
	 */
	public Page<Record> findCartGoodsMember(Pageable pageable ,Long productId){
		String sql = "  from cart_item i LEFT JOIN cart c ON i.cart_id = c.id LEFT JOIN member m ON c.member_id = m.id WHERE 1 =1 ";
		String select = "SELECT m.id, m.phone, m.nickname,i.create_date ";
		if( productId != null){
			sql += " AND i.product_id = " + productId;
		}
		sql += " ORDER BY i.create_date ";
		return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
	}


	/**
	 * 购物车用户列表
	 */
	public Page<Record> findCartMember(Pageable pageable, String id, String name, String phone){

		String sql = " from cart_item i LEFT JOIN cart c ON i.cart_id = c.id LEFT JOIN member m ON c.member_id = m.id WHERE 1 =1 and m.id is NOT NULL ";
		if( name != null ){
			sql += "AND m.nickname LIKE '%" + name +"%' " ;
		}
		if( id != null ){
			sql += "AND m.id LIKE '%" + id +"%' " ;
		}
		if( phone != null ){
			sql += "AND m.phone LIKE '%" + phone +"%' " ;
		}

		String select = "SELECT m.id, m.phone, m.nickname, COUNT(*) number ";

		sql += " GROUP BY i.cart_id ";

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
			ordersSQL = "ORDER BY number DESC";
		}
		sql += ordersSQL;

		return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);


	}

	/**
	 * 购物车详情
	 */
	public Page<Record> findCartMemberMessage(Pageable pageable ,Long memberId){
		String sql = "  FROM cart_item i LEFT JOIN cart c on i.cart_id = c.id LEFT JOIN product p ON i.product_id = p.id LEFT JOIN goods g ON p.goods_id = g.id WHERE 1 = 1 ";
		String select = "SELECT g.id, g.`name` ,i.quantity number , i.create_date ";
		if( memberId != null){
			sql += " AND c.member_id = " + memberId;
		}
		sql += " ORDER BY i.create_date ";
		return Db.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);
	}



	/**
	 * 根据密钥查找购物车
	 * 
	 * @param key
	 *            密钥
	 * @return 购物车，若不存在则返回null
	 */
	public Cart findByKey(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		try {
			String sql = "SELECT * FROM cart WHERE cart_key = ?";
			return modelManager.findFirst(sql, key);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 查找购物车
	 * 
	 * @param hasExpired
	 *            是否已过期
	 * @param count
	 *            数量
	 * @return 购物车
	 */
	public List<Cart> findList(Boolean hasExpired, Integer count) {
		String sql = "SELECT * FROM cart WHERE 1 = 1";
		if (hasExpired != null) {
			if (hasExpired) {
				sql += " AND expire IS NOT NULL AND expire <= '" + DateUtils.getDateTime() + "'";
			} else {
				sql += " AND expire IS NULL OR expire > '" + DateUtils.getDateTime() + "'";
			}
		}
		return super.findList(sql, null, count, null, null);
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