package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Member;
import com.wtshop.model.Order;
import com.wtshop.model.OrderItem;
import org.jsoup.helper.StringUtil;

import java.util.List;

/**
 * Dao - 订单项
 * 
 * 
 */
public class OrderItemDao extends BaseDao<OrderItem> {
	
	/**
	 * 构造方法
	 */
	public OrderItemDao() {
		super(OrderItem.class);
	}

	/**
	 * 根据订单号 商品名称搜索订单
	 */
	public List<OrderItem> findOrderByName(String name , String sn, Member member){
		if(name == null && sn ==null && member == null){
			return null;
		}else {
			String sql = "SELECT i.`name`,i.quantity ,i.thumbnail ,i.order_id from `order` o LEFT JOIN order_item i on o.id = i.order_id WHERE o.is_delete = 0 AND o.is_vip = 0 " +
					" AND o.member_id =" + member.getId()+
					" AND (o.sn LIKE '%" + sn +"%' " +
					" OR o.sn LIKE '%" + name +"%' " +
					" OR i.`name` LIKE '%" + name +"%' " +
					" OR i.`name` LIKE '%" + sn +"%') ";
			sql += " order by o.create_date desc ";
			return modelManager.find(sql);

		}
	}

	/**
	 * 获取最新订单项
	 */
	public OrderItem findOrderItem(){
		String sql = " select * from `order_item` where 1 = 1  order by id desc limit 1 " ;
		return modelManager.findFirst(sql);
	}


	/**
	 * 根据sn查找订单项
	 *
	 */

	public OrderItem findOrderBySn (String sn){
		String sql = " select * from `order_item` where 1 = 1 " ;
		if( sn != null){
			sql += " AND sn = " + sn;
		}
		return modelManager.findFirst(sql);
	}

	/**
	 * 根据订单id查找订单项
	 */

	public List<OrderItem> findOrderItemList(Long id){
		String sqlExceptSelect = "select o.*,g.caption,g.id goods_id,p.cost,g.image FROM `order_item` o left join product p on o.product_id = p.id LEFT JOIN goods g on p.goods_id = g.id where 1= 1";
		if( id != null){
			sqlExceptSelect += " AND o.order_id = " + id;
		}

		return modelManager.find(sqlExceptSelect);
	}

	/**
	 * 根据订单id查找订单项
	 */

	public List<OrderItem> findPrice(Long id){
		String sqlExceptSelect = "select o.quantity,g.price price FROM `order_item` o left join product p on o.product_id = p.id LEFT JOIN goods g on p.goods_id = g.id where 1= 1";
		if( id != null){
			sqlExceptSelect += " AND o.order_id = " + id;
		}

		return modelManager.find(sqlExceptSelect);
	}







	/**
	 *
	 */

	public List<OrderItem> findOrderItemList(List<Long> orderItemLists){
		String sqlExceptSelect = "select o.*,g.caption FROM `order_item` o left join product p on o.product_id = p.id LEFT JOIN goods g on p.goods_id = g.id where 1= 1";
		if( orderItemLists.size() > 0){
			sqlExceptSelect += " AND o.order_id in (" + StringUtil.join(orderItemLists,",") + ")";
		}

		return modelManager.find(sqlExceptSelect);
	}

	/**
	 * 查找评论数量
	 * 
	 * @param member
	 * 			会员
	 * @param status
	 * 			状态
	 * @param isReview
	 * 			是否评论
	 * @param pageable
	 * 			分页
	 * @return
	 * 		订单项分页
	 */
	public Page<OrderItem> findPendingOrderItems(Member member, Order.Status status, Boolean isReview, Pageable pageable) {
		String select = "SELECT r.id AS reviewId , i.* ";
		String sqlExceptSelect = " FROM `order_item` i "
							   + " LEFT JOIN `order` o ON i.order_id = o.id "
							   + " LEFT JOIN review r on  r.order_item_id = i.id WHERE 1 = 1 ";
		
		if (isReview != null) {
			sqlExceptSelect += " AND i.is_review = " + isReview;
		}
		if (member != null) {
			sqlExceptSelect += " AND o.member_id = " + member.getId();
		}
		if (status != null) {
			sqlExceptSelect += " AND o.`status` = " + status.ordinal();
		}
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}
	
	
	/**
	 * 查找评论数量
	 * 
	 * @param member
	 * 			会员
	 * @param status
	 * 			状态
	 * @param isReview
	 * 			是否评论
	 * @return
	 * 		订单项数量
	 */
	public Long count(Member member, Order.Status status, Boolean isReview) {
		String sqlExceptSelect = " FROM `order_item` i "
							   + " LEFT JOIN `order` o ON i.order_id = o.id "
							   + " LEFT JOIN review r on  r.order_item_id = i.id WHERE 1 = 1 ";
		
		if (isReview != null) {
			sqlExceptSelect += " AND i.is_review = " + isReview;
		}
		if (member != null) {
			sqlExceptSelect += " AND o.member_id = " + member.getId();
		}
		if (status != null) {
			sqlExceptSelect += " AND o.`status` = " + status.ordinal();
		}
		return super.count(sqlExceptSelect);
	}

	/**
	 * 根据订单Id删除订单项id
	 */
	public Boolean delete(Long orderId){
		return Db.deleteById("order_item", "order_id", orderId);
	}
}