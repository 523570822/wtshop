package com.wtshop.controller.shop.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.Pageable;
import com.wtshop.controller.shop.BaseController;
import com.wtshop.interceptor.MemberInterceptor;
import com.wtshop.model.Coupon;
import com.wtshop.model.Member;
import com.wtshop.service.CouponCodeService;
import com.wtshop.service.CouponService;
import com.wtshop.service.MemberService;

/**
 * Controller - 会员中心 - 优惠码
 * 
 * 
 */
@ControllerBind(controllerKey = "/member/coupon_code")
@Before(MemberInterceptor.class)
public class CouponCodeController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = enhance(MemberService.class);
	private CouponService couponService = enhance(CouponService.class);
	private CouponCodeService couponCodeService = enhance(CouponCodeService.class);

	/**
	 * 兑换
	 */
	public void exchange() {
		Integer pageNumber = getParaToInt("pageNumber");
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("page", couponService.findPage(true, true, false, pageable ,null));
		render("/shop/${theme}/member/coupon_code/exchange.ftl");
	}

//	/**
//	 * 兑换
//	 */
//	public void exchangeSubmit() {
//		Long id = getParaToLong("id");
//		Coupon coupon = couponService.find(id);
//		if (coupon == null || !coupon.getIsEnabled() || !coupon.getIsExchange() || coupon.hasExpired()) {
//			renderJson(ERROR_MESSAGE);
//			return;
//		}
//		Member member = memberService.getCurrent();
//		if (member.getPoint() < coupon.getPoint()) {
//			renderJson(Message.warn("shop.member.couponCode.point"));
//			return;
//		}
//		couponCodeService.exchange(coupon, member, null);
//		renderJson(SUCCESS_MESSAGE);
//	}

	/**
	 * 列表
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("page", couponCodeService.findPage(member, pageable, null,null,null,null));
		setAttr("member", member);
		render("/shop/${theme}/member/coupon_code/list.ftl");
	}

}