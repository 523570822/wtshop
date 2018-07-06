package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Member;
import com.wtshop.model.Returns;
import com.wtshop.model.ReturnsItem;

import java.util.List;

/**
 * Dao - 退货单
 * 
 * 
 */
public class ReturnsDao extends BaseDao<Returns> {
	
	/**
	 * 构造方法
	 */
	public ReturnsDao() {
		super(Returns.class);
	}

	/**
	 * 根据sn获得退货信息
	 */
	public Returns findReturnsBySn(Long sn){
		String sql = "select * from returns where 1 = 1 ";
		if(sn == null){
			return null;
		}else {
			sql += " AND sn = " + sn ;
		}
		return modelManager.findFirst(sql);
	}

	/**
	 * 退货单分页
	 */
	public Page<Returns> findPages(Pageable pageable){

		String sqlExceptSelect = "FROM returns WHERE 1 = 1 AND is_delete = 0 ";
		return super.findPage(sqlExceptSelect, pageable);

	}

	/**
	 * 根据sn获得退货信息
	 */
	public Returns findReturnsByOrderId(Long orderId){
		String sql = "select * from returns where 1 = 1 ";
		if(orderId == null){
			return null;
		}else {
			sql += " AND order_id = " + orderId ;
		}
		return modelManager.findFirst(sql);
	}


	public Returns findReturnsByGoodsId(Long goodsId, Long orderId){

			String sql = "SELECT g.id FROM `order` o LEFT JOIN `returns` r on o.id = r.order_id LEFT JOIN returns_item i ON r.id = i.return_id LEFT JOIN product p ON i.product_id = p.id LEFT JOIN goods g ON p.goods_id = g.id where 1 = 1 ";
			if(goodsId != null ){
				sql += " and g.id = "+ goodsId;
			}
			if(orderId != null){
				sql += " and o.id = "+ orderId;
			}
			return modelManager.findFirst(sql);
			}




	/**
	 * 根据订单号 商品名称搜索订单
	 */
	public List<Returns> findReturnsList(String name , String sn , Member member){
		if(name == null && sn ==null){
			return null;
		}else {
			String sql = "SELECT r.create_date , r.sn ,r.id ,r.status,r.type FROM `returns` r LEFT JOIN returns_item i on r.id = i.return_id  WHERE r.is_delete = 0 " +
					" AND i.member_id = " + member.getId() +
					" AND (r.sn LIKE '%" + sn +"%' " +
					" OR r.sn LIKE '%" + name +"%' " +
					" OR i.`name` LIKE '%" + name +"%' " +
					" OR i.`name` LIKE '%" + sn +"%') group by r.sn ";
			sql += " order by r.create_date desc ";
			return modelManager.find(sql);

		}
	}


	/**
	 * 退货单项
	 *
	 * @param member
	 * @return 退货单项，若不存在则返回null
	 */
	public List<Returns> findByMember(Member member, Integer status) {
		if (member == null) {
			return null;
		}
		try {
			String sql = "SELECT  r.create_date , r.sn ,r.id ,r.type,r.status FROM `returns` r LEFT JOIN returns_item i on r.id = i.return_id  WHERE r.is_delete = 0 and i.member_id = ? ";
			if (status == ReturnsItem.Status.refund.ordinal()) {
				sql += " AND i.status = " + ReturnsItem.Status.refund.ordinal();
			}
			sql += " group by r.sn order by  r.create_date desc";
			return modelManager.find(sql, member.getId());
		} catch (Exception e) {
			return null;
		}
	}
}