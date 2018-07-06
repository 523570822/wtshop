
package com.wtshop.controller.shop;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.Setting;
import com.wtshop.entity.SafeKey;
import com.wtshop.interceptor.ThemeInterceptor;
import com.wtshop.model.Member;
import com.wtshop.service.MailService;
import com.wtshop.service.MemberService;
import com.wtshop.shiro.core.SubjectKit;
import com.wtshop.util.SystemUtils;

/**
 * Controller - 密码
 * 
 * 
 */
@ControllerBind(controllerKey = "/password")
@Before(ThemeInterceptor.class)
public class PasswordController extends BaseController {

	private MemberService memberService = enhance(MemberService.class);
	private MailService mailService = enhance(MailService.class);

	/**
	 * 找回密码
	 */
	public void find() {
		render("/shop/${theme}/password/find.ftl");
	}

	/**
	 * 找回密码提交
	 */
	public void findSubmit() {
		String captcha = getPara("captcha");
		String username = getPara("username");
		String email = getPara("email");
		
		if (!SubjectKit.doCaptcha("captcha", captcha)) {
			renderJson(Message.error("shop.captcha.invalid"));
			return;
		}
		
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(email)) {
			renderJson(Message.error("shop.common.invalid"));
			return;
		}
		Member member = memberService.findByUsername(username);
		if (member == null) {
			renderJson(Message.error("shop.password.memberNotExist"));
			return;
		}
		if (StringUtils.isEmpty(member.getEmail())) {
			renderJson(Message.error("shop.password.emailEmpty"));
			return;
		}
		if (!StringUtils.equalsIgnoreCase(member.getEmail(), email)) {
			renderJson(Message.error("shop.password.invalidEmail"));
			return;
		}
		Setting setting = SystemUtils.getSetting();
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		member.setSafeKeyExpire(safeKey.getExpire());
		member.setSafeKeyValue(safeKey.getValue());
		member.setSafeKey(safeKey);
		memberService.update(member);
		mailService.sendFindPasswordMail(member.getEmail(), member.getUsername(), safeKey);
		renderJson(Message.success("shop.password.mailSuccess"));
	}

	/**
	 * 重置密码
	 */
	public void reset() {
		String username = getPara("username");
		String key = getPara("key");
		Member member = memberService.findByUsername(username);
		if (member == null) {
			redirect(ERROR_VIEW);
			return;
		}
		SafeKey safeKey = new SafeKey();
		safeKey.setExpire(member.getSafeKeyExpire());
		safeKey.setValue(member.getSafeKeyValue());
		if (safeKey == null || safeKey.getValue() == null || !safeKey.getValue().equals(key)) {
			redirect(ERROR_VIEW);
			return;
		}
		if (safeKey.hasExpired()) {
			setAttr("errorMessage", Message.warn("shop.password.hasExpired"));
			redirect(ERROR_VIEW);
			return;
		}
		setAttr("member", member);
		setAttr("key", key);
		render("/shop/${theme}/password/reset.ftl");
	}

	/**
	 * 重置密码提交
	 */
	public void resetSubmit() {
		String captcha = getPara("captcha");
		String username = getPara("username");
		String newPassword = getPara("newPassword");
		String key = getPara("key");
		
		if (!SubjectKit.doCaptcha("captcha", captcha)) {
			renderJson(Message.error("shop.captcha.invalid"));
			return;
		}
		Member member = memberService.findByUsername(username);
		if (member == null) {
			renderJson(ERROR_MESSAGE);
			return;
		}
		Setting setting = SystemUtils.getSetting();
		if (newPassword.length() < setting.getPasswordMinLength() || newPassword.length() > setting.getPasswordMaxLength()) {
			renderJson(Message.warn("shop.password.invalidPassword"));
			return;
		}
		SafeKey safeKey = new SafeKey();
		safeKey.setExpire(member.getSafeKeyExpire());
		safeKey.setValue(member.getSafeKeyValue());
		if (safeKey == null || safeKey.getValue() == null || !safeKey.getValue().equals(key)) {
			renderJson(ERROR_MESSAGE);
			return;
		}
		if (safeKey.hasExpired()) {
			renderJson(Message.error("shop.password.hasExpired"));
			return;
		}
		member.setPassword(DigestUtils.md5Hex(newPassword));
		member.setSafeKeyExpire(null);
		member.setSafeKeyValue(null);
		memberService.update(member);
		renderJson(Message.success("shop.password.resetSuccess"));
	}

}