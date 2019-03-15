package com.wtshop.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.model.DepositLog;
import com.wtshop.model.Member;
import com.wtshop.service.DepositLogService;
import com.wtshop.service.MemberService;

/**
 * Created by sq on 2017/8/25.
 */

@ControllerBind(controllerKey = "/admin/myWallet")
public class WalletController extends BaseController{

    private DepositLogService depositLogService = enhance(DepositLogService.class);
    private MemberService memberService = enhance(MemberService.class);

    /**
     * 记录
     */
    public void log() {
        Long memberId = getParaToLong("memberId");
        Pageable pageable = getBean(Pageable.class);
        Member member = memberService.find(memberId);
        if (member != null) {
            setAttr("member", member);
            setAttr("page", depositLogService.findPage(member, pageable,null));
        } else {
            Page<DepositLog> search = depositLogService.findPageBySearch(pageable);
            setAttr("page", search);
        }
        setAttr("pageable", pageable);
        render("/admin/wallet/log.ftl");
    }

    /**
     * 列表
     */
    public void list(){
        Pageable pageable = getBean(Pageable.class);
        setAttr("page", memberService.findBalancePage(pageable));
        setAttr("pageable", pageable);
        render("/admin/wallet/list.ftl");
    }

}
