package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Filter;
import com.wtshop.Pageable;
import com.wtshop.model.Coupon;
import com.wtshop.model.GroupRemind;
import com.wtshop.model.Member;
import com.wtshop.util.DateUtils;
import com.wtshop.util.SqlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Dao - 优惠券
 * 
 * 
 */
public class GroupRemindDao extends BaseDao<GroupRemind> {

	/**
	 * 构造方法
	 */
	public GroupRemindDao() {
		super(GroupRemind.class);
	}

	/**
	 * 定时器更新优惠券状态
	 */
	/*public List<Coupon> findCoupon(){
		String sql = " select * FROM coupon WHERE is_enabled =1 AND `status` in (0 , 1)";
		return modelManager.find(sql);
	}*/

	/**
	 * 筛选可用优惠券
	 * @param memberId
	 * @param price
	 * @return
	 */
/*	public List<Coupon> findCodeList(Long memberId , Double price ,List<Long> codeList ,List<Long> codes){


		String sql = "SELECT * FROM (select p.end_date, code ,member_id,p.product_category_id,p.id,  p.money,p.desc,  p.condition, p.image ,p.modulus ,p.type ,p.status from coupon_code d " +
                "left join coupon p on d.coupon_id = p.id where 1 = 1  AND p.id IN "+SqlUtils.getSQLIn(codeList) +"  UNION ALL " +
                "select p.realETime,code ,member_id,p.product_category_id,p.id , p.money,p.desc,  p.condition, p.image ,p.modulus ,p.type,p.status from coupon_code d" +
                " left join ticketreceive p on d.receiveId = p.id where  1 = 1 AND p.id IN "+SqlUtils.getSQLIn(codes) +" )a " +
                "where a.condition <= "+ price+" AND a.member_id = "+memberId+" AND a.type = 1 AND (a.end_date IS NOT NULL AND a.end_date >= ' "+ DateUtils.getDateTime()+ "' )";
		return modelManager.find(sql);

	}*/

	/**
	 * 筛选可用优惠券
	 * @param memberId
	 * @param price
	 * @return
	 */


	/*public List<Coupon> findCodeLists(Long memberId , Double price ,Long productId){

		if (memberId == null) {
			return null;
		}
		String sql = " select code ,c.* from coupon_code d left join coupon c on d.coupon_id = c.id where  d.is_used = 0 AND member_id = " +  memberId;
		if(price != null){
			sql += " AND c.condition <= " + price;
		}
		if(productId != null){
			sql += " AND c.type = 2 AND c.product_id =  " +  productId;
		}

		sql += " AND (c.end_date IS NOT NULL AND c.end_date >= '" + DateUtils.getDateTime()+ "' )";
		return modelManager.find(sql);

	}*/

	/**
	 * 我的可用优惠券
	 * 1 分类  2 商品
	 */

/*
	public List<Record> findMyCode(Long memberId , Integer type ) {
		if (memberId == null) {
			return null;
		}
		String sql = "select *  From ( select p.id pid, 0 category,p.end_date,p.`name`,c.*,p.product_category_id,p.money,p.desc, p.condition, p.image ,p.modulus ,p.type FROM coupon_code c LEFT JOIN coupon p ON c.coupon_id = p.id  WHERE 1 = 1 AND receiveId = 0 UNION ALL select p.id pid,1 category,p.realETime,p.`name`, c.*,p.product_category_id,  p.money,p.desc,  p.condition, p.image ,p.modulus ,p.type FROM coupon_code c LEFT JOIN ticketreceive p ON c.receiveId = p.id  WHERE 1 = 1 AND coupon_id = 0 ) a where 1 =1 AND member_id = " + memberId;
		if(type != null){
			sql += " AND a.type = " + type;
		}

		sql += " AND (a.end_date IS NOT NULL AND a.end_date >= '" + DateUtils.getDateTime() + "' )";
		return Db.find(sql);
	}
*/


	
	/**
	 * 查找优惠券分页
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param isExchange
	 *            是否允许积分兑换
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 优惠券分页
	 */
	/*public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable ,List<Long> productCategory) {
		String sqlExceptSelect = "FROM coupon WHERE 1 = 1 ";
		if (isEnabled != null) {
			sqlExceptSelect += " AND is_enabled = " + isEnabled;
		}
		if (isExchange != null) {
			sqlExceptSelect += " AND is_exchange = " + isExchange;
		}
		if (productCategory != null && productCategory.size()>0) {
			sqlExceptSelect += " AND product_category_id IN " + SqlUtils.getSQLIn(productCategory);
		}
		if (hasExpired != null) {
			if (hasExpired) {
				sqlExceptSelect += " AND (end_date IS NOT NULL AND end_date <= '" + DateUtils.getDateTime()+ "' )";
			} else {
				sqlExceptSelect += " AND (end_date IS NULL OR end_date > '" + DateUtils.getDateTime()+ "' )";
			}
		}
		return super.findPage(sqlExceptSelect, pageable);
	}*/



	/**
	 * 查找我的优惠码分页
	 *
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	/*public Page<Coupon> findPage(Member member, Pageable pageable, Boolean isUsed , List<Long> productCategory , Boolean hasExpired, Boolean isEnabled) {
		String sqlExceptSelect = "From ( select p.end_date,p.`name`,c.*,p.product_category_id,p.money,p.desc, p.condition, p.image ,p.modulus ,p.type FROM coupon_code c LEFT JOIN coupon p ON c.coupon_id = p.id  WHERE 1 = 1 AND receiveId = 0 UNION ALL select p.realETime,p.`name`, c.*,p.product_category_id,  p.money,p.desc,  p.condition, p.image ,p.modulus ,p.type FROM coupon_code c LEFT JOIN ticketreceive p ON c.receiveId = p.id  WHERE 1 = 1 AND coupon_id = 0 ) a where 1 =1 ";
		String select = "select * ";
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (isUsed != null) {
			sqlExceptSelect += " AND is_used = " + isUsed + " ";
		}
		if( productCategory != null){
			sqlExceptSelect += " AND product_category_id IN " + SqlUtils.getSQLIn(productCategory) ;
		}
		if (hasExpired != null) {
			if (hasExpired) {
				sqlExceptSelect += " AND (end_date IS NOT NULL AND end_date <= '" + DateUtils.getDateTime()+ "' )";
			} else {
				sqlExceptSelect += " AND (end_date IS NULL OR end_date > '" + DateUtils.getDateTime()+ "' )";
			}
		}
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}
*/


	//查询所有可用
	/*public   List<Coupon>  getallEnable(){
		List<Filter> fList=new ArrayList<>();
		fList.add(Filter.eq("is_enabled",1));
		fList.add(Filter.eq("status",1));
		List<Coupon> list=findList(0,0,fList,null);
		return  list;
		//filter.ne("is_enabled")
	}*/

}