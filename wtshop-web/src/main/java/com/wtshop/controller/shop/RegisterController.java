package com.wtshop.controller.shop;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Message;
import com.wtshop.Principal;
import com.wtshop.Setting;
import com.wtshop.interceptor.ThemeInterceptor;
import com.wtshop.model.Cart;
import com.wtshop.model.Member;
import com.wtshop.model.MemberAttribute;
import com.wtshop.model.PointLog;
import com.wtshop.service.CartService;
import com.wtshop.service.MemberAttributeService;
import com.wtshop.service.MemberRankService;
import com.wtshop.service.MemberService;
import com.wtshop.service.RSAService;
import com.wtshop.shiro.core.SubjectKit;
import com.wtshop.util.SystemUtils;
import com.wtshop.util.WebUtils;

/**
 * Controller - 会员注册
 * 
 * 
 */
@ControllerBind(controllerKey = "/register")
@Before(ThemeInterceptor.class)
public class RegisterController extends BaseController {

	private RSAService rsaService = enhance(RSAService.class);
	private MemberService memberService = enhance(MemberService.class);
	private MemberRankService memberRankService = enhance(MemberRankService.class);
	private MemberAttributeService memberAttributeService = enhance(MemberAttributeService.class);
	private CartService cartService = enhance(CartService.class);

	/**
	 * 检查用户名是否被禁用或已存在
	 */
	public void checkUsername() {
		String username = getPara("username");
		if (StringUtils.isEmpty(username)) {
			renderJson(false);
			return;
		}
		renderJson(!memberService.usernameDisabled(username) && !memberService.usernameExists(username));
	}

	/**
	 * 检查E-mail是否存在
	 */
	public void checkEmail() {
		String email = getPara("email");
		if (StringUtils.isEmpty(email)) {
			renderJson(false);
			return;
		}
		renderJson(!memberService.emailExists(email));
	}

	/**
	 * 注册页面
	 */
	public void index() {
		setAttr("genders", Member.Gender.values());
		setAttr("captchaId", UUID.randomUUID().toString());
		render("/shop/${theme}/register/index.ftl");
	}

	/**
	 * 注册提交
	 */
	public void submit() {
		String captcha = getPara("captcha");
		String username = getPara("username");
		String email = getPara("email");
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		
		if (!SubjectKit.doCaptcha("captcha", captcha)) {
			renderJson(Message.error("shop.captcha.invalid"));
			return;
		}
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsRegisterEnabled()) {
			renderJson(Message.error("shop.register.disabled"));
			return;
		}
		String password = rsaService.decryptParameter("enPassword", request);
		rsaService.removePrivateKey(request);

		if (username.length() < setting.getUsernameMinLength() || username.length() > setting.getUsernameMaxLength()) {
			renderJson(Message.error("shop.common.invalid"));
			return;
		}
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			renderJson(Message.error("shop.common.invalid"));
			return;
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			renderJson(Message.error("shop.register.disabledExist"));
			return;
		}
		if (!setting.getIsDuplicateEmail() && memberService.emailExists(email)) {
			renderJson(Message.error("shop.register.emailExist"));
			return;
		}

		Member member = new Member();
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				renderJson(Message.error("shop.common.invalid"));
				return;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute, memberAttributeValue);
		}
		member.setUsername(StringUtils.lowerCase(username));
		member.setPassword(DigestUtils.md5Hex(password));
		member.setEmail(StringUtils.lowerCase(email));
		member.setNickname(null);
		member.setPoint(BigDecimal.ZERO);
		member.setBalance(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsEnabled(true);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginPluginId(null);
		member.setOpenId(null);
		member.setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		member.setSafeKey(null);
		member.setMemberRankId(memberRankService.findDefault().getId());
		member.setCart(null);
		member.setOrders(null);
		member.setPaymentLogs(null);
		member.setDepositLogs(null);
		member.setCouponCodes(null);
		member.setReceivers(null);
		member.setReviews(null);
		member.setConsultations(null);
		member.setFavoriteGoods(null);
		member.setProductNotifies(null);
		member.setInMessages(null);
		member.setOutMessages(null);
		member.setPointLogs(null);
		memberService.save(member);

//		if (setting.getRegisterPoint() > 0) {
//			memberService.addPoint(member, setting.getRegisterPoint(), PointLog.Type.reward, null, null);
//		}

		Cart cart = cartService.getCurrent();
		if (cart != null && cart.getMember() == null) {
			cartService.merge(member, cart);
			WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
		}

		/*Map<String, Object> attributes = new HashMap<String, Object>();
		Enumeration<?> keys = session.getAttributeNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			attributes.put(key, session.getAttribute(key));
		}
		session.invalidate();
		session = request.getSession();
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}*/

		//session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
		setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		if (StringUtils.isNotEmpty(member.getNickname())) {
			WebUtils.addCookie(request, response, Member.NICKNAME_COOKIE_NAME, member.getNickname());
		}

		renderJson(Message.success("shop.register.success"));
	}

}