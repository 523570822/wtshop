package com.wtshop.api.controller.member;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.api.common.result.CouponListResult;
import com.wtshop.api.common.result.TicketCouponResult;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.model.Ticketreceive;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.api.common.result.CouponCodeResult;
import com.wtshop.api.common.result.member.CouponCodeListResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Coupon;
import com.wtshop.model.CouponCode;
import com.wtshop.model.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Controller - 会员中心 - 优惠码
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/coupon_code")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class CouponAPICodeController extends BaseAPIController {
	
	private static final int PAGE_SIZE = 10;
	
	private MemberService memberService = enhance(MemberService.class);
	private CouponCodeService couponCodeService = enhance(CouponCodeService.class);
	private CouponService couponService = enhance(CouponService.class);
	private ProductCategoryService productCategoryService = Enhancer.enhance(ProductCategoryService.class);
	private TicketReceiveService ticketReceiveService = enhance(TicketReceiveService.class);
	
	/**
	 * 我的优惠券列表
	 */
	public void list(){
		Integer pageNumber = getParaToInt("pageNumbers");
		Long type = getParaToLong("type");// 244：美发  243：护肤
		Boolean isUsed = getParaToBoolean("isUsed", null);
		List<Long> productCategory = productCategoryService.getProductCategory(type);
		//242 通用优惠券
		productCategory.add(242L);

		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<CouponCode> pages = couponCodeService.findPage(member, pageable, null ,null,true,null);
		for(CouponCode couponCode : pages.getList()){
			Coupon coupon = couponService.find(couponCode.getCouponId());
			coupon.setStatus(Coupon.status.expire.ordinal());
			couponService.update(coupon);
		}

		Page<Coupon> page = couponService.findPage(member, pageable, false, productCategory,null,null);

		renderJson(ApiResult.success(page));
	}

	/**
	 * 所有优惠券
	 */

	public void all(){
        Member member = memberService.getCurrent();
        Integer pageNumber = getParaToInt("pageNumbers");
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Long type = getParaToLong("type");// 244：美发  243：护肤
		List<Long> productCategory = productCategoryService.getProductCategory(type);
		//242 通用优惠券
		productCategory.add(242L);

		//普通优惠券
		Page<Coupon> page = couponService.findPage(true, null, false, pageable ,productCategory);
		List<Coupon> couponList = page.getList();
		List<CouponCodeResult> couponLists = new ArrayList<>();
		for(Coupon coupon : couponList){
			Long count = couponCodeService.count(coupon.getId(), member ,null);
			CouponCodeResult couponCodeResult = new CouponCodeResult(coupon, count);
			couponLists.add(couponCodeResult);
		}

		renderJson(ApiResult.success(page));
	}

	/**
	 * 领取优惠券
	 */
	public void add(){
		Long id = getParaToLong("id");
		Member member = memberService.getCurrent();
		Coupon coupon = couponService.find(id);
		long size = couponCodeService.count(coupon, member, true, false, false);
		long count = couponCodeService.count(coupon, null, true, false, null);
		if (size >= 1){
			renderJson(ApiResult.fail("您已经领取过了"));
			return;
		}
		if (!coupon.getIsEnabled()){
			renderJson(ApiResult.fail("优惠券不可用!"));
			return;
		}
		if (!coupon.hasBegun()) {
			renderJson(ApiResult.fail("该优惠券还未启用,请留意启用时间!"));
			return;
		}
		if( count >= coupon.getCount()){
			renderJson(ApiResult.fail("该优惠券已发放完毕!"));
			return;
		}
		if (coupon.hasExpired()) {
			renderJson(ApiResult.fail("该优惠券已过期!"));
			return;
		}
		couponCodeService.generate(coupon, member,1);
		renderJson(ApiResult.success("领取优惠券成功!"));
	}

}
