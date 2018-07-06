package com.wtshop.api.controller.act;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ServletKit;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.util.ApiResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.service.CouponShareService;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/7.
 */
@ControllerBind(controllerKey = "/act/couponShare")
@Before({WapInterceptor.class, ErrorInterceptor.class} )
public class CouponShareAppControll extends Controller {

    private CouponShareService css=enhance(CouponShareService.class);

    //获取优惠券信息
    public  void  info(){
        int cShareId=getParaToInt("cShareId"); //分享活动id
        setAttr("d", css.info(cShareId));
      render("/admin/couponShare/share.ftl");
        //renderJson(css.info(cShareId));
    }

    //领取优惠券
    public  void receive(){
        String phone=getPara("phone");
       long  cShareId=getParaToLong("cShareId"); //分享活动id
        Map map= css.receive(phone,cShareId, ServletKit.getIp(getRequest()));
        int code=(Integer) map.get("code");
        if (code>0){
            renderJson(ApiResult.fail((String) map.get("msg")));
        }else {
            renderJson(ApiResult.success());
        }
    }


}
