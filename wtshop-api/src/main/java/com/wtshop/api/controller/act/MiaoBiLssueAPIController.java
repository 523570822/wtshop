package com.wtshop.api.controller.act;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.RequestContextHolder;
import com.wtshop.Setting;
import com.wtshop.api.common.result.TuanGouGoodsMessageResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.Assert;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.SystemUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sq on 2017/6/8.
 */
@ControllerBind(controllerKey = "/api/miaobilssue")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class MiaoBiLssueAPIController extends BaseAPIController {
    /** 每页记录数 */
    private static final int PAGE_SIZE = 10;

    private MiaoBiLssueService miaoBiLssueService = enhance(MiaoBiLssueService.class);
    private  MiaoBiLssueLogService miaoBiLssueLogService = enhance(MiaoBiLssueLogService.class);
    private MiaobiLogService miaobiLogService = enhance(MiaobiLogService.class);
    private MemberService memberService = enhance(MemberService.class);

    private Res resZh = I18n.use();


    /**
     * 福袋主页
     */
    public void list() {
        Integer pageNumber = getParaToInt("pageNumbers");
        int status = getParaToInt("status");
        Member m=memberService.getCurrent();

        Map<String, Object> map = new HashedMap();
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);


        Page<MiaobiLssue> list = miaoBiLssueService.findPages(pageable,status,m.getId());
        // map.put("list", list);
        renderJson(ApiResult.success(list));
    }

    /**
     * 福袋主页
     */
    public void listRe() {



        Map<String, Object> map = new HashedMap();

       List<MiaobiLssue> list = miaoBiLssueService.findListRe();
        // map.put("list", list);
        renderJson(ApiResult.success(list));
    }
/**
 * 组团详情
 */
public void receive() throws ParseException {
    Long lssueId = getParaToLong("lssueId");
    Member m=memberService.getCurrent();
    List<MiaobiLssuelog> miaobiLog11 = miaoBiLssueLogService.findbylssueidMem(lssueId, m.getId());


    if(miaobiLog11.size()>0){
        renderJson(ApiResult.fail("已经领取过"));
        return;
    }

    MiaobiLssue ss = miaoBiLssueService.find(lssueId);
    MiaobiLssuelog  miaobiLssueLog=new MiaobiLssuelog();
    miaobiLssueLog.setBeginDate(ss.getBeginDate());
    miaobiLssueLog.setCount(ss.getCount());
    miaobiLssueLog.setEndDate(ss.getEndDate());
    miaobiLssueLog.setMemberId(m.getId());
    miaobiLssueLog.setExplain(ss.getExplain());
    miaobiLssueLog.setGroupnum(ss.getGroupnum());
    miaobiLssueLog.setMiaobilId(ss.getId());
    miaobiLssueLog.setTitle(ss.getTitle());
    miaobiLssueLog.setStatus(ss.getStatus());
    miaobiLssueLog.setNumber(ss.getNumber());
    miaoBiLssueLogService.save(miaobiLssueLog);
    int sendMiaoBi = ss.getNumber();
    MiaobiLog miaobiLog = new MiaobiLog();
    miaobiLog.setMemberId(m.getId());
    miaobiLog.setCredit(BigDecimal.valueOf(sendMiaoBi));
    miaobiLog.setDebit(BigDecimal.ZERO);
    miaobiLog.setType(0);
    miaobiLog.setMemo("活动赠送");
    miaobiLog.setBalance(m.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
    miaobiLogService.save(miaobiLog);
    //更新用户喵币
    m.setPoint(m.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
    memberService.update(m);
    Long time = 0L;
    time = Calendar.getInstance().getTimeInMillis();
 //   FightGroup fightGroup = fightGroupService.find(fightGroupL);
/*    fightGroup.setJiShi(fightGroup.getEndDate().getTime()- time);
    fightGroup.setSales(ss.getSales());
if(fightGroupL==0){

}else{

}*/
    Map<String, Object> map = new HashedMap();
    map.put("sendMiaoBi",sendMiaoBi);
    renderJson(ApiResult.success(map));

}




}
