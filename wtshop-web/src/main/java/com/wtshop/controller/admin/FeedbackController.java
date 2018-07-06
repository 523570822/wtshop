package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.service.FeedbackService;

/**
 * Created by sq on 2017/6/22.
 */

@ControllerBind(controllerKey = "/admin/feedback")
public class FeedbackController extends BaseController{

    private FeedbackService feedbackService = enhance(FeedbackService.class);

    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        String memberName = getPara("memberName");
        setAttr("pageable", pageable);
        setAttr("page", feedbackService.feedbackPage(memberName,pageable));
        render("/admin/feedback/list.ftl");
    }

    /**
     * 删除
     */
    public void delete(){
        Long[] ids = getParaValuesToLong("ids");
        feedbackService.delete(ids);
        renderJson(SUCCESS_MESSAGE);
    }


}
