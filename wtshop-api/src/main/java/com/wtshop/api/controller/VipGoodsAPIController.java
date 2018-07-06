package com.wtshop.api.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.plugin.monogodb.MongoKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.wtshop.Pageable;
import com.wtshop.RequestContextHolder;
import com.wtshop.Setting;
import com.wtshop.api.common.result.VipGoodsResult;
import com.wtshop.api.common.result.VipOrganResult;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.entity.Organ;
import com.wtshop.util.ApiResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ObjectUtils;
import com.wtshop.util.SystemUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sq on 2017/6/22.
 */
@ControllerBind(controllerKey = "/api/vipGoods")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class VipGoodsAPIController extends BaseAPIController {
    private GoodsService goodsService = enhance(GoodsService.class);
    private MemberService memberService = enhance(MemberService.class);
    private ReviewService reviewService = enhance(ReviewService.class);
    private FootPrintService footPrintService= enhance(FootPrintService.class);
    private ReceiverService receiverService = enhance(ReceiverService.class);
    private AreaDescribeService areaDescribeService = enhance(AreaDescribeService.class);
    private AreaService areaService = enhance(AreaService.class);
    private VipGoodsHistoryService vipGoodsHistoryService = enhance(VipGoodsHistoryService.class);

    /**
     * 列表
     */
    public void list() {

        Integer pageNumber = getParaToInt("pageNumber", 1);
        Integer pageSize = 10;
        Pageable pageable = new Pageable(pageNumber, pageSize);

        Page<Goods> page = goodsService.findVipPage(pageable);

        renderJson(ApiResult.success(page));
    }

    /**
     * 详情
     */
    @Before(Tx.class)
    public void detail() {
        Long id = getParaToLong("goodIds");
        Goods goods = goodsService.find(id);
        Pageable pageable = new Pageable(1, 20);
        Boolean favorite = false;
        if (goods == null) {
            return;
        }
        RequestContextHolder.setRequestAttributes(getRequest());
        Member perosn=memberService.getCurrent();
        if (goods.getFavoriteMembers().contains(perosn)) {
            favorite = true;
        }

        Page<Review> reviewPages = reviewService.findPage(null, goods, null, true, pageable);
        List<Review> list = reviewPages.getList();
        for(Review review:list){
            Member member = review.getMember();
            review.setMember(member);
        }
        Long reviewCount = reviewService.count(null, goods, null, true);//全部评论
        Long positiveCount = reviewService.count(null, goods, Review.Type.positive, true);
        Long moderateCount = reviewService.count(null, goods, Review.Type.moderate, true);
        Long negativeCount = reviewService.count(null, goods, Review.Type.negative, true);
        Long imagescount = 0L;
        List<Review> reviews = reviewService.count(null, goods, true);
        for(Review review : reviews){
            String images = review.getImages();
            if(images.contains("/")){
                imagescount += 1;
            }
        }

        String name = goods.getName();
        List<Tag> tags = goodsService.finTagList(goods.getId());

        //收货地址
        Receiver aDefault = receiverService.findDefault(perosn);
        //商品配送
        String receiveTime = null;
        if(aDefault != null){
            AreaDescribe areaDescribe = areaDescribeService.findByAreaId(aDefault.getAreaId());
            //判断本级地区是否填写
            if(areaDescribe != null && areaDescribe.getReceivingBegintime() != null){
                receiveTime = areaDescribe.getReceivingBegintime();
            }else {
                AreaDescribe areaDescribes = areaDescribeService.findByAreaId(areaService.find(aDefault.getAreaId()).getParentId());
                receiveTime = areaDescribes.getReceivingBegintime();
            }
        }

        //购物须知&正品保障
        Setting setting = SystemUtils.getSetting();
        String settingShoppingCopyUrl = setting.getShoppingCopyUrl();
        String shoppingCopy = setting.getShoppingCopy();
        String certifiedCopyUrl = setting.getCertifiedCopyUrl();
        String certifiedCopy = setting.getCertifiedCopy();

        VipGoodsResult vipGoodsResult = new VipGoodsResult(goods,name, favorite, reviewPages, reviewCount, positiveCount,
                moderateCount, negativeCount, imagescount, tags, aDefault, receiveTime, settingShoppingCopyUrl, shoppingCopy, certifiedCopyUrl ,certifiedCopy);
        //插入会员足迹
        if (perosn !=null){
            boolean isHas = footPrintService.findByTime(goods.getId(),perosn.getId());
            if(!isHas){
                Footprint footprint=new  Footprint();
                footprint.setGoodsId(goods.getId());
                footprint.setMemberId(perosn.getId());
                footPrintService.save(footprint);
            }
        }
        renderJson(ApiResult.success(vipGoodsResult));
    }


    /**
     * vip选择门店
     */
    @Before(Tx.class)
    public void exchange(){
        Long goodId = getParaToLong("goodId");
        Integer quantity = getParaToInt("quantity");

        Member member = memberService.getCurrent();
        Receiver receiver = receiverService.findDefault(member);
        Goods goods = goodsService.find(goodId);

        List<DBObject> organList = goodsService.findOrganList(member.getPhone());
        List<Organ> organs = new ArrayList<>();
        for(DBObject object : organList){
            Organ organ = goodsService.findOrgan(object.get("organId").toString());
            organs.add(organ);
        }
        if(organs.size() == 0){
            renderJson(ApiResult.fail("请您先绑定门店!"));
            return;
        }

        VipOrganResult vipOrganResult = new VipOrganResult(member, receiver, goods, organs, quantity);

        renderJson(ApiResult.success(vipOrganResult));

    }

    /**
     * vip商品订单提交
     */
    @Before(Tx.class)
    public void create(){
        Long receiverId = getParaToLong("receiverId");
        String organId = getPara("organId");
        Long goodId = getParaToLong("goodId");
        Integer quantity = getParaToInt("quantity");

        Member member = memberService.getCurrent();
        Receiver receiver = receiverService.find(receiverId);

        if (receiver == null || !member.equals(receiver.getMember())) {
            renderJson(ApiResult.fail("收货地址不能为空"));
            return;
        }

        goodsService.create(receiverId, organId, goodId, quantity);
        renderJson(ApiResult.success());
    }

    /**
     * vip置换记录
     */
    public void history(){
        Member member = memberService.getCurrent();
        Integer pageNumber = getParaToInt("pageNumber", 1);
        Integer pageSize = 10;
        Pageable pageable = new Pageable(pageNumber, pageSize);
        Page<VipgoodsHistory> page = vipGoodsHistoryService.findpage(member, pageable);
        renderJson(ApiResult.success(page));
    }

    /**
     * 用户是不是vip
     */
    public void role(){
        Member member = memberService.getCurrent();

        Integer state = 0;
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("phone",member.getPhone());
        DBObject vip = MongoKit.getCollection("vipMember").findOne(basicDBObject);
        if(! ObjectUtils.isEmpty(vip)){
            state = 1;
        }
        renderJson(ApiResult.success(state));
    }


    /**
     * vip置换规则
     */
    public void rule(){

        String url = "http://shop.rxmao.cn/rxm/goods/vipRule.html";

        renderJson(ApiResult.success(url));
    }







}
