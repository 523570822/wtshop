package com.wtshop.controller.shop.member;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Setting;
import com.wtshop.controller.shop.BaseController;
import com.wtshop.interceptor.MemberInterceptor;
import com.wtshop.model.Member;
import com.wtshop.service.MemberService;
import com.wtshop.util.SystemUtils;

/**
 * Controller - 会员中心 - 密码
 * 
 * 
 */
@ControllerBind(controllerKey = "/member/password")
@Before(MemberInterceptor.class)
public class PasswordController extends BaseController {

	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 验证当前密码
	 */
	public void checkCurrentPassword() {
		String currentPassword = getPara("currentPassword");
		if (StringUtils.isEmpty(currentPassword)) {
			renderJson(false);
			return;
		}
		Member member = memberService.getCurrent();
		renderJson(StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPassword()));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		render("/shop/${theme}/member/password/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		String currentPassword = getPara("currentPassword");
		String password = getPara("password");
		
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(currentPassword)) {
			redirect(ERROR_VIEW);
			return;
		}
		Setting setting = SystemUtils.getSetting();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			redirect(ERROR_VIEW);
			return;
		}
		Member member = memberService.getCurrent();
		if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPassword())) {
			redirect(ERROR_VIEW);
			return;
		}
		member.setPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("edit.jhtml");
	}

}