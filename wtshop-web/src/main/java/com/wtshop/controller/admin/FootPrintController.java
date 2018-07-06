package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.wtshop.Pageable;
import com.wtshop.service.FootPrintService;
import com.wtshop.util.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Controller - 优惠券
 */
@ControllerBind(controllerKey = "/admin/footprint")
public class FootPrintController extends BaseController {

    private FootPrintService footPrintService = enhance(FootPrintService.class);


    /**
     * 列表
     */
    public void list() {
        Pageable pageable = getBean(Pageable.class);
        setAttr("pageable", pageable);
        //用户选择日期
        Date beginDate = null;
        Date endDate = null;
        //设置日期限制
        Date stime = null;
        Date etime = null;
        Date now = new Date();
        stime = DateUtils.addMonths(now, -1);
        etime = now;
        setAttr("stime", stime);
        setAttr("etime", etime);

        String st = getPara("begindate");
        String et = getPara("enddate");
        if (!StringUtils.isEmpty(st)) {
            beginDate = com.wtshop.util.DateUtils.stringToDate(st);
            endDate = com.wtshop.util.DateUtils.stringToDate(et);
        }
        if (beginDate == null) {
            beginDate = DateUtils.addMonths(now, -1);
        }
        if (endDate == null) {
            endDate = now;
        }

        String nickname = StringUtils.endsWithIgnoreCase(pageable.getSearchProperty(), "nickname") ? pageable.getSearchValue() : null;
        String phone = StringUtils.endsWithIgnoreCase(pageable.getSearchProperty(), "phone") ? pageable.getSearchValue() : null;
        setAttr("begindate", beginDate);
        setAttr("enddate", endDate);
        setAttr("nickname", nickname);
        setAttr("phone", phone);
        setAttr("pageable", pageable);
        setAttr("page", footPrintService.getUserFootPrint(beginDate, endDate, phone, nickname, pageable));
        render("/admin/footprint/list.ftl");
    }

    //用户详情列表

    public void userdetails() {

        Pageable pageable = getBean(Pageable.class);
        Date beginDate = getParaToDate("beginDate", null);
        Date endDate = getParaToDate("endDate", null);
        if (beginDate == null) {
            beginDate = DateUtils.addMonths(new Date(), -1);
        }

        if (endDate == null) {
            endDate = new Date();
        }

        Long uid = getParaToLong("uid");
        setAttr("uid", uid);
        setAttr("beginDate", beginDate);
        setAttr("endDate", endDate);
        setAttr("pageable", pageable);
        setAttr("page", footPrintService.getUserDetailsFootPrint(beginDate, endDate, pageable, uid));
        render("/admin/footprint/user_details_list.ftl");

    }


    //商品足迹列表
    public void goodList() {

        Pageable pageable = getBean(Pageable.class);
        Date beginDate = getParaToDate("beginDate", null);
        Date endDate = getParaToDate("endDate", null);
        if (beginDate == null) {
            beginDate = DateUtils.addMonths(new Date(), -1);
        }

        if (endDate == null) {
            endDate = new Date();
        }


        setAttr("beginDate", beginDate);
        setAttr("endDate", endDate);
        setAttr("pageable", pageable);
        setAttr("page", footPrintService.getGoodsFootPrint(beginDate, endDate, pageable));
        render("/admin/footprint/goods_list.ftl");

    }


    //商品足迹列表
    public void goodDetailsList() {

        Pageable pageable = getBean(Pageable.class);
        Date beginDate = getParaToDate("beginDate", null);
        Date endDate = getParaToDate("endDate", null);
        if (beginDate == null) {
            beginDate = DateUtils.addMonths(new Date(), -1);
        }

        if (endDate == null) {
            endDate = new Date();
        }


        setAttr("beginDate", beginDate);
        setAttr("endDate", endDate);
        setAttr("pageable", pageable);
        Long goodsId = getParaToLong("goodsId");
        setAttr("goodsId", goodsId);
        setAttr("page", footPrintService.getGoodsrDetailsFootPrint(beginDate, endDate, pageable, goodsId));
        render("/admin/footprint/goods_details_list.ftl");

    }


}