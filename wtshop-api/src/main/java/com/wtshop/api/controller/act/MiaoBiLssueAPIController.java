package com.wtshop.api.controller.act;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.Page;
import com.wtshop.Pageable;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.RedisUtil;
import org.apache.commons.collections.map.HashedMap;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
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
    m.setJieritixingNum(true);
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
/**
 * 组团详情
 */
public void share() throws ParseException {
    Member m=memberService.getCurrent();
   int  type=4;
   List<MiaobiLog> sss = miaobiLogService.findLogByMemberId(m.getId(), type);
    if(sss.size()>0){
        renderJson(ApiResult.fail("已经领取过"));
        return;
    }
    JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
    double sendMiaoBi=0;
    sendMiaoBi = redisSetting.getDouble("housekeeperSending") ;//邀请码赠送喵币
    MiaobiLog miaobiLog = new MiaobiLog();
    miaobiLog.setMemberId(m.getId());
    miaobiLog.setCredit(BigDecimal.valueOf(sendMiaoBi));
    miaobiLog.setDebit(BigDecimal.ZERO);
    miaobiLog.setType(type);
    miaobiLog.setMemo("分享成功赠送");
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
    map.put("sendMiaobi",sendMiaoBi);
    map.put("title","分享成功");
    map.put("type",1);
    map.put("miaobilId",0);
    renderJson(ApiResult.success(map));

}

/**
 * jie
 */
public void judgingFestival() throws ParseException {
    Member m=memberService.getCurrent();
    Pageable pageable = new Pageable(1, PAGE_SIZE);
    if(!m.getJieritixingNum()){
        Map<String, Object> map = new HashedMap();
        Page<MiaobiLssue> miao = miaoBiLssueService.findPages(pageable, 2, m.getId());
        if( miao.getList().size()>0){
            miao.getList().get(0).getId();
            map.put("sendMiaobi", miao.getList().get(0).getNumber());
            map.put("title", miao.getList().get(0).getTitle());
            map.put("type",2);
            map.put("miaobilId", miao.getList().get(0).getId());
            renderJson(ApiResult.success(map));
      //      m.setJieritixingNum(true);
       //     memberService.update(m);
            return;
        }
    }
        renderJson(ApiResult.fail("已提醒过"));
        return;
}



    /**
     * 组团详情
     */
    public void close() throws ParseException {
        Member m=memberService.getCurrent();
                m.setJieritixingNum(true);
        memberService.update(m);
                renderJson(ApiResult.success());
                return;



    }

}
