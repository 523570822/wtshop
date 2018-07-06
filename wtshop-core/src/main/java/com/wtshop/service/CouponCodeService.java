package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.dao.CouponCodeDao;
import com.wtshop.model.*;
import com.wtshop.util.Assert;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service - 优惠码
 * 
 * 
 */
public class CouponCodeService extends BaseService<CouponCode> {

	/**
	 * 构造方法
	 */
	public CouponCodeService() {
		super(CouponCode.class);
	}
	
	private CouponCodeDao couponCodeDao = Enhancer.enhance(CouponCodeDao.class);
	private MemberService memberService = Enhancer.enhance(MemberService.class);


	
	
	/**
	 * 判断优惠码是否存在
	 * 
	 * @param code
	 *            号码(忽略大小写)
	 * @return 优惠码是否存在
	 */
	public boolean codeExists(String code) {
		return couponCodeDao.codeExists(code);
	}

	/**
	 * 根据号码查找优惠码
	 * 
	 * @param code
	 *            号码(忽略大小写)
	 * @return 优惠码，若不存在则返回null
	 */
	public CouponCode findByCode(String code) {
		return couponCodeDao.findByCode(code);
	}

	/**
	 * 生成优惠码
	 * 
	 * @param coupon
	 *            优惠券
	 * @param member
	 *            会员
	 * @return 优惠码
	 */
	public CouponCode generate(Coupon coupon, Member member,long ticketId,long receiveId) {
		Assert.notNull(coupon);

		CouponCode couponCode = new CouponCode();
		couponCode.setCode(coupon.getPrefix() + DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)).toUpperCase());
		couponCode.setIsUsed(false);
		couponCode.setCouponId(coupon.getId());
		couponCode.setMemberId(StrKit.notNull(member) ? member.getId() : null);

		if (receiveId>0){
			couponCode.setReceiveId(receiveId);
		}

		return super.save(couponCode);
	}


	public CouponCode generate(Record coupon, Member member, long ticketId, long receiveId) {
		Assert.notNull(coupon);

		CouponCode couponCode = new CouponCode();
		couponCode.setCode(coupon.getStr("prefix")+ DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)).toUpperCase());
		couponCode.setIsUsed(false);
		couponCode.setCouponId(0L);
		couponCode.setMemberId(StrKit.notNull(member) ? member.getId() : null);
		if (receiveId>0){
			couponCode.setReceiveId(receiveId);
		}
		return super.save(couponCode);
	}



	/**
	 * 生成优惠码
	 * 
	 * @param coupon
	 *            优惠券
	 * @param member
	 *            会员
	 * @param count
	 *            数量
	 * @return 优惠码
	 */
	public List<CouponCode> generate(Coupon coupon, Member member, Integer count) {
		Assert.notNull(coupon);
		Assert.notNull(count);

		List<CouponCode> couponCodes = new ArrayList<CouponCode>();
		for (int i = 0; i < count; i++) {
			CouponCode couponCode = generate(coupon, member,0,0);
			couponCodes.add(couponCode);
		}
		return couponCodes;
	}

	public List<CouponCode> generate(Record coupon, Member member, Integer count) {
		Assert.notNull(coupon);
		Assert.notNull(count);

		List<CouponCode> couponCodes = new ArrayList<CouponCode>();
		for (int i = 0; i < count; i++) {
			CouponCode couponCode = generate(coupon, member,0,0);
			couponCodes.add(couponCode);
		}
		return couponCodes;
	}

//	/**
//	 * 兑换优惠码
//	 *
//	 * @param coupon
//	 *            优惠券
//	 * @param member
//	 *            会员
//	 * @param operator
//	 *            操作员
//	 * @return 优惠码
//	 */
//	public CouponCode exchange(Coupon coupon, Member member, Admin operator) {
//		Assert.notNull(coupon);
//		Assert.notNull(coupon.getPoint());
//		Assert.state(coupon.getIsEnabled() && coupon.getIsExchange() && !coupon.hasExpired());
//		Assert.notNull(member);
//		Assert.notNull(member.getPoint());
//		Assert.state(member.getPoint() >= coupon.getPoint());
//
//		if (coupon.getPoint() > 0) {
//			memberService.addPoint(member, -coupon.getPoint(), PointLog.Type.exchange, operator, null);
//		}
//
//		return generate(coupon, member,0,0);
//	}


	/**
	 * 查找优惠码分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	public Page<CouponCode> findPage(Member member, Pageable pageable, Boolean isUsed, List<Long> productCategory  ,Boolean hasExpired,Boolean isEnabled) {
		return couponCodeDao.findPage(member, pageable, isUsed , productCategory ,hasExpired, isEnabled);
	}

	/**
	 * 查找所有优惠券
	 *
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	public Page<CouponCode> findPage(Pageable pageable, Boolean isUsed, Integer type) {
		return couponCodeDao.findPage(pageable, isUsed , type);
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
		return couponCodeDao.count(coupon, member, hasBegun, hasExpired, isUsed);
	}

	public Long count(Ticket coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
		return couponCodeDao.count(coupon, member, hasBegun, hasExpired, isUsed);
	}
	public Long count(Long cauponId , Member member ,Long receiveId) {
		return couponCodeDao.count(cauponId, member ,null);
	}


}