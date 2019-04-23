package com.wtshop.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.kit.AliPayApi;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.CommonAttributes;
import com.wtshop.Setting;
import com.wtshop.api.common.bean.LoginResponse;
import com.wtshop.api.common.token.TokenManager;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.constants.Code;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Account;
import com.wtshop.model.Member;
import com.wtshop.model.MiaobiLog;
import com.wtshop.model.Sms;
import com.wtshop.service.AccountService;
import com.wtshop.service.MemberService;
import com.wtshop.service.MiaobiLogService;
import com.wtshop.service.SmsService;
import com.wtshop.util.ApiResult;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.SMSUtils;
import com.wtshop.util.SystemUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerBind(controllerKey = "/api/account")
@Before({WapInterceptor.class, ErrorInterceptor.class} )
public class AccountAPIController extends BaseAPIController {
	
	private MemberService memberService = enhance(MemberService.class);
	private SmsService smsService = enhance(SmsService.class);
	private AccountService accountService = enhance(AccountService.class);
	private MiaobiLogService miaobiLogService = enhance(MiaobiLogService.class);
	
	
	/**
	 * 检查用户名是否被禁用或已存在
	 * {"msg":"","code":1,"data":false}
	 */

	public void checkUsername() {
		String phone = getPara("phone");
		if (StringUtils.isEmpty(phone)) {
			renderJson(ApiResult.fail());
			return;
		}
		//查询现在数据库用户名
		boolean member = memberService.usernameExists(phone);
		//只要mysql 中不存在数据 都要去注册 若mogo中存在 密码同步 若mogo中不存在 插入数据
		if( !member ){
			renderJson(ApiResult.success("该账号可以注册!"));
		}else{
			renderJson(ApiResult.fail("该账号已存在,请直接登录!"));
		}

	}
	
	/**
	 * 发送会员注册短信
	 * {"msg":"短信发送成功！【1653】","code":0,"data":null} memberRegister
	 */
    public void sendRegisterSms() {
		Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
		String username = getPara("phone");
		Cache sm = Redis.use();

		if (StringUtils.isEmpty(username)) {
			renderJson(ApiResult.fail("登录名不能为空!"));
            return;
        }
		boolean ismember = memberService.usernameExists(username);
		if( ismember ){
			renderJson(ApiResult.fail("手机号已被注册或已经被绑定,请直接登录!"));
			return;
		}

        //检查手机号码有效性
        if (!SMSUtils.isMobileNo(username)) {
			renderJson(ApiResult.fail("请检查手机号是否正确!"));
            return;
        }

        if(sm.get("PONHE:"+username) != null){
			renderJson(ApiResult.fail("您的操作过于频繁,请稍后再试!"));
			return;
		}
        String smsCode = SMSUtils.randomSMSCode(4);
		Map<String, String> params = new HashMap<>();
		params.put("code", smsCode);
		params.put("product", "任性猫");
		ApiResult result = SMSUtils.send(username, prop.get("sms.verifyCodeTemplate"), params);
		if(result.resultSuccess()) {
			sm.setex("PONHE:"+username,120,"1");
			Sms sms = new Sms();
			sms.setMobile(username);
			sms.setSmsCode(smsCode);
			sms.setSmsType(Setting.SmsType.memberRegister.ordinal());
			smsService.saveOrUpdate(sms);
			renderJson(ApiResult.success("短信发送成功！【"+smsCode+"】"));
		}else {
			renderJson(ApiResult.fail("您发送的过于频繁,请稍后再试!"));
		}

		
    }
    
