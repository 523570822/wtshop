package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.ActivityStore;
import com.wtshop.service.ActivityStoreService;
import com.wtshop.service.MemberService;

import java.util.Map;

/**
 * Created by 蔺哲 on 2017/5/24.
 */
@ControllerBind(controllerKey = "admin/activityStore")
public class ActivityStoreController extends BaseController{

    private MemberService memberService = enhance(MemberService.class);
    private ActivityStoreService certificatesService = enhance(ActivityStoreService.class);


    public void list(){
        Integer type = getParaToInt("type");
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page", certificatesService.findShenHePage( pageable, type));
        render("/admin/activity_store/list.ftl");
    }


    public void toShenHe(){
        Long certificatesId = getParaToLong("certificatesId");
        setAttr("certificates",certificatesService.find(certificatesId));
        render("/admin/certificates_shenhe/shenhe.ftl");
    }


    public void shenhe(){
        Long certificatesId = getParaToLong("certificatesId");
        Integer type = getParaToInt("type");
        ActivityStore certificates = certificatesService.find(certificatesId);
        Map result = certificatesService.updateCertificates(certificates,type);
        renderJson(result);
    }

    /**
     * 删除
     */
    public void delete() {
        Long[] ids = getParaValuesToLong("ids");
        for(Long id : ids){
            certificatesService.delete(id);
        }
        renderJson(SUCCESS_MESSAGE);
    }

}
