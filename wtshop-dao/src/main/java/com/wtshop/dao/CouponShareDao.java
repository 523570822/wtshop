package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.CouponShare;
import com.wtshop.util.DateUtils;

/**
 * Dao - 优惠券
 * 
 * 
 */
public class CouponShareDao extends BaseDao<CouponShare> {

	/**
	 * 构造方法
	 */
	public CouponShareDao() {
		super(CouponShare.class);
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
	public Page<CouponShare> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable ,Integer type) {
		String sqlExceptSelect = "FROM coupon WHERE 1 = 1 ";
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



	public  boolean  isreceived(long mid,long couponid){
		String sqlExceptSelect = "FROM coupon_code WHERE member_id="+mid+" and coupon_id= "+couponid;
		long c=count(sqlExceptSelect);
		if (c>0){return  true ;}else { return  false; }
	}

}