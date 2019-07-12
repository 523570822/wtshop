package com.wtshop.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.kit.ServletKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.Principal;
import com.wtshop.Setting;
import com.wtshop.api.common.result.CodeResult;
import com.wtshop.api.common.token.TokenManager;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Account;
import com.wtshop.model.Member;
import com.wtshop.model.MiaobiLog;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.MyRequest;
import com.wtshop.util.RedisUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@ControllerBind(controllerKey = "/api/login")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class LoginAPIController extends BaseAPIController {

    private MemberService memberService = enhance(MemberService.class);
    private AppManageService appManageService = enhance(AppManageService.class);
    private AccountService accountService = enhance(AccountService.class);
    private SmsService smsService = enhance(SmsService.class);
    private MiaobiLogService miaobiLogService = enhance(MiaobiLogService.class);
    public void submit() {
        String username = getPara("username1");
        String password = getPara("password1");
        HttpServletRequest request = getRequest();
        Res resZh = I18n.use();
        if (StringUtils.isEmpty(username)) {
            renderJson(ApiResult.fail("登录名不能为空!"));
            return;
        }
        if (StringUtils.isEmpty(password)) {
            renderJson(ApiResult.fail("密码不能为空!"));
            return;
        }

        //查询mongo数据库
   /*     BasicDBObject basicDBObject = new BasicDBObject();
        Boolean isVip = false;
        basicDBObject.put("phone",username);
        DBObject vip = MongoKit.getCollection("vipMember").findOne(basicDBObject);
        if(! ObjectUtils.isEmpty(vip)){
            isVip = true;
        }*/
        Boolean isVip = false;
        //验证 先去member表里查
        Member member = memberService.findByUsernames(username, password);

        if (member == null || member.getPassword() == null) {
            renderJson(ApiResult.fail(resZh.format("shop.login.unknownAccount")));
            return;
        }

        if (!member.getIsEnabled()) {
            renderJson(ApiResult.fail(resZh.format("shop.login.disabledAccount")));
            return;
        }
        if (DigestUtils.md5Hex(password).equals(member.getPassword())) {
            member.setIsVip(isVip);
            member.setLoginIp(request.getRemoteAddr());
            member.setLoginDate(new Date());
            member.setLoginFailureCount(0);
            memberService.update(member);
            Map map = memberService.queryUser(member);
            setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
            //WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
//            if (StringUtils.isNotEmpty(member.getNickname())) {
//                WebUtils.addCookie(request, response, Member.NICKNAME_COOKIE_NAME, member.getNickname());
//            }
            String token = TokenManager.getMe().generateToken(member);
            RedisUtil.setString(token, 60 * 60 * 24 * 7, String.valueOf(member.getId()));
            map.put("token",token);


            RedisUtil.setString("TOKEN:" + member.getId(),60 * 60 * 24 * 7, token);
            renderJson(ApiResult.success(map,"登录成功"));

            return;

        } else {
            renderJson(ApiResult.fail("您输入的用户名或密码错误,请重新输入!"));
            return;
        }

    }
    public void codeXCXSubmit() {
        String code = getPara("code");
        String img = getPara("img","");
        String nickname = getPara("name","");
        HttpServletRequest request = getRequest();
        Map<String, Object> access_token = accountService.getXCXAccess_token(code);
     // accountService.getXCXAccess_token(code);
        Set<String> key = access_token.keySet();
        System.out.println("code：====="+code);

        System.out.println("token 打印");
        for (Iterator<String> it = key.iterator(); it.hasNext();) {
            String s = it.next();
            System.out.println(s+":"+access_token.get(s));//这里的s就是map中的key，map.get(s)就是key对应的value。
        }
    //  Map<String, Object> user = xcxAccountService.getUserInfo(access_token);
        Map<String, Object> user = accountService.getUserInfoXCX(access_token);
        System.out.println("user 打印===============");
        Set<String> key1 = user.keySet();
        for (Iterator<String> it = key1.iterator(); it.hasNext();) {
            String s = it.next();
            System.out.println(s+":"+user.get(s));//这里的s就是map中的key，map.get(s)就是key对应的value。
        }
        //String nickname = com.wtshop.util.StringUtils.filterEmoji(user.get("nickname").toString()) ;
        /*   String openid = user.get("unionid").toString();*/
        String openid = access_token.get("openid").toString();
        String unionid = "";
        if(access_token.get("unionid")!=null){
           unionid = access_token.get("unionid").toString();
        }
       // String unionid = access_token.get("unionid").toString();
        int codes = 9000; //不需要绑定手机号
        Member member = null;
        //获取微信社交绑定的openId
        Long accountId = 0L;
   //   Account  account=accountService.findByUnionid(unionid,0);
        Account account = accountService.findByAccount(openid,unionid, 4);
        if(account != null){
            accountId = account.getId();
            member = memberService.find(account.getMemberId());
        }

        if (member == null) {

            codes = 9001;
            member = new Member();
            member.setIsDelete(false);
            member.setOpenId(openid);

            member.setNickname(nickname);
            member.setAmount(BigDecimal.ZERO);
            member.setBalance(BigDecimal.ZERO);
            member.setPrestore(BigDecimal.ZERO);
            member.setCommission(BigDecimal.ZERO);
            member.setRecharge(BigDecimal.ZERO);
            member.setLoginDate(new Date());
            member.setLoginIp(request.getRemoteAddr());
            member.setMemberRankId(1L);
            member.setIsEnabled(true);
            memberService.save(member);
            Account account1 = new Account();
            account1.setAccount(openid);
            account1.setUnionid(unionid);
            account1.setType(0);
            account1.setNickname(nickname);
            account1.setMemberId(member.getId());
            Account dd = accountService.save(account1);
            CodeResult codeResult = new CodeResult(codes,"", dd.getId(),"",openid,unionid);
            renderJson(ApiResult.success(codeResult, "登录成功"));
            return;

        }else {
            //本身没有绑定手机号
            if(member.getPhone() == null){
                codes = 9001;
            }
            member.setLoginIp(request.getRemoteAddr());
            member.setLoginDate(new Date());
            memberService.update(member);
        }

        setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
        String token = TokenManager.getMe().generateToken(member);
        RedisUtil.setString(token, 60 * 60 * 24 * 7, String.valueOf(member.getId()));
        RedisUtil.setString("TOKEN:" + member.getId(),60 * 60 * 24 * 7, token);
        Cache actCache = Redis.use();
        actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("SOUND:" + member.getId(),"default");
        CodeResult codeResult = new CodeResult(codes,token, accountId,member.getShareCode(),openid,unionid);

        renderJson(ApiResult.success(codeResult, "登录成功"));
    }

    public void codeSubmit() {
        String code = getPara("code");
        HttpServletRequest request = getRequest();
        Map<String, Object> access_token = accountService.getAccess_token(code);




        Map<String, Object> user = accountService.getUserInfo(access_token);
        System.out.println("user 打印===============");
        Set<String> key1 = user.keySet();
        for (Iterator<String> it = key1.iterator(); it.hasNext();) {
            String s = it.next();
            System.out.println(s+":"+user.get(s));//这里的s就是map中的key，map.get(s)就是key对应的value。
        }
        String nickname = com.wtshop.util.StringUtils.filterEmoji(user.get("nickname").toString()) ;
        String openid = user.get("openid").toString();
        String unionid = user.get("unionid").toString();
        int codes = 9000; //不需要绑定手机号
        Member member = null;
        //获取微信社交绑定的openId
        Long accountId = 0L;
        Account account = accountService.findByAccount(openid,unionid, 0);
        if(account != null){
            member = memberService.find(account.getMemberId());
        }

        if (member == null) {
            member = new Member();
            member.setIsDelete(false);
            member.setOpenId(openid);

            member.setNickname(nickname);
            member.setAmount(BigDecimal.ZERO);
            member.setBalance(BigDecimal.ZERO);
            member.setPrestore(BigDecimal.ZERO);
            member.setCommission(BigDecimal.ZERO);
            member.setRecharge(BigDecimal.ZERO);
            member.setLoginDate(new Date());
            member.setLoginIp(request.getRemoteAddr());
            member.setMemberRankId(1L);
            member.setIsEnabled(true);
            memberService.save(member);

            Account account1 = new Account();
            account1.setAccount(openid);
            account1.setUnionid(unionid);
            account1.setType(0);
            account1.setNickname(nickname);
            account1.setMemberId(member.getId());
            accountService.save(account1);

            codes = 9001;
            accountId = account1.getId();
        }else {
            //本身没有绑定手机号
            if(member.getPhone() == null){
                codes = 9001;
                accountId = account.getId();
            }
            member.setLoginIp(request.getRemoteAddr());
            member.setLoginDate(new Date());
            memberService.update(member);
        }

        setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
        String token = TokenManager.getMe().generateToken(member);
        RedisUtil.setString(token, 60 * 60 * 24 * 7, String.valueOf(member.getId()));
        RedisUtil.setString("TOKEN:" + member.getId(),60 * 60 * 24 * 7, token);
        Cache actCache = Redis.use();
        actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("SOUND:" + member.getId(),"default");
        CodeResult codeResult = new CodeResult(codes,token, accountId,member.getShareCode(),openid,unionid);
        renderJson(ApiResult.success(codeResult, "登录成功"));
    }



    public void qqcodeSubmit() {
        String openId = getPara("openId");
        String unionid = getPara("unionid");
        //验证 先去member表里查
        String appId = getPara("appId");
        String access_token = getPara("access_token");
        Map<String, Object> user = accountService.getqq_UserInfo(access_token, openId, appId);
        String nickname = com.wtshop.util.StringUtils.filterEmoji(user.get("nickname").toString()) ;
        Member member = null;
        //获取社交绑定的openId
        Account account = accountService.findByAccount(openId, unionid,1);
        Long accountId = 0L;
        if(account != null) {
            member = memberService.find(account.getMemberId());
        }
        int codes = 9000; //不需要绑定手机号
        if (member == null) {
            member = new Member();
            member.setIsDelete(false);
            member.setOpenId(openId);
            member.setNickname(nickname);
            member.setAmount(BigDecimal.ZERO);
            member.setBalance(BigDecimal.ZERO);
            member.setPrestore(BigDecimal.ZERO);
            member.setCommission(BigDecimal.ZERO);
            member.setRecharge(BigDecimal.ZERO);
            member.setIsEnabled(true);
            member.setMemberRankId(1L);
            memberService.save(member);

            Account account1 = new Account();
            account1.setAccount(openId);
            account1.setType(1);
            account1.setNickname(nickname);
            account1.setMemberId(member.getId());
            accountService.save(account1);

            codes = 9001;
            accountId = account1.getId();
        } else {

            if(member.getPhone() == null){
                codes = 9001;
                accountId = account.getId();
            }

            if ("qzuser".equalsIgnoreCase(member.getNickname())) {
                user = accountService.getqq_UserInfo(access_token, openId, appId);
                nickname = com.wtshop.util.StringUtils.filterEmoji(user.get("nickname").toString());
                member.setNickname(nickname);
                memberService.update(member);
            }
        }

        setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
        String token = TokenManager.getMe().generateToken(member);
        RedisUtil.setString(token, 60 * 60 * 24 * 7, String.valueOf(member.getId()));
        RedisUtil.setString("TOKEN:" + member.getId(),60 * 60 * 24 * 7, token);
        Cache actCache = Redis.use();
        actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("SOUND:" + member.getId(),"default");
        CodeResult codeResult = new CodeResult(codes,token, accountId,member.getShareCode(),openId,"");
        renderJson(ApiResult.success(codeResult, "登录成功"));
    }


    public void weiboSubmit() {
        String openId = getPara("openId");
        String unionid = getPara("unionid");
        String access_token = getPara("access_token");
        Map<String, Object> user = accountService.getSina_UserInfo(access_token, openId);
        String nickname = com.wtshop.util.StringUtils.filterEmoji(user.get("name").toString()) ;
        HttpServletRequest request = getRequest();
        //验证 先去member表里查
        Member member = null;
        //获取社交绑定的openId
        Account account = accountService.findByAccount(openId, unionid,2);
        Long accountId = 0L;
        if(account != null) {
            member = memberService.find(account.getMemberId());
        }
        int codes = 9000; //不需要绑定手机号
        if (member == null) {
            member = new Member();
            member.setIsDelete(false);
            member.setOpenId(openId);
            member.setNickname(nickname);
            member.setAmount(BigDecimal.ZERO);
            member.setBalance(BigDecimal.ZERO);
            member.setPrestore(BigDecimal.ZERO);
            member.setCommission(BigDecimal.ZERO);
            member.setRecharge(BigDecimal.ZERO);
            member.setLoginDate(new Date());
            member.setMemberRankId(1L);
            member.setLoginIp(request.getRemoteAddr());
            member.setIsEnabled(true);
            memberService.save(member);


            Account account1 = new Account();
            account1.setAccount(openId);
            account1.setType(2);
            account1.setNickname(nickname);
            account1.setMemberId(member.getId());
            accountService.save(account1);
            codes = 9001;

            accountId = account1.getId();


        } else {

            if(member.getPhone() == null){
                codes = 9001;
                accountId = account.getId();
            }


            member.setLoginIp(request.getRemoteAddr());
            member.setLoginDate(new Date());
            memberService.update(member);
        }

        setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
        String token = TokenManager.getMe().generateToken(member);
        RedisUtil.setString(token, 60 * 60 * 24 * 7, String.valueOf(member.getId()));
        RedisUtil.setString("TOKEN:" + member.getId(),60 * 60 * 24 * 7, token);
        Cache actCache = Redis.use();
        actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),true);
        actCache.set("SOUND:" + member.getId(),"default");
        CodeResult codeResult = new CodeResult(codes,token, accountId,member.getShareCode(),openId,"");
        renderJson(ApiResult.success(codeResult , "登录成功"));
    }


    //  第三方注册接口
    public void registe3rd() {
        String appId = getApiParaAndCheck("appId", "");
        String appKey = getApiParaAndCheck("appKey", "");
        String phone = getApiParaAndCheck("phone", "");
        String nickname = getPara("nickname");
        ApiResult result = appManageService.register(phone, nickname, appId, appKey, ServletKit.getIp(getRequest()));
        renderJson(result);
    }

    /**
     * 手机号绑定
     */
    public void phoneBang(){
        Integer type =getParaToInt("type");
        Long accountId = getParaToLong("accountId");
        String phone = getPara("phone");
        String passWord = getPara("passWord");
        String code = getPara("code");
        String passWordMD = DigestUtils.md5Hex(passWord);
        if (!smsService.smsExists(phone, code, Setting.SmsType.memberRegister)) {
            renderJson(ApiResult.fail("验证码输入错误!"));
            return;
        }
        Account account = accountService.find(accountId);
        if(account != null){
            Member member = memberService.find(account.getMemberId());
            if(member != null){
                member.setPhone(phone);
                member.setPassword(passWordMD);
                memberService.update(member);

                setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
                String token = TokenManager.getMe().generateToken(member);
                RedisUtil.setString(token, 60 * 60 * 24 * 7, String.valueOf(member.getId()));
                RedisUtil.setString("TOKEN:" + member.getId(),60 * 60 * 24 * 7, token);
                Cache actCache = Redis.use();
                actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),true);
                actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),true);
                actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),true);
                actCache.set("SOUND:" + member.getId(),"default");



                JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
                Double sendMiaoBi = redisSetting.getDouble("registerSending");;
                MiaobiLog miaobiLog = new MiaobiLog();
                miaobiLog.setMemberId(member.getId());
                miaobiLog.setCredit(BigDecimal.valueOf(sendMiaoBi));
                miaobiLog.setDebit(BigDecimal.ZERO);
                miaobiLog.setType(0);
                miaobiLog.setMemo("注册成功赠送");
                miaobiLog.setBalance(member.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
                //更新用户喵币
                member.setPoint(member.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
                Map<String,Object>  map=  new HashMap<>();
                miaobiLogService.save(miaobiLog);
                memberService.update(member);
                map.put("sendMiaobi",sendMiaoBi);
                map.put("title","注册成功");
                map.put("type",1);

                map.put("miaobilId",0);
                map.put("token",token);


                renderJson(ApiResult.success(map));


                return;
            }
        }
        renderJson(ApiResult.fail("系统错误,请稍后尝试"));
    }
    /**
     * 手机号绑定
     */
    public void phoneBangXCX(){

        String openidXCX = getPara("openid");
        String unionid = getPara("unionid");
        String phone = getPara("phone");
        String passWord = getPara("passWord");
        String nickname = getPara("nickname");

        String code = getPara("code");
        String passWordMD = DigestUtils.md5Hex(passWord);
        if (!smsService.smsExists(phone, code, Setting.SmsType.memberRegister)) {
            renderJson(ApiResult.fail("验证码输入错误!"));
            return;
        }
        Member member = memberService.findByPhone(phone);

        Account account = accountService.findByAccount(openidXCX,unionid,null);
        if(account != null){

            account.setAccount(openidXCX);
            account.setUnionid(unionid);
            account.setType(4);
            //account1.setNickname(nickname);
            account.setMemberId(member.getId());
            accountService.update(account);
            if(member != null){
                member.setPhone(phone);
                member.setPassword(passWordMD);
                memberService.update(member);

                setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
                String token = TokenManager.getMe().generateToken(member);
                RedisUtil.setString(token, 60 * 60 * 24 * 7, String.valueOf(member.getId()));
                RedisUtil.setString("TOKEN:" + member.getId(),60 * 60 * 24 * 7, token);
                Cache actCache = Redis.use();
                actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),true);
                actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),true);
                actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),true);
                actCache.set("SOUND:" + member.getId(),"default");



                JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
                Double sendMiaoBi = redisSetting.getDouble("registerSending");;

                //更新用户喵币

                Map<String,Object>  map=  new HashMap<>();

                map.put("sendMiaobi",sendMiaoBi);
                map.put("title","合并成功");
                map.put("type",1);

                map.put("miaobilId",0);
                map.put("token",token);


                renderJson(ApiResult.success(map));


                return;
            }
        }else{
            Account account1 = new Account();
            account1.setAccount(openidXCX);
            account1.setUnionid(unionid);
            account1.setType(4);
            account1.setNickname(nickname);
            account1.setMemberId(member.getId());
            accountService.save(account1);
            if(member != null){
                member.setPhone(phone);
                member.setPassword(passWordMD);
                memberService.update(member);

                setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
                String token = TokenManager.getMe().generateToken(member);
                RedisUtil.setString(token, 60 * 60 * 24 * 7, String.valueOf(member.getId()));
                RedisUtil.setString("TOKEN:" + member.getId(),60 * 60 * 24 * 7, token);
                Cache actCache = Redis.use();
                actCache.set("SYSTEMMESSAGR_SWITCH:" + member.getId(),true);
                actCache.set("ORDERMMESSAGR_SWITCH:" + member.getId(),true);
                actCache.set("STAFFMESSAGR_SWITCH:" + member.getId(),true);
                actCache.set("SOUND:" + member.getId(),"default");



                JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
                Double sendMiaoBi = redisSetting.getDouble("registerSending");;

                //更新用户喵币

                Map<String,Object>  map=  new HashMap<>();

                map.put("sendMiaobi",sendMiaoBi);
                map.put("title","合并成功");
                map.put("type",1);

                map.put("miaobilId",0);
                map.put("token",token);


                renderJson(ApiResult.success(map));


                return;
            }
        }
        renderJson(ApiResult.fail("系统错误,请稍后尝试"));
    }
    public  void getAccessXCX_token(){

        Map<String,Object> ddd=accountService.getall(accountService.getAccessXCX_token());
        renderJson(ApiResult.success(ddd));
    }

    public  void alicloudapi()throws  Exception{

        String host = "https://wuliu.market.alicloudapi.com";       //【1】请求地址  支持http 和 https 及 WEBSOCKET
        String path = "/kdi";                                     //【2】后缀
        String appcode = "e885d89a08b04f9cb0b9e9be7c0bba73";                             //【3】AppCode  你自己的AppCode 在买家中心查看
        String sn = getPara("sn");                   //【4】参数，具体参照api接口参数
       // String sn ="4600617028567"   ;       //【4】参数，具体参照api接口参数
        // String type = "YD";                                            //【5】参数，具体参照api接口参数
        // String urlSend = host + path + "?no=" + sn ;   //【6】拼接请求链接
        String urlSend = host + path + "?no=" + sn ;   //【6】拼接请求链接
        /*【1】 ~ 【6】 需要修改为对应的 可以参考产品详情 */
        Object ddd = MyRequest.sendGet(host + path, "no=" + sn, "APPCODE " + appcode);
        System.out.print(ddd);
 //       URL url = new URL(urlSend);
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);//格式Authorization:APPCODE (中间是英文空格)
//        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
//        httpURLConnection.setRequestProperty("contentType", "utf-8");
//        int httpCode = httpURLConnection.getResponseCode();
//        String json = read(httpURLConnection.getInputStream());
//        System.out.println("/* 获取服务器响应状态码 200 正常；400 权限错误 ； 403 次数用完； */ ");
//        System.out.println(httpCode);
//        System.out.println("/* 获取返回的json   */ ");
//        System.out.print(json);

        renderJson(JSONObject.parse(ddd.toString()));
    }

    /*
       读取返回结果
    */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

