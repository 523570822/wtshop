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
	public Page<CommissionLog> findPage(Member member, Pageable pageable ,Integer type,Integer status) {
		if (member == null) {
			return null;
		}
		String sqlExceptSelect ="";
		String select = "SELECT *  ";
		if( type != null){

				select = " SELECT * ";
		if(status==null){
			sqlExceptSelect+=" from commission_log where type ="+type+" ";
		}else {
			sqlExceptSelect+=" from commission_log where type ="+type+" and status ="+status+ "";
		}



			sqlExceptSelect += "  and member_id = " + member.getId()+ "   order by create_date DESC";
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