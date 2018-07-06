package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.model.Coupon;
import com.wtshop.model.CouponCode;
import com.wtshop.model.Member;
import com.wtshop.model.Ticket;
import com.wtshop.util.DateUtils;
import com.wtshop.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Dao - 优惠码
 * 
 * 
 */
public class CouponCodeDao extends BaseDao<CouponCode> {
	
	/**
	 * 构造方法
	 */
	public CouponCodeDao() {
		super(CouponCode.class);
	}


	
	/**
	 * 判断优惠码是否存在
	 * 
	 * @param code
	 *            号码(忽略大小写)
	 * @return 优惠码是否存在
	 */
	public boolean codeExists(String code) {
		if (StringUtils.isEmpty(code)) {
			return false;
		}
		String sql = "SELECT COUNT(*) FROM coupon_code WHERE LOWER(code) = LOWER(?)";
		Long count = Db.queryLong(sql, code);
		return count > 0;
	}

	/**
	 * 根据号码查找优惠码
	 * 
	 * @param code
	 *            号码(忽略大小写)
	 * @return 优惠码，若不存在则返回null
	 */
	public CouponCode findByCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		try {
			String sql = "SELECT * FROM coupon_code WHERE LOWER(code) = LOWER(?)";
			return modelManager.findFirst(sql, code);
		} catch (Exception e) {
			return null;
		}
	}



	/**
	 * 查找我的优惠码分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	public Page<CouponCode> findPage(Member member, Pageable pageable, Boolean isUsed , List<Long> productCategory , Boolean hasExpired, Boolean isEnabled) {
		String sqlExceptSelect = "FROM coupon_code c LEFT JOIN coupon p ON c.coupon_id = p.id  WHERE 1 = 1 ";
		String select = "select p.end_date,p.`name`, c.* , p.money, p.condition, p.status , p.image ,p.modulus ,p.type";
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (isUsed != null) {
			sqlExceptSelect += " AND is_used = " + isUsed + " ";
		}
		if( productCategory != null){
			sqlExceptSelect += " AND p.product_category_id IN " + SqlUtils.getSQLIn(productCategory) ;
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

	/**
	 * 查找所有优惠码分页
	 *
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	public Page<CouponCode> findPage(Pageable pageable, Boolean isUsed ,Integer type) {
		String sqlExceptSelect = "FROM coupon_code c LEFT JOIN coupon p ON c.coupon_id = p.id  WHERE 1 = 1 AND p.status = 1";
		String select = "select p.end_date,p.`name`,c.`code`, c.id ,p.money ,p.condition ,p.status , c.member_id , p.image";
		if (isUsed != null) {
			sqlExceptSelect += " AND is_used = " + isUsed + " ";
		}
		if( type != null){
			sqlExceptSelect += " AND p.product_category_id = " + type + " ";
		}
		return modelManager.paginate(pageable.getPageNumber(), pageable.getPageSize(), select, sqlExceptSelect);
	}



	/**
	 * 查找优惠码数量
	 * 
	 * @param coupon
	 *            优惠券
	 * @param member
	 *            会员
	 * @param hasBegun
	 *            是否已开始
	 * @param hasExpired
	 *            是否已过期
	 * @param isUsed
	 *            是否已使用
	 * @return 优惠码数量
	 */
	public Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
		String sqlExceptSelect = "FROM coupon_code cc LEFT JOIN coupon c ON cc.`coupon_id` = c.`id` WHERE 1 = 1 ";
		if (coupon != null&&coupon.getId()>0) {
			   sqlExceptSelect += " AND coupon_id = " + coupon.getId();
		}
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (hasBegun != null) {
			if (hasBegun) {
				sqlExceptSelect += " AND (begin_date IS NULL OR begin_date <= '" + DateUtils.getDateTime()+ "') ";
			} else {
				sqlExceptSelect += " AND begin_date IS NOT NULL AND begin_date > '" + DateUtils.getDateTime()+ "' ";
			}
		}
		if (hasExpired != null) {
			if (hasExpired) {
				sqlExceptSelect += " AND end_date IS NOT NULL AND end_date <= '" + DateUtils.getDateTime()+ "' ";
			} else {
				sqlExceptSelect += " AND (end_date IS NULL OR end_date > '" + DateUtils.getDateTime()+ "') ";
			}
		}
		if (isUsed != null) {
			sqlExceptSelect += " AND is_used = " + isUsed + " ";
		}
		return super.count(sqlExceptSelect);
	}


	public Long count(Ticket coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
		String sqlExceptSelect = "FROM coupon_code cc LEFT JOIN ticket c ON cc.`coupon_id` = c.`id` WHERE 1 = 1 ";
		if (coupon != null&&coupon.getId()>0) {
			sqlExceptSelect += " AND coupon_id = " + coupon.getId();
		}
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if (hasBegun != null) {
			if (hasBegun) {
				sqlExceptSelect += " AND (begin_date IS NULL OR begin_date <= '" + DateUtils.getDateTime()+ "') ";
			} else {
				sqlExceptSelect += " AND begin_date IS NOT NULL AND begin_date > '" + DateUtils.getDateTime()+ "' ";
			}
		}
		if (hasExpired != null) {
			if (hasExpired) {
				sqlExceptSelect += " AND end_date IS NOT NULL AND end_date <= '" + DateUtils.getDateTime()+ "' ";
			} else {
				sqlExceptSelect += " AND (end_date IS NULL OR end_date > '" + DateUtils.getDateTime()+ "') ";
			}
		}
		if (isUsed != null) {
			sqlExceptSelect += " AND is_used = " + isUsed + " ";
		}
		return super.count(sqlExceptSelect);
	}


	/**
	 * 判断是否领取过优惠券
	 */
	public Long count (Long cauponId , Member member ,Long receiveId ){
		String sqlExceptSelect = "FROM coupon_code WHERE 1 = 1 AND is_used = 0 ";
		if (member != null) {
			sqlExceptSelect += " AND member_id = " + member.getId();
		}
		if(cauponId != null){
			sqlExceptSelect += " AND coupon_id = " + cauponId;
		}
		if(receiveId != null){
			sqlExceptSelect += " AND receiveId = " + receiveId;
		}
		return super.count(sqlExceptSelect);
	}

}