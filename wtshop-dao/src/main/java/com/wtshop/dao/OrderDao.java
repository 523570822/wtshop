package com.wtshop.dao;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Filter;
import com.wtshop.Pageable;
import com.wtshop.model.Goods;
import com.wtshop.model.Member;
import com.wtshop.model.Order;
import com.wtshop.model.PaymentMethod;
import com.wtshop.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Dao - 订单
 * 
 * 
 */
public class OrderDao extends BaseDao<Order> {
	
	/**
	 * 构造方法
	 */
	public OrderDao() {
		super(Order.class);
	}


	/**
	 * 查找所有未支付的订单
	 */
	public List<Order> findExpriceOrder(){
		String sql = " SELECT * from `order` where `status` = 0 AND expire <= '" + DateUtils.formatDateTime(new Date()) + "' AND is_delete = 0   ";
		return modelManager.find(sql);
	}

	/**
	 * 查找所有未支付的订单
	 */
	public List<Order> updateExperce(Member member){

		String sql = " SELECT * from `order` where `status` = 0 AND expire <= '" + DateUtils.formatDateTime(new Date()) + "' AND is_delete = 0 ";

		if(member != null){
			sql += " and member_id = " + member.getId();
		}
		return modelManager.find(sql);
	}


	/**
	 * 获取当前member最近一次的订单信息
	 */
	public Order findOrderByStatus(Long memberId ,String status){

		if(memberId == null ){
			return null;
		}else {
			String sql = " SELECT * FROM `order` o where 1 = 1 ";
			if(memberId != null){
				sql  += " AND o.member_id = " + memberId;
			}
			if(status != null){
				sql  += " AND o.status = " + status;
			}
			sql += " order BY o.id DESC LIMIT 1" ;
			return modelManager.findFirst(sql);
		}
	}



	/**
	 * 获取当前member最十次的订单信息
	 */
	public List<Order> findOrderList(Long memberId){

		if(memberId == null ){
			return null;
		}else {
			String sql = " SELECT * FROM `order` o where o.member_id = " + memberId +" order BY o.id DESC LIMIT 10";
			return modelManager.find(sql);
		}
	}


