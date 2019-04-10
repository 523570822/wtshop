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
    private FootPrintService footPrintService= enhance(FootPrintService.class);
    private ConsultationService consultationService = enhance(ConsultationService.class);
    private MiaoBiLssueService miaoBiLssueService = enhance(MiaoBiLssueService.class);
    private GoodsService goodsService = enhance(GoodsService.class);
    private GroupRemindService groupRemindService=enhance(GroupRemindService.class);
    private AreaService areaService = enhance(AreaService.class);
    private ReceiverService receiverService = enhance(ReceiverService.class);
    private AreaDescribeService areaDescribeService = enhance(AreaDescribeService.class);
    private ReviewService reviewService = enhance(ReviewService.class);
    private OrderService orderService = enhance(OrderService.class);
    private MemberService memberService = enhance(MemberService.class);
    private  FightGroupService fightGroupService= enhance(FightGroupService.class);
    private ProductService productService = enhance(ProductService.class);
    private ActIntroduceService actIntroduceService = enhance(ActIntroduceService.class);
    private CertificatesService certificatesService= enhance(CertificatesService.class);

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
public void groupDetails() throws ParseException {
    Long fightGroupL = getParaToLong("fightGroup");
    Long tuanGouId = getParaToLong("tuanGouId");
    List<Order> order = orderService.findByfightgroupId(fightGroupL);

    MiaobiLssue ss = miaoBiLssueService.find(tuanGouId);

    Long time = 0L;
    time = Calendar.getInstance().getTimeInMillis();
    FightGroup fightGroup = fightGroupService.find(fightGroupL);
    fightGroup.setJiShi(fightGroup.getEndDate().getTime()- time);
    fightGroup.setSales(ss.getSales());
if(fightGroupL==0){

}else{

}

    Map<String, Object> map = new HashedMap();
    map.put("goods", fightGroup.getProduct().getGoods());
    map.put("fightGroup",fightGroup);
    map.put("order",order);
    renderJson(ApiResult.success(map));

}




}
