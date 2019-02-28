package com.wtshop.api.controller.act;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import org.apache.commons.collections.map.HashedMap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sq on 2017/6/8.
 */
@ControllerBind(controllerKey = "/api/groupbuy")
@Before({WapInterceptor.class, ErrorInterceptor.class, TokenInterceptor.class})
public class GroupBuyAPIController extends BaseAPIController {
    /** 每页记录数 */
    private static final int PAGE_SIZE = 10;
    private GroupBuyService fuDaiService = enhance(GroupBuyService.class);
    private GoodsService goodsService = enhance(GoodsService.class);

    private OrderService orderService = enhance(OrderService.class);
    private MemberService memberService = enhance(MemberService.class);
    private ReceiverService receiverService = enhance(ReceiverService.class);
    private ProductService productService = enhance(ProductService.class);
    private ActIntroduceService actIntroduceService = enhance(ActIntroduceService.class);
    private CertificatesService certificatesService= enhance(CertificatesService.class);
    private Res resZh = I18n.use();


    /**
     * 福袋主页
     */
    public void list() {
        Integer pageNumber = getParaToInt("pageNumbers");


        Map<String, Object> map = new HashedMap();
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);


        Page<GroupBuy> list = fuDaiService.findPages(pageable);
        map.put("list", list);
        renderJson(ApiResult.success(map));
    }

    /**
     * 团购详情信息
     */
    public void primary() {
        Long fuDaiId = getParaToLong("tuanGouId");
        GroupBuy fuDai = fuDaiService.find(fuDaiId);
        List<FightGroup> fightgroupList=new  ArrayList<FightGroup>();
        FightGroup fightGroup=new FightGroup();
        fightGroup.setId(1L);
        fightGroup.setSales(2);
        fightGroup.setGroupnum(10);
        fightgroupList.add(fightGroup);
        FightGroup fightGroup1=new FightGroup();
        fightGroup1.setId(2L);
        fightGroup1.setSales(3);
        fightGroup1.setGroupnum(10);
        fightgroupList.add(fightGroup1);
        Product p = fuDai.getProduct();
        Goods goods = p.getGoods();
        Map<String, Object> map = new HashedMap();
        map.put("groupbuy", fuDai);
        map.put("product", p);
        map.put("goods", goods);
        map.put("fightgroup",fightgroupList);
        renderJson(ApiResult.success(map));
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