	/**
	 * 根据编号查找订单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	public Order findBySn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}
		String sql = "SELECT * FROM `order` WHERE sn = LOWER(?) AND is_delete = 0 ";
		try {
			return modelManager.findFirst(sql, sn);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 根据编号查找订单
	 *
	 * @param groupbuyId
	 *            编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	public List<Order> findByfightgroupId(Long groupbuyId) {

	//	String sql = "SELECT * FROM `order` WHERE fightgroup_id ="+groupbuyId+" AND is_delete = 0 ";
		String sql = "SELECT o.*,m.avatar FROM `order` o LEFT JOIN member m on o.member_id=m.id  WHERE fightgroup_id ="+groupbuyId+" AND o.is_delete = 0 and o.`status` <>7 and o.`status` <>6 and o.`status` <>0 and o.`status` <>8";
		try {
			return modelManager.find(sql);
		} catch (Exception e) {
			return null;
		}
	}	/**
	 * 根据编号查找订单
	 *
	 * @param groupbuyId
	 *            编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	public List<Order> findByfightgroupIdmemberId(Long groupbuyId,Long memberId) {

	//	String sql = "SELECT * FROM `order` WHERE fightgroup_id ="+groupbuyId+" AND is_delete = 0 ";
		String sql = "SELECT o.*,m.avatar FROM `order` o LEFT JOIN member m on o.member_id=m.id  WHERE fightgroup_id ="+groupbuyId+" and o.member_id="+memberId+"  AND o.is_delete = 0 and o.`status` <>7 and o.`status` <>6 and o.`status` <>0 and o.`status` <>8";
		try {
			return modelManager.find(sql);
		} catch (Exception e) {
			return null;
		}
	}	/**
	 * 根据编号查找订单
	 *
	 * @param groupbuyId
	 *            编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	public List<Order> findBytuanGouIdmemberId(Long groupbuyId,Long memberId) {

	//	String sql = "SELECT * FROM `order` WHERE fightgroup_id ="+groupbuyId+" AND is_delete = 0 ";
		String sql = "SELECT o.*,m.avatar FROM `order` o LEFT JOIN member m on o.member_id=m.id  WHERE groupbuy_id ="+groupbuyId+" and o.member_id="+memberId+"  AND o.is_delete = 0 and o.`status` <>7 and o.`status` <>6 and o.`status` <>0 and o.`status` <>8";
		try {
			return modelManager.find(sql);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据倒拍详情ID查找订单
	 * @param actOrderId	倒拍详情id
	 * @return
	 */
	public Order findByActOrderId(String actOrderId) {
		if (StringUtils.isEmpty(actOrderId)) {
			return null;
		}
		String sql = "SELECT * FROM `order` WHERE actOrderId = ? AND is_delete = 0";
		try {
			return modelManager.findFirst(sql, actOrderId);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 查找订单
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isUseCouponCode
	 *            是否已使用优惠码
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 订单
	 */
	public List<Order> findList(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
			List<com.wtshop.Order> orders) {
		String sql = "SELECT * FROM `order` o WHERE 1 = 1 AND o.is_delete = 0  ";
		if (type != null) {
			sql += " AND o.type = " + type.ordinal();
		}
		if (status != null) {
			sql += " AND o.status = " + status.ordinal();
		}
		if (member != null) {
			sql += " AND o.member_id = " + member.getId();
		}
		if (goods != null) {
			sql += " AND EXISTS (SELECT 1 FROM `order_item` oi WHERE o.id = oi.order_id AND oi.product_id IN (SELECT p.id FROM `product` p LEFT JOIN `goods` g ON p.goods_id = g.id AND g.id = " + goods.getId() + ")) ";
		}
		if (isPendingReceive != null) {
			String subQuery = ""
				+ " ((o.expire IS NULL OR o.expire > '" + DateUtils.formatDateTime(new Date()) + "') AND o.payment_method_name = " + PaymentMethod.Type.cashOnDelivery.ordinal()
				+ " AND o.`status` != " + Order.Status.completed.ordinal() 
				+ " AND o.`status` != " + Order.Status.failed.ordinal() 
				+ " AND o.`status` != " + Order.Status.canceled.ordinal() 
				+ " AND o.`status` != " + Order.Status.denied.ordinal()
				+ " AND o.amount_paid < o.amount) ";
			if (isPendingReceive) {
				sql += " AND" + subQuery;
			} else {
			}
		}
		if (isPendingRefunds != null) {
			String subQuery = ""
					+ " ((o.expire IS NOT NULL OR o.expire <= '" + DateUtils.formatDateTime(new Date()) + "'"
					+ " OR  o.`status` = " + Order.Status.failed.ordinal() 
					+ " OR  o.`status` = " + Order.Status.canceled.ordinal() 
					+ " OR  o.`status` = " + Order.Status.denied.ordinal() + ")"
					+ " AND o.amount_paid > 0) "
					+ " AND o.`status` = " + Order.Status.completed.ordinal()
					+ " AND o.amount_paid > o.amount ";
			
			if (isPendingRefunds) {
				sql += " OR " + subQuery;
			} else {
				
			}
		}
		if (isUseCouponCode != null) {
			sql += " AND o.is_use_coupon_code = " + isUseCouponCode;
		}
		if (isExchangePoint != null) {
			sql += " AND o.is_exchange_point = " + isExchangePoint;
		}
		if (isAllocatedStock != null) {
			sql += " AND o.is_allocated_stock = " + isAllocatedStock;
		}
		if (hasExpired != null) {
			if (hasExpired) {
				sql += " AND o.expire IS NOT NULL AND o.expire <= '" + DateUtils.formatDateTime(new Date()) + "' ";
			} else {
				sql += " AND o.expire IS NULL OR o.expire > '" + DateUtils.formatDateTime(new Date()) + "' ";
			}
		}
		return super.findList(sql, null, count, filters, orders);
	}

	/**
	 * 查找订单分页
	 *

	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 订单分页
	 */
	public Page<Order> findPages(Order.Status status, Member member, Pageable pageable ,Integer type) {
		String select = " select o.type, o.id ,o.status ,o.quantity quantity,o.amount price ," +
			"o.sn , o.create_date ,o.freight, o.actOrderId fudaiId";
		String sqlExceptSelect = "FROM `order` o WHERE 1 = 1 AND o.is_delete = 0 And o.type<>7 " ;

		if(status != null &&  !status.equals(Order.Status.shipped)){
			sqlExceptSelect += " AND o.status = "+ status.ordinal();
		}

		if (status != null && status.equals(Order.Status.shipped)) {
			sqlExceptSelect += " AND o.status = 3 " ;
		}

		if (member != null) {
			sqlExceptSelect += " AND o.member_id = " + member.getId();
		}
		sqlExceptSelect += " order by o.modify_date desc";
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}

	/**
	 * 查找团购订单分页
	 *

	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 订单分页
	 */
	public Page<Order> findTuanGouPages(String status, Member member, Pageable pageable ) {
		String select = " select o.type, o.id ,o.status ,o.quantity quantity,o.amount price,o.groupbuy_id,o.fightgroup_id ," +
				"o.sn , o.create_date ,o.freight, o.actOrderId fudaiId  ,gb.group_rate rate, CASE WHEN  o.fightgroup_id =0  or o.member_id = f.member_id THEN 1 ELSE 0 END istuanzhang";
		String sqlExceptSelect = "FROM `order` o LEFT JOIN fight_group f on o.fightgroup_id=f.id   LEFT JOIN group_buy gb on gb.id=o.groupbuy_id WHERE 1 = 1 AND o.is_delete = 0 And o.type=7 " ;

		if(status != null){

			if(status.equals("0")){
			//	sqlExceptSelect = "FROM `order` o LEFT JOIN fight_group f on o.fightgroup_id=f.id  WHERE 1 = 1 AND o.is_delete = 0 And o.type=7 and and o.is_singlepurchase <>1 and f.count<f.groupnum" ;

				sqlExceptSelect += " AND o.status in ('6','7','8','11') ";
			}
			if(status.equals("1")){
				sqlExceptSelect = "FROM `order` o LEFT JOIN fight_group f on o.fightgroup_id=f.id  LEFT JOIN group_buy gb on gb.id=o.groupbuy_id  WHERE 1 = 1 AND o.is_delete = 0 And o.type=7   and f.count=f.groupnum" ;
				sqlExceptSelect += " AND o.status in ('2','3','4','5','9','10') ";
			}
			if(status.equals("2")){
				sqlExceptSelect = "FROM `order` o LEFT JOIN fight_group f on o.fightgroup_id=f.id  LEFT JOIN group_buy gb on gb.id=o.groupbuy_id  WHERE 1 = 1 AND o.is_delete = 0 And o.type=7  and (f.count<f.groupnum or f.count is null)" ;
				sqlExceptSelect += " AND o.status in('0','1','2') ";
			}

		}



		if (member != null) {
			sqlExceptSelect += " AND o.member_id = " + member.getId();
		}
		sqlExceptSelect += " order by o.modify_date desc";
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}
	/**
	 * 查找佣金订单分页
	 *

	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 订单分页
	 */
	public Page<Order> findYongJinPages(String status, Member member, Pageable pageable ) {
		String select = " select o.type, o.id ,o.status ,o.quantity quantity,o.amount price,o.commission_rate,o.groupbuy_id,o.fightgroup_id ," +
				"o.sn , o.create_date ,o.freight, o.actOrderId fudaiId";
		String sqlExceptSelect = "FROM `order` o WHERE 1 = 1 AND o.is_delete = 0 And o.type=0 and o.on_share_code is not null " ;

		if(status != null){

			if(status.equals("0")){

				sqlExceptSelect += " AND o.status in ('0') ";
			}
			if(status.equals("1")){
				sqlExceptSelect += " AND o.status in ('3') ";
			}
			if(status.equals("2")){
				sqlExceptSelect += " AND o.status in('5','9','10') ";
			}

		}

		if (member != null &&StringUtils.isNotEmpty(member.getShareCode())) {
			sqlExceptSelect += " AND ( o.share_code = '" + member.getShareCode()+ "' or o.on_share_code ='"+member.getShareCode()+"' )";
		}else if(member != null){
			sqlExceptSelect +=" o.on_share_code ='"+member.getShareCode()+"' ";
		}

		sqlExceptSelect += "  order by o.modify_date desc";
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}	/**
	 * 查找佣金订单分页
	 *

	 * @param memberId
	 *            状态
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 订单分页
	 */
	public Page<Order> findYongJinXiaPages(Integer memberId, Member member, Pageable pageable ) {
		String select = " select o.type, o.id ,o.status ,o.quantity quantity,o.amount price,o.commission_rate,o.groupbuy_id,o.fightgroup_id ," +
				"o.sn , o.create_date ,o.freight, o.actOrderId fudaiId";
		String sqlExceptSelect = "FROM `order` o WHERE 1 = 1 AND o.is_delete = 0 And (o.type = 0 or o.type=3) and o.on_share_code is not null " ;



			sqlExceptSelect +=" and o.on_share_code ='"+member.getShareCode()+"' and o.member_id ="+memberId+" ";


		sqlExceptSelect += "  order by o.modify_date desc";
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}

	/**
	 * 查找订单分页
	 *

	 *            状态
	 * @param member
	 *            会员
	 *            分页信息
	 * @return 订单分页
	 */
	public List<Order> findPages(Member member) {
		String sqlExceptSelect = " select o.type, o.id ,o.status , o.member_id, o.quantity quantity,o.amount price ,o.sn , o.create_date ,o.freight FROM `order` o LEFT JOIN order_item t ON o.id = t.order_id LEFT JOIN `returns` r ON o.id = r.order_id LEFT JOIN returns_item i ON i.return_id = r.id  WHERE 1 = 1 and o.`status` in (5, 9, 10) and o.type != 2 and o.type !=3 and o.type != 6 AND r.id is NULL OR (r.id IS NOT NULL AND t.product_id = i.product_id) " ;
		sqlExceptSelect += " GROUP BY id HAVING o.member_id = " + member.getId();
		sqlExceptSelect += " order by o.create_date desc";
		return modelManager.find(sqlExceptSelect);
	}



	/**
	 * 查找订单分页
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isUseCouponCode
	 *            是否已使用优惠码
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 订单分页
	 */
	public Page<Order> findPage(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable) {
		String sqlExceptSelect = "FROM `order` o  WHERE 1 = 1 AND o.is_delete = 0";
		if (type != null) {
			sqlExceptSelect += " AND o.type = " + type.ordinal();
		}
		if (status != null) {
			sqlExceptSelect += " AND o.status = " + status.ordinal();
		}
		if (member != null) {
			sqlExceptSelect += " AND o.member_id = " + member.getId();
		}
		if (goods != null) {
			sqlExceptSelect += " AND EXISTS (SELECT 1 FROM `order_item` oi WHERE o.id = oi.order_id AND oi.product_id IN (SELECT p.id FROM `product` p LEFT JOIN `goods` g ON p.goods_id = g.id AND g.id = " + goods.getId() + ")) ";
		}
		if (isPendingReceive != null) {
			String subQuery = ""
					+ " ((o.expire IS NOT NULL OR o.expire <= '" + DateUtils.formatDateTime(new Date()) + "'"
					+ " OR  o.`status` = " + Order.Status.failed.ordinal() 
					+ " OR  o.`status` = " + Order.Status.canceled.ordinal() 
					+ " OR  o.`status` = " + Order.Status.denied.ordinal() + ")"
					+ " AND o.amount_paid > 0) "
					+ " AND o.`status` = " + Order.Status.completed.ordinal()
					+ " AND o.amount_paid > o.amount ";
			if (isPendingReceive) {
				sqlExceptSelect += " OR " + subQuery;
			} else {
				
			}
		}
		if (isPendingRefunds != null) {
			String subQuery = ""
					+ " ((o.expire IS NOT NULL OR o.expire <= '" + DateUtils.formatDateTime(new Date()) + "'"
					+ " OR  o.`status` = " + Order.Status.failed.ordinal() 
					+ " OR  o.`status` = " + Order.Status.canceled.ordinal() 
					+ " OR  o.`status` = " + Order.Status.denied.ordinal() + ")"
					+ " AND o.`status` = " + Order.Status.completed.ordinal();
			if (isPendingRefunds) {
				sqlExceptSelect += " OR " + subQuery;
			} else {
				
			}
		}
		if (isUseCouponCode != null) {
			sqlExceptSelect += " o.is_use_coupon_code = " + isUseCouponCode;
		}
		if (isExchangePoint != null) {
			sqlExceptSelect += " o.is_exchange_point = " + isExchangePoint;
		}
		if (isAllocatedStock != null) {
			sqlExceptSelect += " o.is_allocated_stock = " + isAllocatedStock;
		}
		if (hasExpired != null) {
			if (hasExpired) {
				sqlExceptSelect += " AND o.expire IS NOT NULL AND o.expire <= '" + DateUtils.formatDateTime(new Date()) + "' ";
			} else {
				sqlExceptSelect += " AND o.expire IS NULL OR o.expire > '" + DateUtils.formatDateTime(new Date()) + "' ";
			}
		}
		return super.findPage(sqlExceptSelect, pageable);
	}
	
	/**
	 * 查询订单数量
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param goods
	 *            货品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isUseCouponCode
	 *            是否已使用优惠码
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @return 订单数量
	 */
	public Long count(Order.Type type, Order.Status status, Member member, Goods goods, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {
		String sql = "FROM `order` o WHERE 1 = 1 and o.type<>7 and o.is_delete = 0 ";
		if (type != null) {
			sql += " AND o.type = " + type.ordinal();
		}
		if (status != null) {
			sql += " AND o.status = " + status.ordinal();
		}
		if (member != null) {
			sql += " AND o.member_id = " + member.getId();
		}
		if (goods != null) {
			sql += " AND EXISTS (SELECT 1 FROM `order_item` oi WHERE o.id = oi.order_id AND oi.product_id IN (SELECT p.id FROM `product` p LEFT JOIN `goods` g ON p.goods_id = g.id AND g.id = " + goods.getId() + ")) ";
		}
		if (isPendingReceive != null) {
			String subQuery = ""
					+ " ((o.expire IS NULL OR o.expire > '" + DateUtils.formatDateTime(new Date()) + "') AND o.payment_method_name = " + PaymentMethod.Type.cashOnDelivery.ordinal()
					+ " AND o.`status` != " + Order.Status.completed.ordinal() 
					+ " AND o.`status` != " + Order.Status.failed.ordinal() 
					+ " AND o.`status` != " + Order.Status.canceled.ordinal() 
					+ " AND o.`status` != " + Order.Status.denied.ordinal()
					+ " AND o.amount_paid < o.amount) ";
			if (isPendingReceive) {
				sql += " AND" + subQuery;
			} else {
				
			}
		}
		if (isPendingRefunds != null) {
			String subQuery = ""
					+ " ((o.expire IS NOT NULL OR o.expire <= '" + DateUtils.formatDateTime(new Date()) + "'"
					+ " OR  o.`status` = " + Order.Status.failed.ordinal() 
					+ " OR  o.`status` = " + Order.Status.canceled.ordinal() 
					+ " OR  o.`status` = " + Order.Status.denied.ordinal() + ")"
					+ " AND o.amount_paid > 0) "
					+ " or o.`status` = " + Order.Status.completed.ordinal()
					+ " AND o.amount_paid > o.amount ";
			if (isPendingReceive!=null&&isPendingReceive&&isPendingRefunds) {
				sql += " OR " + subQuery;
			} else  if (isPendingRefunds){
				sql += " AND " + subQuery;
			}
		}
		if (isUseCouponCode != null) {
			sql += " o.is_use_coupon_code = " + isUseCouponCode;
		}
		if (isExchangePoint != null) {
			sql += " o.is_exchange_point = " + isExchangePoint;
		}
		if (isAllocatedStock != null) {
			sql += " o.is_allocated_stock = " + isAllocatedStock;
		}
		if (hasExpired != null) {
			if (hasExpired) {
				sql += " AND o.expire IS NOT NULL AND o.expire <= '" + DateUtils.formatDateTime(new Date()) + "' ";
			} else {
				sql += " AND (o.expire IS NULL OR o.expire > '" + DateUtils.formatDateTime(new Date()) + "' )";
			}
		}
		return super.count(sql);
	}


	/**
	 * 查询订单
	 *
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单创建数
	 */
	public List<Order> findStaffOrder(Date beginDate, Date endDate) {
		String sqlExceptSelect="select * FROM `order`  WHERE 1 = 1 and is_commission = 1";

		if (beginDate != null) {
			sqlExceptSelect += " AND create_date >= '"+ DateUtils.formatDateTime(beginDate) + "' ";
		}
		if (endDate != null) {
			sqlExceptSelect += " AND create_date <= '" + DateUtils.formatDateTime(endDate) + "' ";
		}
		return modelManager.find(sqlExceptSelect);
	}
	
	/**
	 * 查询订单创建数
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单创建数
	 */
	public Long createOrderCount(Date beginDate, Date endDate) {
		String sqlExceptSelect="FROM `order`  WHERE 1 = 1 and is_delete = 0 ";
		
		if (beginDate != null) {
			sqlExceptSelect += " AND create_date >= '"+ DateUtils.formatDateTime(beginDate) + "' ";
		}
		if (endDate != null) {
			sqlExceptSelect += " AND create_date <= '" + DateUtils.formatDateTime(endDate) + "' ";
		}
		return super.count(sqlExceptSelect);
	}

	/**
	 * 查询订单完成数
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单完成数
	 */
	public Long completeOrderCount(Date beginDate, Date endDate) {
		String sqlExceptSelect="FROM `order`  WHERE 1 = 1 and is_delete = 0  ";
		if (beginDate != null) {
			sqlExceptSelect += " AND complete_date >= '"+ DateUtils.formatDateTime(beginDate) + "' ";
		}
		if (endDate != null) {
			sqlExceptSelect += " AND complete_date <= '" + DateUtils.formatDateTime(endDate) + "' ";
		}
		return super.count(sqlExceptSelect);
	}


	/**
	 * 查询订单创建金额
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单创建金额
	 */
	public BigDecimal createOrderAmount(Date beginDate, Date endDate) {
		String sql = "SELECT SUM(amount) FROM `order`  WHERE 1 = 1 and is_delete = 0  ";
		if (beginDate != null) {
			sql += " AND complete_date >= '"+ DateUtils.formatDateTime(beginDate) + "' ";
		}
		if (endDate != null) {
			sql += " AND complete_date <= '" + DateUtils.formatDateTime(endDate) + "' ";
		}
		BigDecimal result = Db.queryBigDecimal(sql);
		return result != null ? result : BigDecimal.ZERO;
	}

	/**
	 * 查询订单完成金额
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单完成金额
	 */
	public BigDecimal completeOrderAmount(Date beginDate, Date endDate) {
		String sql = "SELECT SUM(amount) FROM `order`  WHERE 1 = 1 and is_delete = 0  ";
		if (beginDate != null) {
			sql += " AND complete_date >= '"+ DateUtils.formatDateTime(beginDate) + "' ";
		}
		if (endDate != null) {
			sql += " AND complete_date <= '" + DateUtils.formatDateTime(endDate) + "' ";
		}
		BigDecimal result = Db.queryBigDecimal(sql);
		return result != null ? result : BigDecimal.ZERO;
	}
	public Page queryListByPage(int pageNumber, int pageSize,String name,String phone){
		String sql = "SELECT o.*,m.nickname";
		String from = "FROM `order` AS o LEFT JOIN member AS m ON o.member_id=m.id WHERE 1=1";

		if(!StrKit.isBlank(name)){
			from+=" and m.nickname like '%"+name+"%'";
		}
		if(!StrKit.isBlank(phone)){
			from+=" and o.phone like '%"+phone+"%'";
		}
		Page page = Db.paginate(pageNumber,pageSize,sql,from);
		return page;
	}

    public List<Order> findByMemberId(Long id) {
		String sqlExceptSelect="select * FROM `order` o where o.type=2 and o.member_id='"+id+"' AND o.status in ('2','3','4','5','9','10')";


		return modelManager.find(sqlExceptSelect);
    }
}