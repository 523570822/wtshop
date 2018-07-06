package com.wtshop.api.controller;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.Principal;
import com.wtshop.Setting;
import com.wtshop.util.ApiResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Member;
import com.wtshop.model.Sms;
import com.wtshop.service.MemberRankService;
import com.wtshop.service.MemberService;
import com.wtshop.service.SmsService;
import com.wtshop.util.SMSUtils;
import com.wtshop.util.SystemUtils;
import com.wtshop.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controller - 会员注册
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/register")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class RegisterAPIController extends BaseAPIController {
	
	private MemberService memberService = enhance(MemberService.class);
	private MemberRankService memberRankService = enhance(MemberRankService.class);
	private SmsService smsService = enhance(SmsService.class);
	
	/**
	 * 检查用户名是否被禁用或已存在
	 */
	public void checkUsername() {
		String username = getPara("param");
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isEmpty(username)) {
			renderJson(ApiResult.fail("手机号不为空!"));
			return;
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			renderJson(ApiResult.fail("手机号已存在或禁用中!"));
		} else {
			renderJson(ApiResult.success("允许使用!"));
		}
	}
	
	/**
	 * 发送短信
	 */
	public void send() {
		String mobile = getPara("mobiles");
		String vcode = SMSUtils.randomSMSCode(4);
		Sms sms = new Sms();
		sms.setMobile(mobile);
		sms.setSmsCode(vcode);
		sms.setSmsType(Setting.SmsType.memberRegister.ordinal());
		smsService.saveOrUpdate(sms);
		smsService.send(mobile, vcode);
		renderJson("1234");
	}



	/**
	 * 注册提交
	 */
	public void submit() {
		String mobile = getPara("mobiles");
		String password = getPara("passwords");
		String pwdconfirm = getPara("pwdconfirm");
		String vcode = getPara("vcodes");
		String openId = getPara("openId");
		
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();

		JSONObject object = new JSONObject();

		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(vcode)) {
			renderJson(ApiResult.fail("手机号或验证码不为空!"));
			return;
		}
		
		if (!smsService.smsExists(mobile, vcode, Setting.SmsType.memberRegister)) {
			renderJson(ApiResult.fail("手机号验证码错误!"));
			return;
		}
		
		Res resZh = I18n.use();
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsRegisterEnabled()) {
			renderJson(ApiResult.fail(resZh.format("shop.register.disabledExist")));
			return;
		}
		
		if (password == null || pwdconfirm == null || (! password.equals(pwdconfirm))) {
			renderJson(ApiResult.fail("两次密码不一致!"));
			return;
		} 

		if (mobile.length() < setting.getUsernameMinLength() || mobile.length() > setting.getUsernameMaxLength()) {
			renderJson(ApiResult.fail(resZh.format("shop.common.invalid")));
			return;
		}
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			renderJson(ApiResult.fail( resZh.format("shop.common.invalid")));
			return;
		}
		if (memberService.usernameDisabled(mobile) || memberService.usernameExists(mobile)) {
			renderJson(ApiResult.fail(resZh.format("shop.register.disabledExist")));
			return;
		}
		Member member = new Member();
		if(StringUtils.isNotEmpty(openId)){
			member = memberService.findByOpenId(openId);
			member.setPhone(mobile);
			member.setPassword(DigestUtils.md5Hex(password));
		}else {
			member.removeAttributeValue();
			member.setUsername(StringUtils.lowerCase(mobile));
			member.setPassword(DigestUtils.md5Hex(password));
			member.setNickname(StringUtils.lowerCase(mobile));
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
			member.setMemberRankId(memberRankService.findDefault() != null ? memberRankService.findDefault().getId() : null);
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
			member.setRegisterType(4);
		}

		memberService.save(member);
		//消息推送开关
		Cache actCache = Redis.use();
		actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),true);
		actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),true);
		actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),true);
		actCache.set("SOUND:" + member.getId(),"default");
		setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		if (StringUtils.isNotEmpty(member.getNickname())) {
			WebUtils.addCookie(request, response, Member.NICKNAME_COOKIE_NAME, member.getNickname());
		}

		renderJson(ApiResult.success(object,resZh.format("shop.register.success")));
	}

	/**
	 * 忘记密码提交
	 */
	public void reSubmit() {
		String mobile = getPara("mobiles");
		String password = getPara("passwords");
		String pwdconfirm = getPara("pwdconfirms");
		String vcode = getPara("vcodes");

		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(vcode)) {
			renderJson(ApiResult.fail("手机号或验证码不为空!"));
			return;
		}

		if (!smsService.smsExists(mobile, vcode, Setting.SmsType.memberRegister)) {
			renderJson(ApiResult.fail("手机号验证码错误!"));
			return;
		}

		Res resZh = I18n.use();
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsRegisterEnabled()) {
			renderJson(ApiResult.fail(resZh.format("shop.register.disabledExist")));
			return;
		}

		if (password == null || pwdconfirm == null || (! password.equals(pwdconfirm))) {
			renderJson(ApiResult.fail("两次密码不一致!"));
			return;
		}

		if (mobile.length() < setting.getUsernameMinLength() || mobile.length() > setting.getUsernameMaxLength()) {
			renderJson(ApiResult.fail(resZh.format("shop.common.invalid")));
			return;
		}
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			renderJson(ApiResult.fail( resZh.format("shop.common.invalid")));
			return;
		}

		Member member = memberService.findByPhone(mobile);
		member.setPassword(password);
		memberService.save(member);
		renderJson(ApiResult.success("密码修改成功!"));
	}
	
}
