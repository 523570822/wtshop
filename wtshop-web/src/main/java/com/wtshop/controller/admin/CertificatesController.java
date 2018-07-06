package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.model.Certificates;
import com.wtshop.service.CertificatesService;
import com.wtshop.service.MemberService;
import com.wtshop.util.ObjectUtils;

import java.util.Map;

/**
 * Created by 蔺哲 on 2017/5/24.
 */
@ControllerBind(controllerKey = "admin/certificates_shenhe")
public class CertificatesController extends BaseController{

    private MemberService memberService = enhance(MemberService.class);
    private CertificatesService certificatesService = enhance(CertificatesService.class);


    public void list(){
        Integer type = getParaToInt("type");
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        setAttr("page", certificatesService.findShenHePage( pageable, type));
        render("/admin/certificates_shenhe/list.ftl");
    }


    public void toShenHe(){
        Long certificatesId = getParaToLong("certificatesId");
        setAttr("certificates",certificatesService.find(certificatesId));
        render("/admin/certificates_shenhe/shenhe.ftl");
    }


    public void shenhe(){
        Long certificatesId = getParaToLong("certificatesId");
        Integer type = getParaToInt("type");
        Certificates certificates = certificatesService.find(certificatesId);
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
