package com.wtshop.api.controller.act;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.redis.Redis;
import com.wtshop.api.common.result.member.ReverseAuctionDetailResult;
import com.wtshop.api.common.result.member.ReverseAuctionResult;
import com.wtshop.api.controller.BaseAPIController;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.api.interceptor.TokenInterceptor;
import com.wtshop.constants.Code;
import com.wtshop.dao.OrderDao;
import com.wtshop.dao.ReverseAuctionHistroyDao;
import com.wtshop.interceptor.WapInterceptor;
import com.wtshop.model.*;
import com.wtshop.service.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.IpUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 * Created by Administrator on 2017/6/2.
 * 倒拍活动
 */
@ControllerBind(controllerKey = "/api/act/reverseAuction")
@Before({WapInterceptor.class, ErrorInterceptor.class})
public class ReverseAuctionController extends BaseAPIController {

    private MemberService memberService = enhance(MemberService.class);
    private ReverseAuctionService reverseAuctionService = enhance(ReverseAuctionService.class);
    private ReverseAuctionHistroyService reverseAuctionHistroyService = enhance(ReverseAuctionHistroyService.class);
    private UserPayService userPayService = enhance(UserPayService.class);
    private CertificatesService certificatesService = enhance(CertificatesService.class);
    private OrderDao orderDao = enhance(OrderDao.class);
    private ReverseAuctionHistroyDao reverseAuctionHistroyDao = enhance(ReverseAuctionHistroyDao.class);

    //  获取倒拍列表
    public void auctionList() {

        String auctionId = getPara("auctionId");
        ReverseAuction reverseAuction = reverseAuctionService.find(Long.valueOf(auctionId));
        if (reverseAuction == null) {
            renderJson(ApiResult.fail("倒拍不存在"));
            return;
        }
        ReverseAuctionResult auctionResult = new ReverseAuctionResult();
        auctionResult.setAuctionId(reverseAuction.getId());
        auctionResult.setTitle(reverseAuction.getTitle());
        auctionResult.setDownRule("本商品每分钟降价");
        String sql = "select d.*, g.`name`, g.`image` from `reverse_auction_detail` d , product p, goods g  where d.`reverse_auction_id` = ?  and d.`product_id` = p.`id`  and p.`goods_id` = g.id";
        List<ReverseAuctionDetailResult> resultList = new ArrayList<>();
        List<Record> list = Db.find(sql, auctionId);
        for (Record record : list) {
            ReverseAuctionDetailResult result = new ReverseAuctionDetailResult();
            result.setTitle(record.get("name"));
            result.setDetailId(record.getLong("id").toString());
            result.setProductId(record.getLong("product_id").toString());
            result.setAuctionOriginalPrice((long) (record.getDouble("auction_original_price") * 100));
            result.setImage(record.get("image"));
            resultList.add(result);
        }
        auctionResult.setDetailResultList(resultList);
        renderJson(ApiResult.success(auctionResult));
    }

    //  获取上一期倒拍列表
    public void prevAuctionList() {
        Record reverseAuctionRecord = Db.findFirst("select * from `reverse_auction` ra where ra.`state` = 10  order by ra.end_date desc limit 1");
        if (reverseAuctionRecord == null) {
            renderJson(ApiResult.fail("暂无上一期倒拍"));
            return;
        }
        _renderAuctionList(reverseAuctionRecord);
    }

    //  获取下一期倒拍列表
    public void nextAuctionList() {
        Record reverseAuctionRecord = Db.findFirst("select * from `reverse_auction` ra where ra.`state` = 8  order by ra.end_date desc limit 1");
        if (reverseAuctionRecord == null) {
            renderJson(ApiResult.fail("暂无下一期倒拍"));
            return;
        }
        _renderAuctionList(reverseAuctionRecord);
    }

    //  获取倒排详情
    public void detail() {
        Member member = memberService.getCurrent();
        String auctionId = getPara("auctionId");
        String auctionDetailId = getPara("auctionDetailId");
        Long productId = getParaToLong("productId");
        ApiResult result = reverseAuctionService.detail(member.getId(), auctionId, auctionDetailId, productId);
        renderJson(result);
    }

    //  历史成交
    public void historyList() {
        Member member = memberService.getCurrent();
        List<Record> resultList = Db.find("SELECT rah.`reverse_auction_id`, rah.`reverse_auction_detail_id`, " +
                "rah.`reverse_pruduct_id`,rah.`buy_time`,rah.`user_name`,rah.`start_price`,rah.`buy_price`,g.`name`,g.`image` " +
                "FROM reverse_auction_histroy rah, product p, goods g " +
                "WHERE rah.`reverse_pruduct_id` = p.id " +
                "AND p.`goods_id` = g.`id`  and rah.`member_id` = ? ORDER BY rah.`buy_time` DESC LIMIT 20", member.getId());
        renderJson(ApiResult.success(resultList));
    }

