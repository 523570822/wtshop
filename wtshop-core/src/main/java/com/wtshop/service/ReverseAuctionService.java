package com.wtshop.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wtshop.dao.*;
import com.wtshop.model.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.UUIDUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 倒拍业务逻辑类
 * Created by 蔺哲 on 2017/5/10.
 */
public class ReverseAuctionService extends BaseService<ReverseAuction> {

    public ReverseAuctionService() {
        super(ReverseAuction.class);
    }

    private ReverseAuctionDetailDao reverseAuctionDetailDao = Enhancer.enhance(ReverseAuctionDetailDao.class);
    private ReviewService reviewService = Enhancer.enhance(ReviewService.class);
    private GoodsService goodsService = Enhancer.enhance(GoodsService.class);
    private ReceiverService receiverService = Enhancer.enhance(ReceiverService.class);
    private MemberService memberService = Enhancer.enhance(MemberService.class);

    private ProductDao productDao = Enhancer.enhance(ProductDao.class);
    private ReverseAuctionHistroyDao reverseAuctionHistroyDao = Enhancer.enhance(ReverseAuctionHistroyDao.class);
    private ReverseAuctionOrderDao reverseAuctionOrderDao = Enhancer.enhance(ReverseAuctionOrderDao.class);
    private SnDao snDao = Enhancer.enhance(SnDao.class);
    private OrderDao orderDao = Enhancer.enhance(OrderDao.class);
    private OrderItemDao orderItemDao = Enhancer.enhance(OrderItemDao.class);
    private OrderLogDao orderLogDao = Enhancer.enhance(OrderLogDao.class);
    private DepositLogDao depositLogDao = Enhancer.enhance(DepositLogDao.class);

