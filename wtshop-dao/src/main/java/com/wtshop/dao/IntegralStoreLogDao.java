package com.wtshop.dao;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.IntegralStoreLog;
import com.wtshop.model.Member;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Dao - 积分记录
 * 
 * 
 */
public class IntegralStoreLogDao extends BaseDao<IntegralStoreLog> {

	/**
	 * 构造方法
	 */
	public IntegralStoreLogDao() {
		super(IntegralStoreLog.class);
	}
	
	/**
	 * 查找积分记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	public Page<IntegralStoreLog> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return null;
		}
		String sqlExceptSelect = "FROM integral_log WHERE member_id = " + member.getId();
		return super.findPage(sqlExceptSelect, pageable);
	}
	/**
	 * 喵币分页
	 * @param pageable
	 * @param type
	 * @return
	 */
	public Page<IntegralStoreLog> findPages(Pageable pageable, String name , Integer type){

		String sql = " from  integral_store_log g LEFT JOIN member m ON g.member_id = m.id WHERE 1 =1  ";
		if( name != null ){
			sql += "AND m.nickname LIKE '%" + name +"%' " ;
		}

		if( type != null && 4 != type ){
			sql += "AND g.type = " + type ;
		}

		String select = "SELECT g.*,m.nickname   ";


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
			ordersSQL = " ORDER BY g.create_date DESC";
		}
		sql += ordersSQL;

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

	}
	public IntegralStoreLog findLogByMemberId(Long memberId){
		if (memberId == null) {
			return null;
		}
		String sql = " select * from integral_log where type = 1 and member_id = " + memberId;
        return modelManager.findFirst(sql);
	}
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