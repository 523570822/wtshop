package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.dao.IdentifierDao;
import com.wtshop.dao.SpecialCouponDao;
import com.wtshop.model.Identifier;
import com.wtshop.model.SpecialCoupon;
import com.wtshop.util.PinYinUtil;
import com.wtshop.util.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Service - 品牌
 * 
 * 
 */
public class SpecialCouponService extends BaseService<SpecialCoupon> {

	public SpecialCouponService() {
		super(SpecialCoupon.class);
	}
	
	private SpecialCouponDao brandDao = Enhancer.enhance(SpecialCouponDao.class);




	public Map<String,List> findBrandSort(){
		List<Record> brandSort = brandDao.findBrandSort();
		Collections.sort(brandSort, new Comparator<Record>() {
			@Override
			public int compare(Record o1, Record o2) {
				String name1 = StringUtils.isEmpty(o1.get("name"))?o1.get("name").toString():o1.get("name").toString();
				String name2 = StringUtils.isEmpty(o2.get("name"))?o2.get("name").toString():o2.get("name").toString();
				String zm1 = PinYinUtil.getPinYinHeadChar(name1.substring(0,1)).toUpperCase();
				String zm2 = PinYinUtil.getPinYinHeadChar(name2.substring(0,1)).toUpperCase();
				return zm1.compareTo(zm2);
			}
		});
		Map<String, List> stringListMap = PinYinUtil.groupByUserName(brandSort);
		return stringListMap;
	}
	public SpecialCoupon save(SpecialCoupon brand) {
		return super.save(brand);
	}

	public SpecialCoupon update(SpecialCoupon brand) {
		return super.update(brand);
	}
	public int update(String sql) {
		return	brandDao.update(sql);

	}
//	public Brand update(Brand brand, String... ignoreProperties) {
//		return super.update(brand, ignoreProperties);
//	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(SpecialCoupon brand) {
		super.delete(brand);
	}
	/**
	 * 获取最后一条记录
	 */
	public SpecialCoupon findBySql(String sql){
		return  brandDao.findBySql(sql);
	}
	public Page<SpecialCoupon> findPage(String sql, Pageable pageable){
		return brandDao.findPage(sql,pageable);
	}

    public List<SpecialCoupon> findByIdfCode(String idfCode) {
		return brandDao.findByIdfCode(idfCode);
    }
    public List<SpecialCoupon> findByOnCodeShare(String onCodeShare,Long memberId,String status) {
		return brandDao.findByOnCodeShare(onCodeShare,memberId,status);
    }  public List<SpecialCoupon> findByOnCodeShareSB(String onCodeShare,Long memberId) {
		return brandDao.findByOnCodeShareSB(onCodeShare,memberId);
    }
    public List<SpecialCoupon> findByMemberId(Long memberId) {
		return brandDao.findByMemberId(memberId);
    }

	public List<SpecialCoupon> findByDay(String s, String i) {
		return brandDao.findByDay(s,i);
	}

    public Object findPages(String select, String sql, Pageable pageable) {
		return brandDao.findPages(select,sql,pageable);
    }
}