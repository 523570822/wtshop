package com.wtshop.service;

import com.jfinal.aop.Enhancer;
import com.wtshop.Message;
import com.wtshop.dao.OrganDao;
import com.wtshop.model.Organ;
import com.wtshop.shiro.core.SubjectKit;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by sq on 2017/7/21.
 */
public class OrganService extends BaseService<Organ>{

    /**
     * 构造方法
     */
    public OrganService() {
        super(Organ.class);
    }
    private OrganDao organDao = Enhancer.enhance(OrganDao.class);

    /**
     * 登陆验证
     */
    public Message login(String username, String password, boolean isRememberUsername, String captcha, HttpServletRequest request) {
        Message failureMessage = null;
        if (! SubjectKit.doCaptcha("captcha", captcha)) {
            return failureMessage = Message.error("admin.captcha.invalid");
        }
        Organ organ = organDao.findByUsername(username);
        if (organ == null) {
            return failureMessage = Message.error("admin.login.unknownAccount");
        }else {
            if(!organ.getAdminPassword().equals(DigestUtils.md5Hex(password))){
                return failureMessage = Message.error("admin.login.incorrectCredentials");
            }
        }

        return failureMessage;
    }

    public Organ findByUsername(String username){
        return organDao.findByUsername(username);
    }

}
