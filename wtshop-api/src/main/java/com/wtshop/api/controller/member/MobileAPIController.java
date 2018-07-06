package com.wtshop.api.controller.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.CommonAttributes;
import com.wtshop.Setting;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.util.ApiResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Member;
import com.wtshop.model.Sms;
import com.wtshop.service.MemberService;
import com.wtshop.service.SmsService;
import com.wtshop.util.SMSUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Controller 修改手机 - 会员中心
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/mobile")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class MobileAPIController extends BaseAPIController {

	SmsService smsService = enhance(SmsService.class);
	MemberService memberService = enhance(MemberService.class);


	/**
	 * 旧手机验证
	 */
	public void confirm() {
		String mobile = getPara("mobiles");
		String vcode = getPara("vcodes");
		if(smsService.smsExists(mobile, vcode, Setting.SmsType.resetMobile)) {
			renderJson(ApiResult.success("验证成功!"));
		} else {
			renderJson(ApiResult.fail("验证码错误!"));
		}

	}
	
	/**
	 * 更新新手机
	 */
	public void update() {
		String mobile = getPara("mobiles");
		String vcode = getPara("vcodes");
		if(smsService.smsExists(mobile, vcode, Setting.SmsType.resetMobile)) {
			Member member = memberService.getCurrent();
			member.setPhone(mobile);
			memberService.update(member);
			renderJson(ApiResult.success("修改成功!"));
		} else {
			renderJson(ApiResult.fail("验证码错误!"));
		}

	}
	
	/**
	 * 发送短信
	 */
	public void send() {
		Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
		Cache sm = Redis.use();
		String mobile = getPara("mobiles");
		String smsCode = SMSUtils.randomSMSCode(4);
		Map<String, String> params = new HashMap<>();
		params.put("code", smsCode);
		params.put("product", "任性猫");
		if(sm.get("PONHE:"+mobile) != null){
			renderJson(ApiResult.fail("您的操作过于频繁,请稍后再试!"));
			return;
		}
		ApiResult result = SMSUtils.send(mobile, prop.get("sms.verifyCodeTemplate"), params);
		if(result.resultSuccess()) {
			sm.setex("PONHE:"+mobile,120,"1");
			Sms sms = new Sms();
			sms.setMobile(mobile);
			sms.setSmsCode(smsCode);
			sms.setSmsType(Setting.SmsType.resetMobile.ordinal());
			smsService.saveOrUpdate(sms);
			renderJson(ApiResult.success("短信发送成功！【"+smsCode+"】"));
		}else {
			renderJson(ApiResult.fail("您发送的过于频繁,请稍后再试!"));
		}

	}
	
}
