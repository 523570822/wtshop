package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;

/**
 * Created by sq on 2017/8/21.
 */

@ControllerBind(controllerKey = "/admin/messageAll")
public class MessageAllController extends BaseController {

    public void tomain(){

        render("/admin/message_all/main.ftl");
    }


}
