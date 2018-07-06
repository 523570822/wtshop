package com.wtshop.controller.shop.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.controller.shop.BaseController;
import com.wtshop.interceptor.MemberInterceptor;
import com.wtshop.model.Member;
import com.wtshop.service.ConsultationService;
import com.wtshop.service.MemberService;

/**
 * Controller - 会员中心 - 咨询
 * 
 * 
 */
@ControllerBind(controllerKey = "/member/consultation")
@Before(MemberInterceptor.class)
public class ConsultationController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = new MemberService();
	private ConsultationService consultationService = new ConsultationService();

	/**
	 * 列表
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("page", consultationService.findPage(member, null, null, pageable));
		setAttr("member", member);
		render("/shop/${theme}/member/consultation/list.ftl");
	}

}