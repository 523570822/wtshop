package com.wtshop.api.controller.member;

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
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.constants.Code;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Member;
import com.wtshop.model.Sms;
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
 * Controller - 修改密码 - 会员中心
 * 
 * 
 */
@ControllerBind(controllerKey = "/api/member/password")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class PasswordAPIController extends BaseAPIController {

	private MemberService memberService = enhance(MemberService.class);
	private SmsService smsService = enhance(SmsService.class);
	private Res resZh = I18n.use();

	
	/**
	 * 更新会员
	 * {"msg":"","code":1,"data":{"referer":"/wap/member.jhtml"}}
	 */
	public void update() {
		String oldpassword = getPara("oldpasswords");
		String newpassword = getPara("newpasswords");
		String newpassword1 = getPara("newpassword1s");
		if (StringUtils.isEmpty(newpassword) || StringUtils.isEmpty(oldpassword)) {
			renderJson(ApiResult.fail("新旧密码不能为空!"));
			return;
		}
		if (!StringUtils.equals(newpassword, newpassword1)) {
			renderJson(ApiResult.fail("两次输入密码不相同!"));
			return;
		}
		Setting setting = SystemUtils.getSetting();
		if (newpassword.length() < setting.getPasswordMinLength() || newpassword.length() > setting.getPasswordMaxLength()) {
			renderJson(ApiResult.fail("密码长度必须在" +setting.getPasswordMinLength() +"到" + setting.getPasswordMaxLength() + "之间!"));
			return;
		}
		Member member = memberService.getCurrent();
		if (!StringUtils.equals(DigestUtils.md5Hex(oldpassword), member.getPassword())) {
			renderJson(ApiResult.fail("旧密码错误!"));
			return;
		}
		member.setPassword(DigestUtils.md5Hex(newpassword));
		memberService.update(member);

		renderJson(ApiResult.success("密码修改成功!"));
	}

	/**
	 * 修改支付密码
	 */
	public void edit(){
		Member member = memberService.getCurrent();

		String oldPassword = getPara("oldPassword");
		String newPassword = getPara("newPassword");

		if(StringUtils.isEmpty(member.getPayPassword())){
			renderJson(ApiResult.fail("您还没有设置过支付密码!"));
			return;
		}

		if( DigestUtils.md5Hex(oldPassword).equals(member.getPayPassword())){
			member.setPayPassword(DigestUtils.md5Hex(newPassword));
			memberService.update(member);
			renderJson(ApiResult.success("密码修改成功!"));
			return;
		}else {
			renderJson(ApiResult.fail("您输入的密码有误，请重新输入!"));
		}

	}

	/**
	 * 设置支付密码
	 */
	public void add(){
		Member member = memberService.getCurrent();
		String newPassword = getPara("newPassword");
		if(StringUtils.isEmpty(member.getPayPassword())){
			member.setPayPassword(DigestUtils.md5Hex(newPassword));
			memberService.update(member);
			renderJson(ApiResult.success("密码设置成功!"));
		}else{
			renderJson(ApiResult.fail("对不起,您已经设置过密码!"));
		}
	}

	/**
	 * 发送短信
	 * {"msg":"测试环境验证码是：0410","code":1,"data":{}}
	 */
	public void send_sms() {
		Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
		String username = getPara("phone");
		Cache sm = Redis.use();
		if (StringUtils.isEmpty(username)) {
			renderJson(ApiResult.fail("登录名不能为空!"));
			return;
		}
		if(sm.get("PONHE:"+username) != null){
			renderJson(ApiResult.fail("您的操作过于频繁,请稍后再试!"));
			return;
		}

		//检查手机号码有效性
		if (!SMSUtils.isMobileNo(username)) {
			renderJson(ApiResult.fail("请检查手机号是否正确!"));
			return;
		}
		String smsCode = SMSUtils.randomSMSCode(4);
		Map<String, Object> params = new HashMap<>();
		params.put("code", smsCode);
		params.put("product", "任性猫");
		ApiResult result = SMSUtils.send(username, prop.get("sms.verifyCodeTemplate"), params);
		if(result.getCode() == Code.SUCCESS){
			sm.setex("PONHE:"+username,120,"1");
			Sms sms = new Sms();
			sms.setMobile(username);
			sms.setSmsCode(smsCode);
			sms.setSmsType(Setting.SmsType.findPassword.ordinal());
			smsService.saveOrUpdate(sms);
			renderJson(ApiResult.success("短信发送成功！【"+smsCode+"】"));

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

		if (!smsService.smsExists(phone, smsCode, Setting.SmsType.findPassword)) {
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
