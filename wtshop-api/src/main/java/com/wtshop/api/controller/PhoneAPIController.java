package com.wtshop.api.controller;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.AppVersion;
import com.wtshop.model.Member;
import com.wtshop.model.Version;
import com.wtshop.service.AppVersionService;
import com.wtshop.service.MemberService;
import com.wtshop.service.VersionService;
import com.wtshop.util.ApiResult;

/**
 * Created by sq on 2017/9/8.
 */
@ControllerBind(controllerKey = "/api/phone")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class PhoneAPIController extends BaseAPIController {

    private VersionService versionService = enhance(VersionService.class);
    private MemberService memberService = enhance(MemberService.class);
    private AppVersionService appVersionService = enhance(AppVersionService.class);


    /**
     * 消息推送声音接口
     */
    public void sound(){
        Member current = memberService.getCurrent();
        Cache redis = Redis.use();
        Boolean sound = getParaToBoolean("sound",true);
        if(sound){
            redis.set("SOUND:" + current.getId(),"default");
        }else {
            redis.set("SOUND:" + current.getId(),"sound.wav");
        }
        renderJson(ApiResult.success());
    }
    /**
     * 消息推送声音接口
     */
    public void phone(){
        Member current = memberService.getCurrent();

        Boolean sound = getParaToBoolean("status",true);
        current.setPhoneStatus(sound);
        memberService.update(current);

        renderJson(ApiResult.success());
    }
    /**
     * 版本
     */
    public void version(){

        Version newVersion = versionService.findNewVersion();
        //获取当前的版本号
        renderJson(ApiResult.success(newVersion));
    }

    /**
     * 渠道下载
     */
    public void checkVersion(){
        String type = getPara("type");
        AppVersion newVersion = appVersionService.checkVersion(type);
        //获取当前的版本号
        renderJson(ApiResult.success(newVersion));
    }

    public void shareUrl(){
        renderJson(ApiResult.success("www.baidu.com"));
    }

    /**
     * 推送按钮的默认状态
     */
    public void soundStatus(){
        Member current = memberService.getCurrent();
        Cache redis = Redis.use();
        String sound = redis.get("SOUND:" + current.getId());
        Boolean status = true;
        if("sound.wav".equals(sound)){
            status = false;
        }
        renderJson(ApiResult.success(status));
    }
    /**
     * 推送按钮的默认状态
     */
    public void phoneStatus(){
        Member current = memberService.getCurrent();
        Boolean sound = current.getPhoneStatus();
        if(sound==null){
            sound=true;
        }
        renderJson(ApiResult.success(sound));
    }

}
