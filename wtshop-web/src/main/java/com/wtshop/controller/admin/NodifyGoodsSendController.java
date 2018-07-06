package com.wtshop.controller.admin;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.service.NodifyGoodsSendService;

/**
 * Created by sq on 2017/10/19.
 */

@ControllerBind(controllerKey = "/admin/goodsSend")
public class NodifyGoodsSendController extends BaseController {

    private NodifyGoodsSendService nodifyGoodsSendService = Enhancer.enhance(NodifyGoodsSendService.class);


    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        Page<Record> pageList = nodifyGoodsSendService.findPageList(pageable);
        setAttr("page", pageList);
        setAttr("pageable", pageable);
        render("/admin/goods_send/list.ftl");


    }



}
