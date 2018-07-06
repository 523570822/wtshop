package com.wtshop.api.controller.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.util.ApiResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Share;
import com.wtshop.service.ShareService;

/**
 * Created by sq on 2017/7/27.
 */

@ControllerBind(controllerKey = "/api/member/share")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class ShareAPIController extends BaseAPIController {


    private ShareService shareService = enhance(ShareService.class);

    /**
     * 保存分享的信息
     */
    public void add(){

        String title = getPara("title");
        String type = getPara("type");
        Long member_id = getParaToLong("member_id");
        String content = getPara("content");
        String link = getPara("link");
        Share share = new Share();
        share.setContent(content);
        share.setLink(link);
        share.setType(type);
        share.setTitle(title);
        share.setMemberId(member_id);
        shareService.save(share);
        renderJson(ApiResult.success());

    }




}
