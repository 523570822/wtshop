package com.wtshop.controller.wap.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.controller.wap.BaseController;
import com.wtshop.interceptor.WapMemberInterceptor;
import com.wtshop.model.Member;
import com.wtshop.service.CouponCodeService;
import com.wtshop.service.MemberService;

/**
 * Controller - 会员中心 - 优惠码
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/member/coupon_code")
@Before(WapMemberInterceptor.class)
public class CouponCodeController extends BaseController {
	
	private static final int PAGE_SIZE = 10;
	
	private MemberService memberService = enhance(MemberService.class);
	private CouponCodeService couponCodeService = enhance(CouponCodeService.class);
	
	/**
	 * 列表
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Boolean isUsed = getParaToBoolean("isUsed", null);
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("pages", couponCodeService.findPage(member, pageable, isUsed,null,null,null));
		setAttr("isUsed", isUsed == null ? "all" : isUsed);
		setAttr("title" , "会员中心 - 优惠码");
		render("/wap/member/coupon_code/list.ftl");
	}

}
