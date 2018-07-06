package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Filter;
import com.wtshop.Pageable;
import com.wtshop.model.Ticket;
import com.wtshop.util.DateUtils;
import com.wtshop.util.SqlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Dao - 优惠券
 * //2017年8月8日17:42:37注意为了代码统一,方便调用,coupon一般是指的ticket表
 * 
 * 
 */
public class TicketDao extends BaseDao<Ticket> {

	/**
	 * 构造方法
	 */
	public TicketDao() {
		super(Ticket.class);
	}

	/**
	 * 定时器更新优惠券状态
	 */
	public List<Ticket> findCoupon(){
		String sql = " select * FROM ticket WHERE is_enabled =1 AND `status` in (0 , 1)";
		return modelManager.find(sql);
	}

	/**
	 * 筛选可用优惠券
	 * @param memberId
	 * @param price
	 * @return
	 */
	public List<Ticket> findCodeList(Long memberId , Double price ,List<Long> codeList){

		if (memberId == null) {
			return null;
		}
		String sql = " select code ,c.* from coupon_code d left join ticket c on d.coupon_id = c.id where  d.is_used = 0 AND member_id = " +  memberId;
		if(price != null){
			sql += " AND c.condition <= " + price;
		}
		if(codeList != null && codeList.size() > 0){

			sql += " AND c.type = 1 AND c.id IN " +  SqlUtils.getSQLIn(codeList);

		}

		sql += " AND (c.end_date IS NOT NULL AND c.end_date >= '" + DateUtils.getDateTime()+ "' )";
		return modelManager.find(sql);

	}

	/**
	 * 筛选可用优惠券
	 * @param memberId
	 * @param price
	 * @return
	 */


	public List<Ticket> findCodeLists(Long memberId , Double price ,Long productId){

		if (memberId == null) {
			return null;
		}
		String sql = " select code ,c.* from coupon_code d left join ticket c on d.coupon_id = c.id where  d.is_used = 0 AND member_id = " +  memberId;
		if(price != null){
			sql += " AND c.condition <= " + price;
		}
		if(productId != null){
			sql += " AND c.type = 2 AND c.product_id =  " +  productId;
		}

		sql += " AND (c.end_date IS NOT NULL AND c.end_date >= '" + DateUtils.getDateTime()+ "' )";
		return modelManager.find(sql);

	}

	/**
	 * 我的可用优惠券
	 * 1 分类  2 商品
	 */

	public List<Ticket> findMyCode(Long memberId ,Integer type ) {
		if (memberId == null) {
			return null;
		}
		String sql = " select code ,c.* from coupon_code d left join ticket c on d.coupon_id = c.id where  d.is_used = 0 AND member_id = " + memberId;
		if(type != null){
			sql += " AND c.type = " + type;
		}

		sql += " AND (c.end_date IS NOT NULL AND c.end_date >= '" + DateUtils.getDateTime() + "' )";
		return modelManager.find(sql);
	}
	
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
	public Page<Ticket> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable ,Integer type) {
		String sqlExceptSelect = "FROM ticket WHERE 1 = 1 ";
		if (isEnabled != null) {
			sqlExceptSelect += " AND is_enabled = " + isEnabled;
		}
		if (isExchange != null) {
			sqlExceptSelect += " AND is_exchange = " + isExchange;
		}
		if (type != null) {
			sqlExceptSelect += " AND product_category_id = " + type;
		}
		if (hasExpired != null) {
			if (hasExpired) {
				sqlExceptSelect += " AND (end_date IS NOT NULL AND end_date <= '" + DateUtils.getDateTime()+ "' )";
			} else {
				sqlExceptSelect += " AND (end_date IS NULL OR end_date > '" + DateUtils.getDateTime()+ "' )";
			}
		}
		return super.findPage(sqlExceptSelect, pageable);
	}



	//查询所有可用
	public   List<Ticket>  getallEnable(){
		List<Filter> fList=new ArrayList<>();
		fList.add(Filter.eq("is_enabled",1));
		fList.add(Filter.eq("status",1));
		List<Ticket> list=findList(0,0,fList,null);
		return  list;
		//filter.ne("is_enabled")
	}

}