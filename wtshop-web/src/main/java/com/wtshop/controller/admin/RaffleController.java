package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

import com.wtshop.Pageable;

import com.wtshop.model.*;
import com.wtshop.service.*;

import  com.wtshop.Message;

import com.wtshop.util.ReadProper;


import java.util.Date;


/**
 * Created by mr feng on 2017/7/11.
 */
@ControllerBind(controllerKey = "/admin/raffle")
public class RaffleController extends BaseController {

    private RaffleService raffleService=enhance(RaffleService.class);
    private OrderService orderService=enhance(OrderService.class);
    private MemberService memberService=enhance(MemberService.class);


    public void list() {
        Pageable pageable = getBean(Pageable.class);

        pageable.setOrderProperty("create_date");
        pageable.setOrderDirection("desc");


        Page<Raffle> raffleList = raffleService.findPage(pageable);








        setAttr("pageable", pageable);
        setAttr("page", raffleList);
        render("/admin/raffle/list.ftl");
    }

    //去添加页面
    public void add() {
        setAttr("fuDaiQuestionImage", ReadProper.getResourceValue("fuDaiDefaultImage"));
        //用户选择日期
        Date beginDate = null;
        Date endDate = null;
        Date stime = null;
        Date etime = null;
        Date now = new Date();
        if (beginDate == null) {
            beginDate =now ;

        }
        if (endDate == null) {

            endDate = org.apache.commons.lang3.time.DateUtils.addDays(now, 1);
            stime = org.apache.commons.lang3.time.DateUtils.addDays(now, 1);
        }
        //设置日期限制



        etime = now;
        setAttr("stime", stime);
        setAttr("etime", etime);
        setAttr("beginDate", beginDate);
        setAttr("endDate", endDate);
        render("/admin/activity/add.ftl");
    }

    /**
     * 发放奖品
     */
    public void publish() {
        Long id = getParaToLong("id");
         String sn=getPara("sn");


        Order order = orderService.findBySn(sn);

        Raffle raffle = raffleService.find(id);

        if (order==null){
           Message message =new Message(Message.Type.warn,"订单不存在");
            addFlashMessage(message);
        }else {
           raffle.setSn(sn);
           raffle.setIssue(1);
           raffleService.update(raffle);

        }





        redirect("/admin/raffle/list.jhtml");
    }

















}
