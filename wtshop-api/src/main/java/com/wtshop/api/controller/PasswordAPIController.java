package com.wtshop.api.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.CommonAttributes;
import com.wtshop.Setting;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.constants.Code;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Member;
import com.wtshop.model.Sms;
import com.wtshop.service.MailService;
import com.wtshop.service.MemberService;
import com.wtshop.service.SmsService;
import com.wtshop.util.ApiResult;
import com.wtshop.util.SMSUtils;
import com.wtshop.util.SystemUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 密码
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/password")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class PasswordAPIController extends BaseAPIController {

	private MemberService memberService = enhance(MemberService.class);
	private MailService mailService = enhance(MailService.class);
	private SmsService smsService = enhance(SmsService.class);
	private Res resZh = I18n.use();

	
	/**
	 * 发送短信
	 * {"msg":"测试环境验证码是：0410","code":1,"data":{}}
	 */
	public void send_sms() {
		Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
		String mobile = getPara("phone");
		Cache sm = Redis.use();
		if (StringUtils.isEmpty(mobile)) {
			renderJson(ApiResult.fail("手机号不能为空"));
			return;
		}
		Member member = memberService.findByUsername(mobile);
		if (member == null) {
			renderJson(ApiResult.fail(resZh.format("shop.password.memberNotExist")));
			return;
		}
		if(sm.get("PONHE:"+mobile) != null){
			renderJson(ApiResult.fail("您的操作过于频繁,请稍后再试!"));
			return;
		}
		String smsCode = SMSUtils.randomSMSCode(4);
		Map<String, String> params = new HashMap<>();
		params.put("code", smsCode);
		params.put("product", "安吃");
		ApiResult result = SMSUtils.send(mobile, prop.get("sms.verifyCodeTemplate"), params);
		if(result.getCode() == Code.SUCCESS) {
			sm.setex("PONHE:"+mobile,120,"1");
			Sms sms = new Sms();
			sms.setMobile(mobile);
			sms.setSmsCode(smsCode);
			sms.setSmsType(Setting.SmsType.resetPassword.ordinal());
			smsService.saveOrUpdate(sms);
			renderJson(ApiResult.successMsg("测试环境验证码是：" + smsCode));
		}else {
			renderJson(ApiResult.fail("您发送的过于频繁,请稍后再试!"));
		}
	}

	
	/**
	 * 重置密码提交
	 */
	public void submit() {
		String phone = getPara("phone");
		String smsCode = getPara("smsCode");
		String password = getPara("password");
		String pwdconfirm = getPara("pwdconfirm");

		Member member = memberService.findByUsername(phone);
		if (member == null) {
			renderJson(ApiResult.fail(resZh.format("shop.password.memberNotExist")));
			return;
		}
		if (!StringUtils.equals(password, pwdconfirm)) {
			renderJson(ApiResult.fail("两次输入密码不相同!"));
			return;
		}

		if(StringUtils.isEmpty(smsCode)){
			renderJson(ApiResult.fail("验证码不能为空!"));
			return;
		}

		//检查手机号码有效性
		if (!SMSUtils.isMobileNo(phone)) {
			renderJson(ApiResult.fail("请检查手机号是否正确!"));
			return;
		}

		if (!smsService.smsExists(phone, smsCode, Setting.SmsType.resetPassword)) {
			renderJson(ApiResult.fail("验证码输入错误!"));
			return;
		}

		Setting setting = SystemUtils.getSetting();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			renderJson(ApiResult.fail(resZh.format("shop.password.invalidPassword")));
			return;
		}


		member.setPassword(DigestUtils.md5Hex(password));
		member.setSafeKeyExpire(null);
		member.setSafeKeyValue(null);
		memberService.update(member);
		renderJson(ApiResult.success());
	}
	
}
