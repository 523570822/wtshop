package com.wtshop.controller.admin;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.wtshop.Message;
import com.wtshop.service.AdminService;
import com.wtshop.service.OrganService;
import com.wtshop.service.RSAService;
import com.wtshop.shiro.core.SubjectKit;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.security.interfaces.RSAPublicKey;

/**
 * Created by sq on 2017/7/21.
 */

@ControllerBind(controllerKey = "/admin/organLogin")
public class OrganLoginController extends Controller {

    private RSAService rsaService = new RSAService();
    private AdminService adminService = new AdminService();
    private OrganService organService = new OrganService();


    /**
     * 跳转登录界面
     */
    public void index(){

        RSAPublicKey publicKey = rsaService.generateKey(getRequest());
        setAttr("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
        setAttr("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));

        render("/admin/organLogin/index.ftl");
    }

    /**
     * 登录
     */
    public void organLogin() {
        String username = getPara("username", "");
        String captcha = getPara("captcha");
        boolean remember = StringUtils.equals(getPara("remember"), "on") ? true : false;
        String password = rsaService.decryptParameter("enPassword", getRequest());
        rsaService.removePrivateKey(getRequest());
        Message failureMessage = null;
        if (StrKit.notBlank(username) && StrKit.notBlank(password)) {
            failureMessage = organService.login(username, password, remember, captcha, getRequest());
        }else {
            failureMessage=Message.error("admin.login.incorrectCredentials");
        }
        RSAPublicKey publicKey = rsaService.generateKey(getRequest());
        setAttr("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
        setAttr("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
        setAttr("failureMessage", failureMessage);

        if(failureMessage == null){
            render("/admin/common/main4.ftl");
        }else{
            render("/admin/organLogin/index.ftl");
        }





    }


}