    /**
	 * 注册提交
	 * {"msg":"请求成功","code":1,"data":{}}
	 */
	public void register(){
		String username = getPara("phone");
        String smsCode = getPara("smsCode");
        String password = getPara("password");
		String pwdconfirm = getPara("pwdconfirm");
		String onShareCode = getPara("onShareCode");
		if(StringUtils.isNotEmpty(onShareCode)){
			onShareCode=onShareCode.toUpperCase();
		}

		List<Member> me = memberService.findByShareCode(onShareCode);


		if((StringUtils.isNotEmpty(onShareCode)&&(me==null||me.size()==0))&&(!"VA3TYG".equals(onShareCode))){

			renderJson(ApiResult.fail("邀请码不存在!"));
			return;
		}
		if(StringUtils.isEmpty(username)){
			renderJson(ApiResult.fail("登录名不能为空!"));
			return;
		}

		if(StringUtils.isEmpty(smsCode)){
			renderJson(ApiResult.fail("验证码不能为空!"));
			return;
		}
		
		//检查手机号码有效性
        if (!SMSUtils.isMobileNo(username)) {
			renderJson(ApiResult.fail("请检查手机号是否正确!"));
            return;
        }
		if (!Code.isDevMode && !smsService.smsExists(username, smsCode, Setting.SmsType.memberRegister)) {
			renderJson(ApiResult.fail("验证码输入错误!"));
			return;
		}

//		if(!"1234".equals(smsCode)){
//			renderJson(ApiResult.fail("验证码输入错误!"));
//			return;
//		}

		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsRegisterEnabled()) {
			renderJson("会员注册功能已关闭");
			return;
		}

