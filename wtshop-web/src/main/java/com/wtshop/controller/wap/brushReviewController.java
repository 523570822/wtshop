package com.wtshop.controller.wap;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.wtshop.Principal;
import com.wtshop.Setting;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Member;
import com.wtshop.service.MemberService;
import com.wtshop.util.SystemUtils;
import com.wtshop.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerBind(controllerKey = "/wap/brushLogin")
@Before(WapInterceptor.class)
public class brushReviewController extends BaseController {

	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 登录页面
	 */
	public void index() {
		String urlForward = getPara("url_forward");
		setSessionAttr("url_forward", urlForward);
		setAttr("title" , "会员登录");
		LogKit.info("OPEN_ID >>> " + getSessionAttr(Member.OPEN_ID));
		render("/wap/brushLogin/index.ftl");
	}

	/**
	 * 登录提交
	 */
	public void submit() {
		String username = getPara("username");
		String password = getPara("password");
		String urlForward = getSessionAttr("url_forward");

		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();

		Res resZh = I18n.use();
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.common.invalid"));
			renderJson(map);
			return;
		}

		Member member;
		Setting setting = SystemUtils.getSetting();
		if (setting.getIsEmailLogin() && username.contains("@")) {
			List<Member> members = memberService.findListByEmail(username);
			if (members.isEmpty()) {
				member = null;
			} else if (members.size() == 1) {
				member = members.get(0);
			} else {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, resZh.format("shop.login.unsupportedAccount"));
				renderJson(map);
				return;
			}
		} else {
			member = memberService.findByUsername(username);
		}
		if (member == null) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.login.unknownAccount"));
			renderJson(map);
			return;
		}
		if (!member.getIsEnabled()) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.login.disabledAccount"));
			renderJson(map);
			return;
		}
		if (member.getIsLocked()) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				int loginFailureLockTime = setting.getAccountLockTime();
				if (loginFailureLockTime == 0) {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, resZh.format("shop.login.lockedAccount"));
					renderJson(map);
					return;
				}
				Date lockedDate = member.getLockedDate();
				Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
				if (new Date().after(unlockDate)) {
					member.setLoginFailureCount(0);
					member.setIsLocked(false);
					member.setLockedDate(null);
					memberService.update(member);
				} else {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, resZh.format("shop.login.lockedAccount"));
					renderJson(map);
					return;
				}
			} else {
				member.setLoginFailureCount(0);
				member.setIsLocked(false);
				member.setLockedDate(null);
				memberService.update(member);
			}
		}

		if (!DigestUtils.md5Hex(password).equals(member.getPassword())) {
			int loginFailureCount = member.getLoginFailureCount() + 1;
			if (loginFailureCount >= setting.getAccountLockCount()) {
				member.setIsLocked(true);
				member.setLockedDate(new Date());
			}
			member.setLoginFailureCount(loginFailureCount);
			memberService.update(member);
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, resZh.format("shop.login.accountLockCount", setting.getAccountLockCount()));
				renderJson(map);
				return;
			} else {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, resZh.format("shop.login.incorrectCredentials", setting.getAccountLockCount()));
				renderJson(map);
				return;
			}
		}
		String openId = (String) request.getSession().getAttribute(Member.OPEN_ID);
		if (StrKit.isBlank(member.getOpenId()) && StrKit.notBlank(openId)) {
			member.setOpenId(openId);
		}
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		memberService.update(member);

		setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		if (StringUtils.isNotEmpty(member.getNickname())) {
			WebUtils.addCookie(request, response, Member.NICKNAME_COOKIE_NAME, member.getNickname());
		}

		map.put(STATUS, SUCCESS);
		map.put(MESSAGE, "登录成功!");

		map.put("referer", StrKit.notBlank(urlForward) ? urlForward : "member/review/add.jhtml");
		renderJson(map);
	}

}
