package com.wtshop.controller.wap.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.controller.wap.BaseController;
import com.wtshop.interceptor.WapMemberInterceptor;
import com.wtshop.service.MemberService;

/**
 * Controller - 会员中心 - 个人资料
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/member/profile")
@Before(WapMemberInterceptor.class)
public class ProfileController extends BaseController {

	MemberService memberService = enhance(MemberService.class);
	
	/**
	 * 编辑
	 */
	public void edit() {
		setAttr("title" , "安全中心 - 会员中心");
		setAttr("member" , memberService.getCurrent());
		render("/wap/member/profile/edit.ftl");
	}
	
	
}
