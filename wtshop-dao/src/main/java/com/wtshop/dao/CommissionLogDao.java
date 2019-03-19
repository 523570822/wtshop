package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Order;
import com.wtshop.Pageable;
import com.wtshop.model.CommissionLog;
import com.wtshop.model.DepositLog;
import com.wtshop.model.Member;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Dao - 预存款记录
 * 
 * 
 */
public class CommissionLogDao extends BaseDao<CommissionLog> {

	/**
	 * 构造方法
	 */
	public CommissionLogDao() {
		super(CommissionLog.class);
	}


	/**
	 * 查询充值总金额
	 */
	public Record findRechange(Long memberId){
		if(memberId == null){
			return null;
		}else {
			String sql = "SELECT sum(credit) price FROM deposit_log WHERE type = 0 AND member_id ="+ memberId;
			return Db.findFirst(sql);
		}
	}

	/**
	 * 查找预存款记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 预存款记录分页
	 */
	public Page<CommissionLog> findPage(Member member, Pageable pageable ,Integer type) {
		if (member == null) {
			return null;
		}
		String sqlExceptSelect ="";
		String select = "SELECT *  ";
		if( type != null){
			if( 0 == type ){
				select = " SELECT * ";
				sqlExceptSelect = " FROM deposit_log WHERE member_id = " + member.getId() + " AND type in (2 , 3 , 1 ,0 ,5 ,6)";
				sqlExceptSelect += " order by create_date DESC";
			}else if( 1 == type){
//				select = " select o.name,o.price,o.create_date ";
//				sqlExceptSelect = " FROM deposit_log d left join order_item o on d.order_id = o.order_id WHERE member_id = " + member.getId() + " AND d.type in (4)";
//				sqlExceptSelect += " order by create_date DESC";

				select = " select i.name memo, o.amount_paid price, o.create_date ,f.title operator ,o.id orderId ";
				sqlExceptSelect = " FROM deposit_log d left join order_item i on d.order_id = i.order_id LEFT JOIN `order` o ON d.order_id = o.id LEFT JOIN fu_dai f ON o.actOrderId = f.id WHERE d.member_id = " + member.getId() + " AND d.type in (4)";
				sqlExceptSelect += " group by i.order_id  order by o.create_date DESC";

			}else {
				return null;
			}
		}else {
			sqlExceptSelect = " FROM deposit_log WHERE member_id = " + member.getId() ;
		}

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}

	public CommissionLog findDeposit(Long id){
		String sql = "select * from deposit_log where member_id = " + id ;
		return modelManager.findFirst(sql);
	}



	/**
	 * 用户钱包管理分页
	 */

	public Page<CommissionLog> findPageBySearch(Pageable pageable, String userPhone, String nickname){
		String sql = " from deposit_log d left join member m on m.id = d.member_id where 1 = 1  ";
		if( nickname != null){
			sql += " AND m.nickname like '%" + nickname + "%'";
		}

		if( userPhone != null){
			sql += " AND m.phone like '%" + userPhone + "%'";
		}

		String select = "SELECT d.*,m.nickname, m.phone ";

		sql +=  " ORDER BY d.create_date DESC";

		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sql);

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