/*    public  void kdquerytools(){
        String text = getPara("text");
        String method = getPara("method");

        StringBuilder requestUrl = new StringBuilder("https://m.kuaidi100.com/apicenter/kdquerytools.do?text=")
                .append(text).append("&method=").append(method);
        String res = HttpUtils.get(requestUrl.toString());
        renderJson(res);
    }
    public  void ext(){
        String platform = getPara("platform");
        String pos = getPara("pos");
        String method = getPara("method");

        StringBuilder requestUrl = new StringBuilder("https://m.kuaidi100.com/assets/ext?platform=")
                .append(platform).append("&method=").append(method).append("&pos=").append(pos);
        String res = HttpUtils.get(requestUrl.toString());
        renderJson(res);
    }

    public  void company(){

        String number = getPara("number");
        String method = getPara("method");

        StringBuilder requestUrl = new StringBuilder("https://m.kuaidi100.com/assets/ext?number=").append(number).append("&method=").append(method);
        String res = HttpUtils.get(requestUrl.toString());
        renderJson(res);
    }
    public  void query(){
        String postid = getPara("postid");
        String id = getPara("id");
        String valicode = getPara("valicode");
        String temp = getPara("temp");
        String type = getPara("type");
        String phone = getPara("platform");
        Map<String,String> map=new HashMap<>();
        map.put("postid",postid);
        map.put("id",id);
        map.put("valicode",valicode);
        map.put("temp",temp);
        map.put("type",type);
        map.put("phone",phone);

       StringBuilder requestUrl1 = new StringBuilder("https://m.kuaidi100.com/query?postid=").append(postid).append("&id=").append(id).append("&valicode=").append(valicode).append("&temp=").append(temp).append("&type=").append(type).append("&phone=").append(phone);
     //  StringBuilder requestUrl = new StringBuilder("https://m.kuaidi100.com/query");
      //  Object dd = JSONObject.toJSON(map);

     //   String res =HttpUtils.post(requestUrl.toString(),dd.toString());
      String res = HttpUtils.get(requestUrl1.toString());
System.out.println(res);
        renderJson(res);
    }*/