    @Before(Tx.class)
    public ApiResult detail(Long memberId, String auctionId, String detailId, Long productId) {
        ApiResult result = ApiResult.fail();
        try {
            ReverseAuctionDetail reverseAuctionDetail = reverseAuctionDetailDao.find(Long.valueOf(detailId));
            Product product = productDao.find(productId);
            Goods goods = product.getGoods();
            Long reviewCount = reviewService.count(null, goods, null, true);//全部评论
            Long positiveCount = reviewService.count(null, goods, Review.Type.positive, true);
            Long moderateCount = reviewService.count(null, goods, Review.Type.moderate, true);
            Long negativeCount = reviewService.count(null, goods, Review.Type.negative, true);
            //Long imagesCount = reviewService.count(null, good, null);
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("reviewCount", reviewCount);
            resultMap.put("positiveCount", positiveCount);
            resultMap.put("moderateCount", moderateCount);
            resultMap.put("negativeCount", negativeCount);
            Long imagesCount = 0L;
            List<Review> reviews = reviewService.count(null, goods, null);
            for (Review review : reviews) {
                String images = review.getImages();
                if (images.contains("/")) {
                    imagesCount += 1;
                }
            }
            resultMap.put("imagesCount", imagesCount);
            //获取标签
            List<Tag> tags = goodsService.finTagList(goods.getId());
            resultMap.put("tags", tags);
            //获取历史订单5条
            List<Map> historyList = new ArrayList<>();
            List<Record> recordList = Db.find("select * from `reverse_auction_histroy` where `reverse_pruduct_id` = ? order by buy_time desc limit 5", productId);
            for (Record record : recordList) {
                Map<String, Object> map = new HashMap<>();
                map.put("buy_username", record.getStr("user_name"));
                map.put("buy_time", record.getDate("buy_time"));
                map.put("buy_price", record.getDouble("buy_price"));
                historyList.add(map);
            }
            resultMap.put("historyList", historyList);
            //获取商品内容明细
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("goodsId", goods.getId());
            productMap.put("image", goods.getImage());
            productMap.put("introduction", goods.getIntroduction());
            productMap.put("title", goods.getName());
            productMap.put("subtitle", "本商品每分钟降价");
            productMap.put("original_price", reverseAuctionDetail.getAuctionOriginalPrice());
            productMap.put("start_price", reverseAuctionDetail.getAuctionStartPrice());
            Record favRecord = Db.findFirst("SELECT * FROM `member_favorite_goods` mfg  where mfg.`favorite_members` = ? and mfg.`favorite_goods` = ? limit 1", memberId, goods.getId());
            productMap.put("isFavorite", favRecord != null);
            //  存在购买历史，获取最近的一条购买历史
            if (CollectionUtils.isNotEmpty(recordList)) {
                Record record = recordList.get(0);
                productMap.put("buy_username", record.getStr("user_name"));
                productMap.put("buy_price", record.getDouble("buy_price"));
            }
            resultMap.put("pro", productMap);
            //  倒拍详情评价
            resultMap.put("reviewList", new ArrayList<>());

            //  倒拍轮播图
            List imageList = new ArrayList<>();
            if (StringUtils.isNotBlank(goods.getProductImages())) {
                imageList = JSONArray.parseArray(goods.getProductImages());
            }
            resultMap.put("imageList", imageList);
            result = ApiResult.success(resultMap);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    //  微信倒拍回调
    @Before(Tx.class)
    public ApiResult handleWechatPayCallback(Map<String, String> callbackMap) {
        String payNo = callbackMap.get("out_trade_no");
        String otherNo = callbackMap.get("transaction_id");
        String amount = callbackMap.get("total_fee");
        String returnCode = callbackMap.get("return_code");
        int payState = "SUCCESS".equals(returnCode) ? 1 : 2;


        //  保存回调记录
        ReverseAuctionOrder order = new ReverseAuctionOrder();
        order.setAuctionPayNo(payNo);
        order.setAuctionOtherNo(otherNo);
        order.setAuctionPayChannel("wechat");
        order.setAuctionPayState(payState);
        order.setCallbackSummary(JSONObject.toJSONString(callbackMap));
        reverseAuctionOrderDao.save(order);

        //  获取待支付历史
        ReverseAuctionHistroy histroy = ReverseAuctionHistroy.dao.findFirst("select * from `reverse_auction_histroy` where `buy_no` = ? limit 1", payNo);
        if (histroy == null) {
            return ApiResult.fail("订单不存在");
        }
        histroy.setBuyState(2);
        reverseAuctionHistroyDao.update(histroy);
        updateReverseAuctionOrderToPendingShip(histroy, Order.PayType.wechat, String.valueOf(NumberUtils.toDouble(amount) / 100.0), "0", otherNo);
        return ApiResult.success();
    }

    //  支付宝倒拍回调
    @Before(Tx.class)
    public ApiResult handleAlipayPayCallback(Map<String, String> callbackMap) {
        String payNo = callbackMap.get("out_trade_no");
        String otherNo = callbackMap.get("trade_no");
        String amount = callbackMap.get("total_amount");
        String tradeStatus = callbackMap.get("trade_status");
        int payState = "TRADE_SUCCESS".equals(tradeStatus) ? 1 : 2;

        //  保存回调记录
        ReverseAuctionOrder order = new ReverseAuctionOrder();
        order.setAuctionPayNo(payNo);
        order.setAuctionOtherNo(otherNo);
        order.setAuctionPayChannel("alipay");
        order.setAuctionPayState(payState);
        order.setCallbackSummary(JSONObject.toJSONString(callbackMap));
        reverseAuctionOrderDao.save(order);

        //  获取待支付历史
        ReverseAuctionHistroy histroy = ReverseAuctionHistroy.dao.findFirst("select * from `reverse_auction_histroy` where `buy_no` = ? limit 1", payNo);
        if (histroy == null) {
            return ApiResult.fail("订单不存在");
        }
        //  记录订单
        histroy.setBuyState(2);
        reverseAuctionHistroyDao.update(histroy);
        updateReverseAuctionOrderToPendingShip(histroy, Order.PayType.alipay, amount, "0", otherNo);

        return ApiResult.success();
    }

    public String buildReverseAuctionBusinessKey() {
        return "REVERSE_AUCTION:" + UUIDUtils.getStringUUID();
    }

    @Before(Tx.class)
    public Order updateReverseAuctionOrderToPendingShip(ReverseAuctionHistroy reverseAuctionHistroy, Order.PayType payType, String amount, String balance, String orderNo) {
        Member member = memberService.find(reverseAuctionHistroy.getMemberId());
        Order order = orderDao.findByActOrderId(String.valueOf(reverseAuctionHistroy.getReverseAuctionDetailId()));
        order.setAmountPaid(new BigDecimal(balance));
        order.setOrderNo(orderNo);
        if (payType == Order.PayType.wechat) {
            order.setWeixinPaid(new BigDecimal(amount));
        }else {
            order.setWeixinPaid(BigDecimal.ZERO);
        }
        if (payType == Order.PayType.alipay) {
            order.setAliPaid(new BigDecimal(amount));
        } else {
            order.setAliPaid(BigDecimal.ZERO);
        }
        order.setStatus(Order.Status.pendingShipment.ordinal());
        orderDao.update(order);

        //  更新用户余额
        memberService.addAmount(member, order.getAmount());
        if (order.getAmountPaid().doubleValue() > 0) {
            BigDecimal decimal = member.getBalance().subtract(order.getAmountPaid()).setScale(2, RoundingMode.UP);
            if (decimal.doubleValue() < 0) {
                decimal = BigDecimal.ZERO;
            }
            member.setBalance(decimal);
            memberService.update(member);
            //添加余额消费记录
            DepositLog depositLog = new DepositLog();
            depositLog.setBalance(decimal);
            depositLog.setCredit(new BigDecimal("0"));
            depositLog.setDebit(order.getAmountPaid());
            depositLog.setMemo("倒拍支付");
            depositLog.setType(DepositLog.Type.payment.ordinal());
            depositLog.setOrderId(order.getId());
            depositLog.setMemberId(member.getId());
            depositLogDao.save(depositLog);
        }
        return order;
    }

    //  创建倒拍订单（待支付）
    @Before(Tx.class)
    public Order createReverseAuctionOrderToPendingPayment(Long reverseAuctionHistroyId, long maxPayTimeInSecond) {
        ReverseAuctionHistroy reverseAuctionHistroy = reverseAuctionHistroyDao.find(reverseAuctionHistroyId);
        Order order = new Order();
        order.setSn(snDao.generate(Sn.Type.order));
        order.setType(Order.Type.daopai.ordinal());
        order.setPrice(new BigDecimal(reverseAuctionHistroy.getBuyPrice()));
        order.setFee(BigDecimal.ZERO);
        order.setFreight(BigDecimal.ZERO);
        order.setMiaobiPaid(BigDecimal.ZERO);
        order.setPromotionDiscount(BigDecimal.ZERO);
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setAmountPaid(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setRewardPoint(0L);
        order.setExchangePoint(0L);
        order.setWeight(0);
        order.setQuantity(1);
        order.setAmount(new BigDecimal(reverseAuctionHistroy.getBuyPrice()));
        order.setReturnCopyPaid(BigDecimal.ZERO);
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setIsDelete(false);

        order.setWeixinPaid(BigDecimal.ZERO);
        order.setAliPaid(BigDecimal.ZERO);

        Member member = memberService.find(reverseAuctionHistroy.getMemberId());
        Receiver receiver = receiverService.findDefault(member);
        if (receiver != null) {
            order.setConsignee(receiver.getConsignee());
            order.setAreaName(receiver.getAreaName());
            order.setAddress(receiver.getAddress());
            order.setZipCode(receiver.getZipCode());
            order.setPhone(receiver.getPhone());
            order.setArea(receiver.getArea());
            order.setAreaId(receiver.getAreaId());
        }
        order.setMemo(null);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        order.setInvoice(null);
        order.setShippingMethodId(1L);
        order.setMemberId(reverseAuctionHistroy.getMemberId());
        order.setPromotionNames(null);
        order.setCoupons(null);
        order.setIsVip(false);
        order.setTax(BigDecimal.ZERO);
        order.setStatus(Order.Status.pendingPayment.ordinal());
        order.setPaymentMethod(null);
        order.setPaymentMethodId(1L);
        order.setPaymentMethodName("倒拍支付");
        order.setExpire(DateUtils.addMinutes(new Date(), (int)(maxPayTimeInSecond / 60)));
        order.setActOrderId(reverseAuctionHistroy.getReverseAuctionDetailId().toString());
        if (order.getArea() != null) {
            order.setAreaName(order.getArea().getFullName());
        }
        if (order.getPaymentMethod() != null) {
            order.setPaymentMethodName(order.getPaymentMethod().getName());
            order.setPaymentMethodType(order.getPaymentMethod().getType());
        }
        if (order.getShippingMethod() != null) {
            order.setShippingMethodName(order.getShippingMethod().getName());
        }

        List<OrderItem> orderItems = order.getOrderItems();

        Product product = productDao.find(reverseAuctionHistroy.getReversePruductId());
        OrderItem orderItem = new OrderItem();
        orderItem.setSn(product.getSn());
        orderItem.setName(product.getName());
        orderItem.setType(product.getType().ordinal());
        orderItem.setPrice(new BigDecimal(reverseAuctionHistroy.getBuyPrice()));
        orderItem.setWeight(product.getWeight());
        orderItem.setIsDelivery(product.getIsDelivery());
        orderItem.setThumbnail(product.getThumbnail());
        orderItem.setQuantity(1);
        orderItem.setShippedQuantity(0);
        orderItem.setReturnedQuantity(0);
        orderItem.setProductId(reverseAuctionHistroy.getReversePruductId());
        orderItem.setSpecifications(JSON.toJSONString(product.getSpecifications()));
        orderItems.add(orderItem);
        orderDao.save(order);
        if (CollectionUtils.isNotEmpty(orderItems)) {
            for (OrderItem orderItem1 : orderItems) {
                orderItem1.setOrderId(order.getId());
                orderItemDao.save(orderItem1);
            }
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create.ordinal());
        orderLog.setOrderId(order.getId());
        orderLogDao.save(orderLog);
        return order;
    }

}
