package com.wtshop.controller.shop.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.controller.shop.BaseController;
import com.wtshop.interceptor.MemberInterceptor;
import com.wtshop.model.Member;
import com.wtshop.service.MemberService;
import com.wtshop.service.PointLogService;

/**
 * Controller - 会员中心 - 我的积分
 * 
 * 
 */
@ControllerBind(controllerKey = "/member/point_log")
@Before(MemberInterceptor.class)
public class PointLogController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = enhance(MemberService.class);
	private PointLogService pointLogService = enhance(PointLogService.class);

	/**
	 * 列表
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("page", pointLogService.findPage(member, pageable));
		setAttr("member", member);
		render("/shop/${theme}/member/point_log/list.ftl");
	}

}