public static void main(String[] args) throws  Exception{
    String host = "https://wuliu.market.alicloudapi.com";       //【1】请求地址  支持http 和 https 及 WEBSOCKET
    String path = "/kdi";                                     //【2】后缀
    String appcode = "e885d89a08b04f9cb0b9e9be7c0bba73";                             //【3】AppCode  你自己的AppCode 在买家中心查看
    //  String sn = getPara("sn");                   //【4】参数，具体参照api接口参数
    String sn ="4600617028567"   ;       //【4】参数，具体参照api接口参数
   // String type = "YD";                                            //【5】参数，具体参照api接口参数
   // String urlSend = host + path + "?no=" + sn ;   //【6】拼接请求链接
     String urlSend = host + path + "?no=" + sn ;   //【6】拼接请求链接
    /*【1】 ~ 【6】 需要修改为对应的 可以参考产品详情 */
    Object ddd = MyRequest.sendGet(host + path, "no=" + sn, "APPCODE " + appcode);
//    MyRequest.sendGet( host + path,sn,"APPCODE " + appcode);
//    URL url = new URL(urlSend);
//    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//    httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);//格式Authorization:APPCODE (中间是英文空格)
//    int httpCode = httpURLConnection.getResponseCode();
//    String json = read(httpURLConnection.getInputStream());
//    System.out.println("/* 获取服务器响应状态码 200 正常；400 权限错误 ； 403 次数用完； */ ");
//    System.out.println(httpCode);
//    System.out.println("/* 获取返回的json   */ ");
    System.out.print(ddd);
}
}
