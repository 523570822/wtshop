package com.wtshop.api.controller.act;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ServletKit;
import com.jfinal.ext.route.ControllerBind;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.dao.TicketconfigDao;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.Ticketconfig;
import com.wtshop.model.Ticketsn;
import com.wtshop.service.CouponShareService;
import com.wtshop.service.TicketSnService;
import com.wtshop.util.ApiResult;

/**
 * Created by Administrator on 2017/8/7.
 */
@ControllerBind(controllerKey = "/act/ticketShare")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class TicketShareAppControll extends Controller {

    private CouponShareService css = enhance(CouponShareService.class);
    private TicketSnService tss = enhance(TicketSnService.class);
    private TicketconfigDao configDao = Enhancer.enhance(TicketconfigDao.class);


    public void info() {
        String sn = getPara("sn");
        Ticketsn ticketsnBySn = tss.getTicketsnBySn(sn);
        Ticketconfig ticketconfig = null;
        if (ticketsnBySn != null) {
            ticketconfig = configDao.find(ticketsnBySn.getConfigId());
        }

        setAttr("ticketsn", ticketsnBySn);
        setAttr("ticketconfig", ticketconfig);
        render("/admin/ticketConfig/share.ftl");
        //renderJson(css.info(cShareId));
    }

    //抽券
    public void extractTicket() {
        String sn = getPara("sn");
        String phone = getPara("phone");
        ApiResult apiResult = tss.extractTicket(phone, sn, ServletKit.getIp(getRequest()));
        renderJson(apiResult);
    }


    //生成链接
    public void generateLink() {
        String orderId = getPara("orderId");
        renderJson(tss.generateLink(orderId));
    }


}
