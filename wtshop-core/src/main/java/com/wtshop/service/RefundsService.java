package com.wtshop.service;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.wtshop.Pageable;
import com.wtshop.constants.Code;
import com.wtshop.dao.DepositLogDao;
import com.wtshop.dao.RefundsDao;
import com.wtshop.dao.SnDao;
import com.wtshop.exception.AppRuntimeException;
import com.wtshop.model.*;
import com.wtshop.util.ApiResult;
import com.wtshop.util.Assert;
import com.wtshop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * Service - 退款单
 */
public class RefundsService extends BaseService<Refunds> {

    /**
     * 构造方法
     */
    public RefundsService() {
        super(Refunds.class);
    }

    private MemberService memberService = Enhancer.enhance(MemberService.class);
    private DepositLogDao depositLogDao = Enhancer.enhance(DepositLogDao.class);
    private RefundsDao refundsDao = Enhancer.enhance(RefundsDao.class);
    private ReturnsItemService returnsItemService = Enhancer.enhance(ReturnsItemService.class);
    private OrderService orderService = Enhancer.enhance(OrderService.class);
    private MiaobiLogService miaobiLogService = Enhancer.enhance(MiaobiLogService.class);
    private AccountService accountService = Enhancer.enhance(AccountService.class);
    private SnDao snDao = Enhancer.enhance(SnDao.class);


    /**
     * 查找退款单
     *
     * @param pageable
     * @return
     */
    public Page<Record> findPages(Pageable pageable) {

        String searchProperty = pageable.getSearchProperty();
        String searchValue = pageable.getSearchValue();

        String order = null;
        String returns = null;
        String sn = null;
        if ("order".equals(searchProperty)) {
            order = searchValue;
        } else if ("return".equals(searchProperty)) {
            returns = searchValue;
        } else if ("sn".equals(searchProperty)) {
            sn = searchValue;
        }


        return refundsDao.findPages(pageable, order, returns, sn);
    }


    public Refunds save(Refunds refunds) {
        Assert.notNull(refunds);

        refunds.setSn(snDao.generate(Sn.Type.refunds));

        return super.save(refunds);
    }

    Logger logger = Logger.getLogger(RefundsService.class);

