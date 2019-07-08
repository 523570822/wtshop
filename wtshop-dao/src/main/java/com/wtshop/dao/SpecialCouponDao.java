package com.wtshop.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.model.Identifier;
import com.wtshop.model.SpecialCoupon;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Dao - 品牌
 * 
 * 
 */
public class SpecialCouponDao extends   BaseDao<SpecialCoupon> {

	/**
	 * 构造方法
	 */
	public SpecialCouponDao() {
		super(SpecialCoupon.class);
	}

	/**
	 * 查询品牌列表
	 */
	public List<Record> findBrandSort(){

		String sql = " select id , name from brand order by name";
		return Db.find(sql);

	}

    public List<SpecialCoupon> findByIdfCode(String idfCode) {
		if (StringUtils.isEmpty(idfCode)) {
			return null;
		}
		try {
			String sql = "SELECT * FROM special_coupon i where   i.code= UPPER(?)";
			return modelManager.find(sql, idfCode);
		} catch (Exception e) {
			return null;
		}
    }

	public List<SpecialCoupon> findByOnCodeShare(String onCodeShare,Long memberId,String status) {
		if (StringUtils.isEmpty(onCodeShare)) {
			return null;
		}
		try {
			String sql = "SELECT *, m.store FROM special_coupon i LEFT JOIN member m ON i.share_code = m.share_code where   i.share_code= UPPER(?) and i.member_id= '"+memberId+"'";
			if (StringUtils.isNotEmpty(status)) {
				sql=sql+" and i.state= 2";
			}
			return modelManager.find(sql, onCodeShare);
		} catch (Exception e) {
			return null;
		}
	}


	public List<SpecialCoupon> findByOnCodeShareSB(String onCodeShare,Long memberId) {
		if (StringUtils.isEmpty(onCodeShare)) {
			return null;
		}
		try {
			String sql = "SELECT *, m.store FROM special_coupon i LEFT JOIN member m ON i.share_code = m.share_code where  i.status<>'1' and  i.share_code= UPPER(?) and i.member_id= '"+memberId+"'";
			return modelManager.find(sql, onCodeShare);

		} catch (Exception e) {
			return null;
		}
	}
	public List<SpecialCoupon> findByMemberId(Long memberId) {
		try {
			String sql = "SELECT * FROM ( SELECT i.*, m.store FROM special_coupon i LEFT JOIN member m ON i.share_code = m.share_code WHERE i.member_id = ? ORDER BY i.end_date DESC ) j GROUP BY j.share_code\n";
			return modelManager.find(sql, memberId);
		} catch (Exception e) {
			return null;
		}
	}
	public int update(String sql) {
		try {

			return Db.update(sql);
		} catch (Exception e) {
			return 0;
		}
	}
}