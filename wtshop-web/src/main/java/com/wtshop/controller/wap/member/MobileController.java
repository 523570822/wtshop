package com.wtshop.controller.wap.member;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Setting;
import com.wtshop.controller.wap.BaseController;
import com.wtshop.interceptor.WapMemberInterceptor;
import com.wtshop.model.Member;
import com.wtshop.model.Sms;
import com.wtshop.service.MemberService;
import com.wtshop.service.SmsService;
import com.wtshop.util.SMSUtils;

/**
 * Controller 修改手机 - 会员中心
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/member/mobile")
@Before(WapMemberInterceptor.class)
public class MobileController extends BaseController{

	SmsService smsService = enhance(SmsService.class);
	MemberService memberService = enhance(MemberService.class);
	
	/**
	 * 编辑
	 */
	public void edit() {
		setAttr("title" , "修改手机 - 会员中心");
		render("/wap/member/mobile/edit.ftl");
	}
	
	/**
	 * 更新
	 */
	public void update() {
		String mobile = getPara("mobile");
		String vcode = getPara("vcode");
		Map<String, String> map = new HashMap<String, String>();
		if(smsService.smsExists(mobile, vcode, Setting.SmsType.resetMobile)) {
			Member member = memberService.getCurrent();
			member.setMobile(mobile);
			memberService.update(member);
			map.put(STATUS, SUCCESS);
			map.put("referer", "/wap/member/profile/edit.jhtml");
		} else {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "验证码错误!");
		}
		renderJson(map);
	}
	
	/**
	 * 发送短信
	 */
	public void send() {
		String mobile = getPara("mobile");
		String vcode = SMSUtils.randomSMSCode(4);
		Sms sms = new Sms();
		sms.setMobile(mobile);
		sms.setSmsCode(vcode);
		sms.setSmsType(Setting.SmsType.resetMobile.ordinal());
		smsService.saveOrUpdate(sms);
		smsService.send(mobile, vcode);
		renderJson();
	}
	
}
