package com.wtshop.api.controller.act;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.RequestContextHolder;
import com.wtshop.Setting;
import com.wtshop.api.common.result.GoodsMessageResult;
import com.wtshop.api.common.result.TuanGouGoodsMessageResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.SystemUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sq on 2017/6/8.
 */
@ControllerBind(controllerKey = "/api/groupbuy")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class GroupBuyAPIController extends BaseAPIController {
    /** 每页记录数 */
    private static final int PAGE_SIZE = 10;
    private FootPrintService footPrintService= enhance(FootPrintService.class);
    private ConsultationService consultationService = enhance(ConsultationService.class);
    private GroupBuyService fuDaiService = enhance(GroupBuyService.class);
    private GoodsService goodsService = enhance(GoodsService.class);
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
        boolean status = getParaToBoolean("status");


        Map<String, Object> map = new HashedMap();
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);


        Page<GroupBuy> list = fuDaiService.findPages(pageable,status);
        // map.put("list", list);
        renderJson(ApiResult.success(list));
    }

    /**
     * 福袋主页
     */
    public void listRe() {



        Map<String, Object> map = new HashedMap();

       List<GroupBuy> list = fuDaiService.findListRe();
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

    GroupBuy ss = fuDaiService.find(tuanGouId);

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

    /**
     * 团购详情信息
     */
    public void primary() throws ParseException {
        Map<String, String[]> ss = getParaMap();
        Long fuDaiId = getParaToLong("tuanGouId");
        GroupBuy fuDai = fuDaiService.find(fuDaiId);
      /*  List<FightGroup> fightgroupList=new  ArrayList<FightGroup>();*/


        //可拼团认
        List<FightGroup> fightgroupList=fightGroupService.findByProductId(fuDai.getProductId());

        Product p = fuDai.getProduct();
        Goods goods = p.getGoods();
        Map<String, Object> map = new HashedMap();

        Long id = goods.getId();
        String type = getPara("type"); //是否喵币商品
        Member perosn=memberService.getCurrent();
        Pageable pageable = new Pageable(1, 20);
        Boolean favorite = false;

        if (goods == null) {
            return;
        }
        List<Area> areas = areaService.findParents(goods.getArea(), true, null);
        goods.setAttributeValue0(areas.get(0).getName());

        RequestContextHolder.setRequestAttributes(getRequest());
        Member m=memberService.getCurrent();
        if (goods.getFavoriteMembers().contains(m)) {
            favorite = true;
        }
        Page<Consultation> consultationPages = consultationService.findPage(null, goods, true, pageable);
        Page<Review> reviewPages = reviewService.findPageList(null, goods, null, true, pageable);
        List<Review> list = reviewPages.getList();
        for(Review review:list){
            Date dd = review.getModifyDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String s = sdf.format(dd);

            Date date =  sdf.parse(s);
            review.setModifyDate(date);
            Member member = memberService.find(review.getMemberId());
            String nickname = member.getNickname();
            if(review.getIsAnonymous() != null && review.getIsAnonymous()){
                if(StringUtils.isNotBlank(nickname)){
                    String first = nickname.substring(0,1);
                    String end = nickname.substring(nickname.length()-1);
                    review.setOrderContent(first+"**"+end);
                }else {
                    review.setOrderContent("**");
                }
            }else {
                review.setOrderContent(nickname);
            }
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
        List<Product> productList = productService.findProductList(id);
        String name = goods.getName();
        List<Tag> tags = goodsService.finTagList(goods.getId());



        //购物须知&正品保障&关税
        Setting setting = SystemUtils.getSetting();
        String settingShoppingCopyUrl = setting.getShoppingCopyUrl();
        String certifiedCopyUrl = setting.getCertifiedCopyUrl();
        String taxExplainUrl = setting.getTaxExplainUrl();
        Product byGoodsId = productService.findByGoodsId(goods.getId());
        Integer stock = byGoodsId.getStock();


        //插入会员足迹
        if (m !=null){
            boolean isHas = footPrintService.findByTime(goods.getId(),perosn.getId());
            if(!isHas){
                Footprint footprint=new  Footprint();
                footprint.setGoodsId(goods.getId());
                footprint.setMemberId(perosn.getId());
                footPrintService.save(footprint);
            }
        }
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
                if(areaDescribes !=null){
                    receiveTime = areaDescribes.getReceivingBegintime();
                }

            }
        }
        String 	freeMoney= RedisUtil.getString("freeMoney");
        String freMon;
        if(freeMoney==null||freeMoney.trim().equals("")||freeMoney.trim().equals("null")||freeMoney.trim().equals("0")){
            freMon=	"包邮";
        }else{
            freMon="订单满"+freeMoney+"元包邮";
        }


        TuanGouGoodsMessageResult goodsMessageResult = new TuanGouGoodsMessageResult(stock,goods,name, favorite, consultationPages, reviewPages, reviewCount,positiveCount,moderateCount,negativeCount,imagescount,tags,productList,settingShoppingCopyUrl,certifiedCopyUrl,taxExplainUrl,aDefault,receiveTime,freMon,fuDai,fightgroupList);
        renderJson(ApiResult.success(goodsMessageResult));


    }

    /**
     * 核对订单
     */
    public void check() {
        Long fuDaiId = getParaToLong("fuDaiId");
        GroupBuy fuDai = fuDaiService.find(fuDaiId);
        Member member = memberService.getCurrent();
        //是否实名认证
        Certificates certificates = certificatesService.queryByMemberId(member.getId());
        if(certificates != null && certificates.getState() != 1){
            renderJson(new ApiResult(101,"请先进行实名认证!"));
            return;
        }
        //默认收货人
        Receiver defaultReceiver = receiverService.findDefault(member);


        Product product = fuDai.getProduct();
        Goods goods = product.getGoods();

        Map<String, Object> map = new HashedMap();
        //    map.put("deliveryList",deliverieList);
        map.put("goods", goods);
        map.put("product", product);
        map.put("receiver", defaultReceiver);
        map.put("balance", member.getBalance());
        map.put("fuDai", fuDai);
        renderJson(ApiResult.success(map));
    }

    /**
     * 创建订单
     */
    public void create() {
        Member member = memberService.getCurrent();
        Long fudaiId = getParaToLong("fuDaiId");
        Long receiverId = getParaToLong("receiverId");
        Receiver receiver = receiverService.find(receiverId);
        GroupBuy fuDai = fuDaiService.find(fudaiId);

        if (member == null) {
            renderJson(ApiResult.fail("当前用户为空!"));
            return;
        }

        if (receiver == null || !member.equals(receiver.getMember())) {
            renderJson(ApiResult.fail("收货地址不能为空"));
            return;
        }
//1是 ，0否  是否開發票
        Boolean isInvoice=getParaToBoolean("isInvoice");
        //1是 ，0否  是否是個人
        Boolean isPersonal=getParaToBoolean("isPersonal");
        String taxNumber = getPara("taxNumber"); 	//單位名稱
        String companyName = getPara("companyName"); 	//稅號

//一会要写创建团购订单
       // Order order = orderService.createFudai(Order.Type.fudai, fuDai, receiver,isInvoice,isPersonal,taxNumber,companyName);
        renderJson(ApiResult.success(null));

    }


    //福袋购买记录
    public void payRecord() {
        Integer pageNumber = getParaToInt("pageNumber", 1);
        Pageable pageable = new Pageable(pageNumber, 10);
        renderJson(fuDaiService.winRecord(pageable));
    }

    //排行榜
    public void rankList() {
        Pageable pageable = getBean(Pageable.class);
        renderJson(fuDaiService.rankList(pageable));
    }

    //福袋说明
    public void explain() {
        String url = "http://shop.rxmao.cn/rxm/goods/fuDai.html";
        renderJson(ApiResult.success(url));
    }

    //获取个人幸运记录
    public void rankDetail() {

        long orderId = getParaToLong("orderId");
        renderJson(ApiResult.success(fuDaiService.getRankDetail(orderId)));

    }

    /**
     *
     */
    public void goodsByOrderId() {
        Long orderId = getParaToLong("orderId");
        List<Goods> goodsByItemId = goodsService.findGoodsByItemId(orderId);
        renderJson(ApiResult.success(goodsByItemId));
    }


}