    public ApiResult saveRefunds(Returns returns) {
        Order order = returns.getOrder();
        Member member = memberService.find(order.getMemberId());
        String orderNo = "";    //支付单号
        Double orderPrice = 0D;  //订单金额
        Double price = 0D;  //退款金额
        JSONObject redisSetting = JSONObject.parseObject(RedisUtil.getString("redisSetting"));
        Double scale = redisSetting.getDouble("scale");
        logger.info("本次退款喵币转换比例:" + scale);
        //  未收货状态，已发货
        if (Order.Status.shipped.ordinal() == order.getStatus()) {
            //退喵币
            if (order.getMiaobiPaid().doubleValue() > 0) {
                //换算喵币
                BigDecimal scale1 = order.getMiaobiPaid().multiply(new BigDecimal(scale)).setScale(2, BigDecimal.ROUND_HALF_UP);
                member.setPoint(member.getPoint().add(scale1).setScale(2, ROUND_HALF_DOWN));
                //添加喵币记录
                MiaobiLog pointLog = new MiaobiLog();
                pointLog.setType(3);
                pointLog.setCredit(scale1);
                pointLog.setDebit(BigDecimal.ZERO);
                pointLog.setBalance(member.getPoint());
                pointLog.setMemo("订单退款");
                pointLog.setMemberId(member.getId());
                miaobiLogService.save(pointLog);
            }

            //有余额支付
            if (order.getAmountPaid().doubleValue() > 0) {
                member.setBalance(member.getBalance().add(order.getAmountPaid()).setScale(2, ROUND_HALF_UP));
                //添加余额消费记录
                DepositLog depositLog = new DepositLog();
                depositLog.setBalance(member.getBalance());
                depositLog.setCredit(order.getAmountPaid());
                depositLog.setDebit(BigDecimal.ZERO);
                depositLog.setMemo("订单退款");
                depositLog.setType(DepositLog.Type.refunds.ordinal());
                depositLog.setOrderId(order.getId());
                depositLog.setMemberId(member.getId());
                depositLogDao.save(depositLog);
            }

            orderNo = order.getOrderNo();
            if (order.getWeixinPaid().doubleValue() > 0) {
                orderPrice = order.getWeixinPaid().doubleValue();
                price = order.getWeixinPaid().doubleValue();
                Integer totalMoney = Integer.parseInt(String.format("%.0f", orderPrice * 100));
                Integer returnMoney = Integer.parseInt(String.format("%.0f", price * 100));
                try {
                    Map<String, String> map = accountService.BackToWeChat(orderNo, totalMoney, returnMoney, "任性猫退款");
                    if (map != null) {
                        if (StringUtils.isNotEmpty(map.get("err_code"))) {
                            throw new AppRuntimeException("系统错误,请稍后尝试!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //添加微信退款逻辑
            }
            if (order.getAliPaid().doubleValue() > 0) {
                price = order.getAliPaid().doubleValue();
                //添加支付宝退款逻辑
                try {
                    accountService.tradeRefund(orderNo, price, member.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else { // 已收货的状态
            //获取订单支付的总价格-邮费
            BigDecimal amount = order.getAmount().subtract(order.getFee()).setScale(2, ROUND_HALF_DOWN);
            //获取商品总价格
            BigDecimal orderAllPrice = order.getPrice();
            //获取退款商品的价格
            BigDecimal refunds = returnsItemService.findMoneyById(returns.getId()).getAmount();
            //比例
            Double limit = refunds.divide(orderAllPrice, 2, ROUND_HALF_DOWN).doubleValue();
            //退款金额
            price = amount.multiply(new BigDecimal(limit)).setScale(2, ROUND_HALF_DOWN).doubleValue();
            //退喵币
            if (order.getMiaobiPaid().doubleValue() > 0) {
                //  换算喵币
                BigDecimal miaobiScaled = order.getMiaobiPaid().multiply(new BigDecimal(scale)).multiply(new BigDecimal(limit)).setScale(2, ROUND_HALF_DOWN);
                member.setPoint(member.getPoint().add(miaobiScaled).setScale(2, ROUND_HALF_DOWN));
                //添加喵币记录
                MiaobiLog pointLog = new MiaobiLog();
                pointLog.setType(3);
                pointLog.setCredit(miaobiScaled);
                pointLog.setDebit(BigDecimal.ZERO);
                pointLog.setBalance(member.getPoint());
                pointLog.setMemo("订单退款");
                pointLog.setMemberId(member.getId());
                miaobiLogService.save(pointLog);
            }

            orderNo = order.getOrderNo();
            //  全部使用余额支付
            if (order.getAmountPaid().doubleValue() > 0 && order.getAmountPaid().doubleValue() == order.getAmount().doubleValue()) {
                member.setBalance(member.getBalance().add(new BigDecimal(price)).setScale(2, ROUND_HALF_DOWN));
                //  添加余额消费记录
                DepositLog depositLog = new DepositLog();
                depositLog.setBalance(member.getBalance());
                depositLog.setCredit(new BigDecimal(price));
                depositLog.setDebit(BigDecimal.ZERO);
                depositLog.setMemo("订单退款");
                depositLog.setType(DepositLog.Type.refunds.ordinal());
                depositLog.setOrderId(order.getId());
                depositLog.setMemberId(member.getId());
                depositLogDao.save(depositLog);
            } else if (order.getAmountPaid().doubleValue() > 0) {
                //  部分使用余额支付
                BigDecimal balace = null;
                //  支付宝退部分+余额退部分
                if (order.getAliPaid().doubleValue() > 0) {
                    try {
                        //余额退款
                        Double balaceReturnPrice = order.getAmountPaid().multiply(new BigDecimal(limit)).setScale(2, ROUND_HALF_DOWN).doubleValue();
                        balace = new BigDecimal(balaceReturnPrice).setScale(2, ROUND_HALF_DOWN);
                        //支付宝退款
                        Double alipayReturnPrice = order.getAliPaid().multiply(new BigDecimal(limit)).setScale(2, ROUND_HALF_DOWN).doubleValue();
                        accountService.tradeRefund(orderNo, alipayReturnPrice, member.getId());
                    } catch (Exception e) {
                        logger.error("支付宝退款失败: " + e.getCause());
                        logger.error("支付宝退款失败详情: " + orderNo + " : " + returns);
                        throw new AppRuntimeException("支付宝退款失败");
                    }
                }
                //  微信退部分+余额退部分
                if (order.getWeixinPaid().doubleValue() > 0) {
                    try {
                        //  总订单金额
                        Integer totalPrice = Integer.parseInt( String.format("%.0f", order.getWeixinPaid().doubleValue() * 100));
                        //余额退款
                        Double balaceReturnPrice = order.getAmountPaid().multiply(new BigDecimal(limit)).setScale(2, ROUND_HALF_DOWN).doubleValue();
                        balace = new BigDecimal(balaceReturnPrice).setScale(2, ROUND_HALF_DOWN);
                        //微信退款
                        Double wechatReturnPrice = order.getWeixinPaid().multiply(new BigDecimal(limit)).setScale(2, ROUND_HALF_DOWN).doubleValue();
                        Integer wechatReturnPriceIntValue = Integer.parseInt(String.format("%.0f", wechatReturnPrice * 100));
                        Map<String, String> map = accountService.BackToWeChat(orderNo, totalPrice, wechatReturnPriceIntValue, "任性猫退款");
                        logger.info("微信退款 order:" + order + "map: " + map);
                        if (map == null || !"SUCCESS".equals(map.get("result_code"))) {
                            throw new AppRuntimeException("微信退款失败");
                        }
                    } catch (Exception e) {
                        logger.error("微信退款失败: " + e.getCause());
                        logger.error("微信退款失败详情: " + orderNo + " : " + returns);
                        throw new AppRuntimeException("微信退款失败");
                    }
                }

                if (balace != null)
                    member.setBalance(member.getBalance().add(balace).setScale(2, ROUND_HALF_DOWN));
                //添加余额消费记录
                DepositLog depositLog = new DepositLog();
                depositLog.setBalance(member.getBalance());
                depositLog.setCredit(balace == null ? BigDecimal.ZERO : balace);
                depositLog.setDebit(BigDecimal.ZERO);
                depositLog.setMemo("订单退款");
                depositLog.setType(DepositLog.Type.refunds.ordinal());
                depositLog.setOrderId(order.getId());
                depositLog.setMemberId(member.getId());
                depositLogDao.save(depositLog);

            } else {
                //  没有使用余额支付
                //  支付宝退
                if (order.getAliPaid().doubleValue() > 0) {
                    try {
                        //支付宝退款
                        Double alipayReturnPrice = order.getAliPaid().multiply(new BigDecimal(limit)).setScale(2, ROUND_HALF_DOWN).doubleValue();
                        accountService.tradeRefund(orderNo, alipayReturnPrice, member.getId());
                    } catch (Exception e) {
                        logger.error("支付宝退款失败: " + e.getCause());
                        logger.error("支付宝退款失败详情: " + orderNo + " : " + returns);
                        throw new AppRuntimeException(Code.FAIL,"支付宝退款失败");
                    }
                }
                //  微信退
                if (order.getWeixinPaid().doubleValue() > 0) {
                    try {
                        //  总订单金额
                        Integer totalPrice = Integer.parseInt( String.format("%.0f", order.getWeixinPaid().doubleValue() * 100));
                        //微信退款
                        Double wechatReturnPrice = order.getWeixinPaid().multiply(new BigDecimal(limit)).setScale(2, ROUND_HALF_DOWN).doubleValue();
                        Integer wechatReturnPriceIntValue = Integer.parseInt(String.format("%.0f", wechatReturnPrice * 100));
                        Map<String, String> map = accountService.BackToWeChat(orderNo, totalPrice, wechatReturnPriceIntValue, "任性猫退款");
                        logger.info("微信退款 order:" + order + "map: " + map);
                        if (map == null || !"SUCCESS".equals(map.get("result_code"))) {
                            throw new AppRuntimeException("微信退款失败");
                        }
                    } catch (Exception e) {
                        logger.error("微信退款失败: " + e.getCause());
                        logger.error("微信退款失败详情: " + orderNo + " : " + returns);
                        throw new AppRuntimeException(Code.FAIL,"微信退款失败");
                    }
                }
            }

        }

        Refunds refund = new Refunds();
        refund.setBalanceReturn(new BigDecimal(price));
        refund.setMemberId(order.getMemberId());
        refund.setSn(snDao.generate(Sn.Type.refunds));
        refund.setOrderId(order.getId());
        refund.setReturnsId(returns.getId());
        super.save(refund);

        memberService.update(member);

        order.setStatus(Order.Status.returnMoney.ordinal());
        order.update();
        return ApiResult.success();
    }
}