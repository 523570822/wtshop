package com.wtshop.controller.shop.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.controller.shop.BaseController;
import com.wtshop.interceptor.MemberInterceptor;
import com.wtshop.model.Member;
import com.wtshop.service.MemberService;
import com.wtshop.service.ReviewService;

/**
 * Controller - 会员中心 - 评论
 * 
 * 
 */
@ControllerBind(controllerKey = "/member/review")
@Before(MemberInterceptor.class)
public class ReviewController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = enhance(MemberService.class);
	private ReviewService reviewService = enhance(ReviewService.class);

	/**
	 * 列表
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("page", reviewService.findPage(member, null, null, null, pageable));
		setAttr("member", member);
		render("/shop/${theme}/member/review/list.ftl");
	}

}