    //  倒拍说明
    public void explain() {
        render("/admin/act/explain.ftl");
    }

    //  进入倒拍首页
    public void auctionIndex() {
        Member member = memberService.getCurrent();
        Map<String, Object> resultMap = new HashMap<>();
        //是否实名认证
        int isCert = Code.TRUE;
        Certificates certificates = certificatesService.queryByMemberId(member.getId());
        if (certificates != null && certificates.getState() != Code.TRUE) {
            isCert = Code.FAIL;
        }
        resultMap.put("isCert", isCert);
        resultMap.put("certMsg", "请先进行实名认证");
        if (member != null) {
            String setting = org.apache.commons.lang3.ObjectUtils.defaultIfNull(Redis.use("queue").get(Code.kAuctionSetting), "");
            String[] settings = org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens(setting, ",");
            long maxPayTimeInSecond = 180;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(setting) && settings.length == 3) {
                maxPayTimeInSecond = NumberUtils.toLong(settings[0]);
            }
            long maxBuyTime = System.currentTimeMillis() - maxPayTimeInSecond * 1000;
            Record record = Db.findFirst("select * from `reverse_auction_histroy` rah where rah.`member_id` = ? and rah.buy_state = 1 and rah.buy_time > ? order by `buy_time` desc limit 1", member.getId(), new Date(maxBuyTime));
            if (record != null) {
                Long reverse_auction_detail_id = record.getLong("reverse_auction_detail_id");
                Order order = orderDao.findByActOrderId(String.valueOf(reverse_auction_detail_id));
                if (order == null) {    //  生成待支付订单
                    Long reverse_auction_histroy_id = record.getLong("id");
                    reverseAuctionService.createReverseAuctionOrderToPendingPayment(reverse_auction_histroy_id, maxPayTimeInSecond);
                }
                double balance = member.getBalance().doubleValue();
                resultMap.put("auction_history_id", record.getLong("id"));
                resultMap.put("pay_state", 1);
                resultMap.put("pay_price", record.getDouble("buy_price"));
                resultMap.put("pay_time", record.getDate("buy_time").getTime() + maxPayTimeInSecond * 1000 - System.currentTimeMillis());
                resultMap.put("balance", balance);
                renderJson(ApiResult.success(resultMap));
                return;
            }
        }
        renderJson(ApiResult.success(resultMap));
    }

    //  获取订单支付信息
    public void queryOrderPayDetail() {
        Member member = memberService.getCurrent();
        Map<String, Object> resultMap = new HashMap<>();
        if (member != null) {
            String setting = org.apache.commons.lang3.ObjectUtils.defaultIfNull(Redis.use("queue").get(Code.kAuctionSetting), "");
            String[] settings = org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens(setting, ",");
            long maxPayTimeInSecond = 180;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(setting) && settings.length == 3) {
                maxPayTimeInSecond = NumberUtils.toLong(settings[0]);
            }
            Long order_id = getParaToLong("order_id");
            Order order = orderDao.find(order_id);
            ReverseAuctionHistroy reverseAuctionHistroy = reverseAuctionHistroyDao.findByReverseAuctionDetailId(order.getActOrderId());
            double balance = member.getBalance().doubleValue();
            resultMap.put("auction_history_id", reverseAuctionHistroy.getId());
            resultMap.put("pay_state", 1);
            resultMap.put("pay_price", reverseAuctionHistroy.getBuyPrice());
            resultMap.put("pay_time", reverseAuctionHistroy.getBuyTime().getTime() + maxPayTimeInSecond * 1000 - System.currentTimeMillis());
            resultMap.put("balance", balance);
            renderJson(ApiResult.success(resultMap));
        }
        renderJson(ApiResult.success(resultMap));
    }


    /**
     * 准备支付信息
     */
    @Before(Tx.class)
    public void preparePay() {
        //  商品支付金额
        Member member = memberService.getCurrent();
        Long auction_history_id = getParaToLong("auction_history_id");
        String pay_price = getPara("pay_price");
        Integer pay_type = getParaToInt("pay_type");
        Boolean isBalance = getParaToBoolean("isBalance", false);
        String balance_price = getPara("balance_price");

        //  获取倒拍数据
        ReverseAuctionHistroy reverseAuctionHistroy = reverseAuctionHistroyService.find(auction_history_id);
        if (reverseAuctionHistroy == null) {
            renderJson(ApiResult.fail("倒拍不存在"));
            return;
        }
        if (!Code.isDevMode && isBalance){ //  使用余额支付，需要判断是否有余额、是否余额和平台支付价格一致
            if (NumberUtils.toDouble(balance_price) > member.getBalance().doubleValue() ){ //  待支付余额 大于 用户实际余额， 支付失败
                renderJson(ApiResult.fail("余额不足"));
                return;
            }
            if (NumberUtils.toDouble(pay_price) + NumberUtils.toDouble(balance_price) < reverseAuctionHistroy.getDouble("buy_price")){
                renderJson(ApiResult.fail("倒拍金额校验失败"));
                return;
            }
        }
        if ( !Code.isDevMode && !isBalance && reverseAuctionHistroy.getDouble("buy_price") != NumberUtils.toDouble(pay_price)) {
            renderJson(ApiResult.fail("倒拍金额校验失败"));
            return;
        }

        if (isBalance && NumberUtils.toDouble(pay_price) <= 0){ //  余额付
            reverseAuctionHistroy.setBuyState(2);
            reverseAuctionHistroyService.update(reverseAuctionHistroy);
            reverseAuctionService.updateReverseAuctionOrderToPendingShip(reverseAuctionHistroy, Order.PayType.balance, balance_price, balance_price, "");
            renderJson(ApiResult.success("支付成功"));
            return;
        }else{
            String order_no = "DP" + RandomStringUtils.randomAlphanumeric(16).toUpperCase();
            if (1 == pay_type) {
                //微信
                String ip = IpUtil.getIpAddr(getRequest());
                if (StrKit.isBlank(ip) || ("0:0:0:0:0:0:0:1").equals(ip)) {
                    ip = "127.0.0.1";
                }
                Map<String, String> map = userPayService.getPrepayIdForDp(Double.parseDouble(pay_price), ip, order_no);
                reverseAuctionHistroy.setBuyNo(order_no);
                reverseAuctionHistroyService.update(reverseAuctionHistroy);
                renderJson(ApiResult.success(map));
            } else if (2 == pay_type) {
                //支付宝
                Map<String, String> map = userPayService.aliPayOrderForDp(Double.parseDouble(pay_price), order_no);
                reverseAuctionHistroy.setBuyNo(order_no);
                reverseAuctionHistroyService.update(reverseAuctionHistroy);
                renderJson(ApiResult.success(map));
            } else {
                renderJson(ApiResult.fail("错误的支付类型"));
            }
        }
    }

    //  查询倒拍详情(上一期、下一期)
    private void _renderAuctionList(Record reverseAuctionRecord) {
        Long auctionId = reverseAuctionRecord.getLong("id");
        ReverseAuctionResult auctionResult = new ReverseAuctionResult();
        auctionResult.setAuctionId(auctionId);
        auctionResult.setTitle(reverseAuctionRecord.get("title"));
        auctionResult.setDownRule("本商品每分钟降价");
        String sql = "select d.id, (select `buy_price` from `reverse_auction_histroy` where `reverse_auction_detail_id` = d.id order by `buy_time` desc limit 1 ) as buy_price , " +
                " (select `user_name` from `reverse_auction_histroy` where `reverse_auction_detail_id` = d.id order by `buy_time` desc limit 1 ) as buy_username ," +
                " d.`product_id`,d.`auction_original_price`,d.`auction_start_price` , " +
                " g.`name`, g.`image` from `reverse_auction_detail` d , product p, goods g  where d.`reverse_auction_id` = ?  and d.`product_id` = p.`id`  and p.`goods_id` = g.id order by d.id";
        List<ReverseAuctionDetailResult> resultList = new ArrayList<>();
        List<Record> list = Db.find(sql, auctionId);
        for (Record record : list) {
            ReverseAuctionDetailResult result = new ReverseAuctionDetailResult();
            result.setTitle(record.get("name"));
            result.setDetailId(record.getLong("id").toString());
            result.setProductId(record.getLong("product_id").toString());
            result.setAuctionOriginalPrice((long) (record.getDouble("auction_original_price") * 100));
            result.setImage(record.get("image"));
            result.setBuyUsername(record.get("buy_username"));
            if (record.getDouble("buy_price") != null) {
                result.setBuyPrice((long) (record.getDouble("buy_price") * 100));
            }
            resultList.add(result);
        }
        auctionResult.setDetailResultList(resultList);
        renderJson(ApiResult.success(auctionResult));
    }


}
