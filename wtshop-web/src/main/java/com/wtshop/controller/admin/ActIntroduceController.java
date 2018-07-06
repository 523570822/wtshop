package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.model.ActIntroduce;
import com.wtshop.service.ActIntroduceService;

/**
 * Created by Administrator on 2017/8/21.
 */

//各种活动的详情
@ControllerBind(controllerKey = "/admin/actIntroduce")
public class ActIntroduceController extends  BaseController {
    private ActIntroduceService actIntroduceService=enhance(ActIntroduceService.class);
    public  void details(){
        int type=getParaToInt("type");
        setAttr("type",type);
        setAttr("actIntroduce",actIntroduceService.getDetailsByType(type));
        render("/admin/act/introduce.ftl");
    }

    public  void save(){
        ActIntroduce actIntroduce=getModel(ActIntroduce.class);
        ActIntroduce dbobj=   actIntroduceService.getDetailsByType(actIntroduce.getType());
        if (dbobj==null){
            actIntroduceService.save(actIntroduce);
        }else {
            actIntroduceService.update(actIntroduce);
        }



        redirect("/admin/fuDai/list.jhtml");
    }
}
