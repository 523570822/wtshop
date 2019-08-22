package com.wtshop.api.controller.act;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sq on 2017/6/8.
 */
@ControllerBind(controllerKey = "/api/fudai")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class FuDaiAPIController extends BaseAPIController {

    private FuDaiService fuDaiService = enhance(FuDaiService.class);
    private GoodsService goodsService = enhance(GoodsService.class);
    private FuDaiProductService fuDaiProductService = enhance(FuDaiProductService.class);
    private OrderService orderService = enhance(OrderService.class);
    private MemberService memberService = enhance(MemberService.class);
    private MiaobiLogService miaobiLogService = enhance(MiaobiLogService.class);
    private ReceiverService receiverService = enhance(ReceiverService.class);
    private ProductService productService = enhance(ProductService.class);
    private ActIntroduceService actIntroduceService = enhance(ActIntroduceService.class);
    private CertificatesService certificatesService= enhance(CertificatesService.class);
    private Res resZh = I18n.use();


    /**
     * 福袋主页
     */
    public void fudai() {
        List<FuDai> list = fuDaiService.findLists();
        renderJson(list);
    }

    /**
     * 主福袋信息
     */
    public void primary() {
        Long fuDaiId = getParaToLong("fuDaiId");
        FuDai fuDai = fuDaiService.find(fuDaiId);
        fuDai.setExplain("rxm/goods/fuDai.html");
        FudaiProduct fudaiProduct = fuDaiProductService.findPrimary(fuDaiId);
        Product p = productService.find(fudaiProduct.getProductId()); //获取规格
        Goods goods = p.getGoods();
        Map<String, Object> map = new HashedMap();
        map.put("fuDai", fuDai);
        map.put("product", p);
        map.put("goods", goods);
        renderJson(ApiResult.success(map));
    }

    /**
     * 核对订单
     */
    public void check() {
        Long fuDaiId = getParaToLong("fuDaiId");
        FuDai fuDai = fuDaiService.find(fuDaiId);
        Member member = memberService.getCurrent();


        if(StringUtils.isEmpty(member.getOnShareCode())){
            renderJson(ApiResult.fail(7,"请填写邀请码"));
            return;
        }
        //是否实名认证
        Certificates certificates = certificatesService.queryByMemberId(member.getId());
        if(certificates != null && certificates.getState() != 1){
            renderJson(new ApiResult(101,"请先进行实名认证!"));
            return;
        }
        //默认收货人
        Receiver defaultReceiver = receiverService.findDefault(member);

        FudaiProduct fudaiProduct = fuDai.getFudaiProduct();
        Product product = fudaiProduct.getProduct();
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
        FuDai fuDai = fuDaiService.find(fudaiId);

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


        Order order = orderService.createFudai(Order.Type.fudai, fuDai, receiver,isInvoice,isPersonal,taxNumber,companyName);
        renderJson(ApiResult.success(order));

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

        Map<String, Object> item = new HashMap<String, Object>();
        Member m = memberService.getCurrent();
        Long orderId = getParaToLong("orderId");
        List<Goods> goodsByItemId = goodsService.findGoodsByItemId(orderId);
        item.put("shareCode",m.getShareCode());
        item.put("goodsByItemId",goodsByItemId);
        JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
        Map<String,Object>  map=  new HashMap<>();
        double sendMiaoBi=0;
        sendMiaoBi = redisSetting.getDouble("housekeeperSending") ;//邀请码赠送喵币

        MiaobiLog miaobiLog = new MiaobiLog();
        miaobiLog.setMemberId(m.getId());
        miaobiLog.setCredit(BigDecimal.valueOf(sendMiaoBi));
        miaobiLog.setDebit(BigDecimal.ZERO);
        miaobiLog.setType(0);
        miaobiLog.setMemo("购买福袋赠送");
        miaobiLog.setBalance(m.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
        //更新用户喵币
        m.setPoint(m.getPoint().add(BigDecimal.valueOf(sendMiaoBi)).setScale(2, BigDecimal.ROUND_HALF_UP));
        miaobiLogService.save(miaobiLog);
        memberService.update(m);
        item.put("sendMiaobi",sendMiaoBi);

            List<Order> orderList=orderService.findByMemberId(m.getId());
            if(orderList.size()>1){
                item.put("title","恭喜获得喵币");
            }else {
                item.put("title","恭喜成为管家");
            }






        item.put("type",1);
        item.put("miaobilId",redisSetting.getDouble("housekeeperSending"));//福袋赠送喵币

        renderJson(ApiResult.success(item));
    }


}
