package com.wtshop.service;

import com.google.common.collect.ImmutableMap;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext2.kit.MobileValidateKit;
import com.jfinal.ext2.kit.RandomKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.constants.Code;
import com.wtshop.dao.AppManageDao;
import com.wtshop.exception.AppRuntimeException;
import com.wtshop.model.AppManage;
import com.wtshop.model.Member;
import com.wtshop.util.ApiResult;
import com.wtshop.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class AppManageService extends BaseService<AppManage> {

    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private AppManageDao appManageDao = Enhancer.enhance(AppManageDao.class);
    private static Object lock = new Object();

    public AppManageService() {
        super(AppManage.class);
    }

    public ApiResult register(String phone, String nickname, String appId, String appKey, String ip) {
//        if (!"127.0.0.1".equals(ip)){
//            throw new AppRuntimeException(Code.API_ERROR_IP, "服务IP不在白名单列表中");
//        }

        Cache redis = Redis.use();
        AppManage appManage = redis.hget("appManage", appId);
        if (appManage == null) {
            throw new AppRuntimeException(Code.API_ERROR_APPID, "错误的appId");
        }

        if (!appManage.getAppKey().equals(appKey)) {
            throw new AppRuntimeException(Code.API_ERROR_APPKEY, "错误的appKey");
        }

        if (!StringUtils.isEmpty(appManage.getIpWhiteList())) {
            boolean isWhite = false;
            String[] strarray = appManage.getIpWhiteList().split(",");
            for (String e : strarray) {
                if (e.equals(ip)) {
                    isWhite = true;
                    break;
                }
            }
            if (!isWhite) {
                throw new AppRuntimeException(Code.API_ERROR_IP, "服务IP不在白名单列表中");
            }
        }

        if (!MobileValidateKit.isValidate(phone)) {
            throw new AppRuntimeException(Code.API_ERROR_MOBILE, "错误的手机号码");
        }
        Map resultMap = new HashMap<>();
        synchronized (lock) {
            Member member = memberService.findByPhone(phone);
            if (member != null) {
                throw new AppRuntimeException(Code.API_ERROR_BUSS, "用户已经存在");
            }
            member = memberService.register(phone, RandomKit.randomStr(), nickname, ip,null,null);
            resultMap = ImmutableMap.of("id", member.getId(), "username", member.getUsername(), "phone", member.getPhone());
        }

        return ApiResult.apiSuccess(resultMap);
    }

}