		if (password == null || pwdconfirm == null || (! password.equals(pwdconfirm))) {
			renderJson(ApiResult.fail("两次密码不一致!"));
			return;
		}

		
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			renderJson(ApiResult.fail("密码长度在" + setting.getPasswordMinLength() + "-" + setting.getPasswordMaxLength()));
			return;
		}
		boolean ismember = memberService.usernameExists(username);
		if( ismember ){
			renderJson(ApiResult.fail("手机号已被注册!"));
			return;
		}
		String remoteAddr = getRequest().getRemoteAddr();

		Member member = memberService.register(username, password, remoteAddr,onShareCode);



		//消息推送开关
		Cache actCache = Redis.use();
		actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),true);
		actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),true);
		actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),true);
		actCache.set("SOUND:" + member.getId(),"default");
		smsService.delete(username, smsCode);
		LoginResponse response = new LoginResponse();
		response.setToken(TokenManager.getMe().generateToken(member));
		setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, TokenManager.getMe().validate(response.getToken()));

		JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
		Map<String,Object>  map=  new HashMap<>();
		double sendMiaoBi=0;
		if(StringUtils.isNotEmpty(onShareCode)||(me==null||me.size()==0)){
			sendMiaoBi = redisSetting.getDouble("registerSending") ;//邀请码赠送喵币
		}else {
			sendMiaoBi = redisSetting.getDouble("registerSending") + redisSetting.getDouble("vipSending");//邀请码赠送喵币
		}


		MiaobiLog miaobiLog = new MiaobiLog();
		miaobiLog.setMemberId(member.getId());
		miaobiLog.setCredit(BigDecimal.valueOf(sendMiaoBi));
		miaobiLog.setDebit(BigDecimal.ZERO);
		miaobiLog.setType(0);
		miaobiLog.setMemo("注册成功赠送");
		miaobiLog.setBalance(member.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
		//更新用户喵币
		member.setPoint(member.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
		miaobiLogService.save(miaobiLog);
		memberService.update(member);

		map.put("sendMiaobi",sendMiaoBi);
		map.put("title","注册成功");
		map.put("type",1);
		map.put("miaobilId",0);

		renderJson(ApiResult.success(map));
	}


	/**
	 * 用户绑定信息
	 */

	public void memberAccount(){
		Member member = memberService.getCurrent();
		List<Account> userInfo = accountService.getUserInfoList(member.getId());
		renderJson(ApiResult.success(userInfo));
	}



	/**
	 * 社交账号 微信绑定
	 */
	public void weiXinAccount(){
		Member member = memberService.getCurrent();
		String code = getPara("code");
		Map<String, Object> access_token = accountService.getAccess_token(code);
		Map<String, Object> user = accountService.getUserInfo(access_token);
		String nickname = com.wtshop.util.StringUtils.filterEmoji(user.get("nickname").toString()) ;
		String openid = user.get("openid").toString();
		Account accounts = accountService.findByAccount(openid, 0);
		if(accounts != null){
			accountService.delete(accounts);
		}
		Account userInfo = accountService.getUserInfo(member.getId(),0);
		if(userInfo != null ){
			userInfo.setNickname(nickname);
			userInfo.setAccount(openid);
			accountService.update(userInfo);
		}else {
			Account account = new Account();
			account.setMemberId(member.getId());
			account.setNickname(nickname);
			account.setType(0);
			account.setAccount(openid);
			accountService.save(account);
		}
		renderJson(ApiResult.success(nickname));
	}


	/**
	 * 新浪微博绑定
	 */
	public void sinaCode(){
		Member member = memberService.getCurrent();
		String uid = getPara("uid");
		String access_token = getPara("access_token");
		Map<String, Object> user = accountService.getSina_UserInfo(access_token, uid);
		String nickname = com.wtshop.util.StringUtils.filterEmoji(user.get("name").toString()) ;

		Account accounts = accountService.findByAccount(uid, 2);
		if(accounts != null){
			accountService.delete(accounts);
		}
		Account userInfo = accountService.getUserInfo(member.getId(),2);
		if(userInfo != null ){
			userInfo.setNickname(nickname);
			userInfo.setAccount(uid);
			accountService.update(userInfo);
		}else {
			Account account = new Account();
			account.setMemberId(member.getId());
			account.setNickname(nickname);
			account.setType(2);
			account.setAccount(uid);
			accountService.save(account);
		}
		renderJson(ApiResult.success(nickname));

	}


	/**
	 * qq绑定
	 */
	public void qqAccount(){
        String openId = getPara("openId");
        //验证 先去member表里查
		String appId = getPara("appId");
		Member member = memberService.getCurrent();
        String access_token = getPara("access_token");
        Map<String, Object> user = accountService.getqq_UserInfo(access_token, openId, appId);
		String nickname = com.wtshop.util.StringUtils.filterEmoji(user.get("nickname").toString()) ;

		Account accounts = accountService.findByAccount(openId, 1);
		if(accounts != null){
			accountService.delete(accounts);
		}
		Account userInfo = accountService.getUserInfo(member.getId(),1);

		if(userInfo != null){
			userInfo.setNickname(nickname);
			userInfo.setAccount(openId);
			accountService.update(userInfo);
		}else {
			Account account = new Account();
			account.setMemberId(member.getId());
			account.setNickname(nickname);
			account.setType(1);
			account.setAccount(openId);
			accountService.save(account);
		}
		renderJson(ApiResult.success(nickname));
	}

	/**
	 * 获取支付宝字符串
	 */
	public void getInfo(){
		String infoStr = accountService.getAliMessage();
		renderJson(ApiResult.success(infoStr));
	}

	/**
	 * 支付宝绑定
	 */
	public void aLiAccount(){
		Member member = memberService.getCurrent();
		String code = getPara("code");
		Map<String, String> map = accountService.getAliPay_accessToken(code);
		if(map != null){

			String payAccessToken = map.get("accessToken");
			String uid = map.get("uid");
			String username = accountService.getAliPau_userInfo(payAccessToken);
			String nickname = com.wtshop.util.StringUtils.filterEmoji(username) ;

			Account accounts = accountService.findByAccount(uid, 3);
			if(accounts != null){
				accountService.delete(accounts);
			}
			Account userInfo = accountService.getUserInfo(member.getId(),3);
			if(userInfo != null){
				userInfo.setNickname(nickname ==null ? "已绑定" : nickname);
				userInfo.setAccount(uid);
				accountService.update(userInfo);
			}else {
				Account account = new Account();
				account.setMemberId(member.getId());
				account.setNickname(nickname ==null ? "已绑定" : nickname);
				account.setType(3);
				account.setAccount(uid);
				accountService.save(account);
			}
			renderJson(ApiResult.success(nickname));
			return;
		}
		renderJson(ApiResult.fail("错误的code!"));
	}

}

