package com.wtshop.api.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Member;
import com.wtshop.service.MemberService;
import com.wtshop.util.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller - 会员注销
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/logout")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class LogoutAPIController extends BaseAPIController {

	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 注销
	 */
	public void index() {
		WebUtils.removeCookie(getRequest(), getResponse(), Member.USERNAME_COOKIE_NAME);
		WebUtils.removeCookie(getRequest(), getResponse(), Member.NICKNAME_COOKIE_NAME);
		if (memberService.isAuthenticated()) {
			getSession().removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		}
		String openId = (String) getRequest().getSession().getAttribute(Member.OPEN_ID);
		Member member = memberService.findByOpenId(openId);
		if (member != null) {
			getSession().removeAttribute(Member.OPEN_ID);
		}
		redirect("/api.jhtml");
	}

}