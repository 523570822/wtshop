package com.wtshop.controller.wap;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.model.Member;
import com.wtshop.service.MemberService;
import com.wtshop.util.WebUtils;

/**
 * Controller - 会员注销
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/logout")
public class LogoutController extends BaseController {

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
		redirect("/wap.jhtml");
	}

}