package com.wtshop.api.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.wtshop.CommonAttributes;
import com.wtshop.api.common.result.PriceResult;
import com.wtshop.api.interceptor.ErrorInterceptor;
import com.wtshop.model.Order;
import com.wtshop.model.SpecialCoupon;
import com.wtshop.service.OrderService;
import com.wtshop.service.ReverseAuctionService;
import com.wtshop.service.SpecialCouponService;
import com.wtshop.service.UserPayService;
import com.wtshop.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by sq on 2017/7/28.
 */
@ControllerBind(controllerKey = "/api/userPay")
@Before({ErrorInterceptor.class})
public class UserPayAPIController extends BaseAPIController {

    Logger _logger = LoggerFactory.getLogger(UserPayAPIController.class);

    private OrderService orderService = enhance(OrderService.class);
    private UserPayService userPayService = enhance(UserPayService.class);
    private ReverseAuctionService reverseAuctionService = enhance(ReverseAuctionService.class);
    private SpecialCouponService specialCouponService = enhance(SpecialCouponService.class);

    /**
     * 支付商品
     */
    @Before(Tx.class)
    public void userPay() {

        Long id = getParaToLong("id");


        Integer type = getParaToInt("type");
        String money = getPara("money");
        Order order = orderService.find(id);
        Date expire = order.getExpire();
        Date date = new Date();
        if (expire != null && !expire.after(date)) {
            renderJson(ApiResult.fail("订单已过期,请重新下单购买"));
            return;
        }

        if ( order.getSpecialcoupId()!=null&&!order.getSpecialcoupId().equals("0")) {
            List<SpecialCoupon> sPecialCouponList = specialCouponService.findBySpecialCids(order.getSpecialcoupId());
            for (SpecialCoupon sPecialCoupon:sPecialCouponList){
                if(sPecialCoupon.getMemberId().equals(order.getMemberId())&&sPecialCoupon.getStatus()==1){

                }else {
                    renderJson(ApiResult.fail(9,"代金卡异常"));
                    return;
                }

            }
           /* SpecialCoupon sPecialCoupon = specialCouponService.find(order.getSpecialcoupId());

            if(sPecialCoupon.getMemberId().equals(order.getMemberId())&&sPecialCoupon.getStatus()==1){
                //判断代金券金额是否可用
                if(order.getPrice().doubleValue()>=sPecialCoupon.getMoney().doubleValue()){


                }else {
                    renderJson(ApiResult.fail(10,"订单金额不够满减"));
                    return;
                }

            }else {
                renderJson(ApiResult.fail(9,"代金卡异常"));
                return;
            }*/
        }

        //微信
        if (1 == type) {
            String ip = IpUtil.getIpAddr(getRequest());
            if (StrKit.isBlank(ip) || ("0:0:0:0:0:0:0:1").equals(ip)) {
                ip = "127.0.0.1";
            }
            Map<String, String> map = userPayService.getPrepayId(order, ip, true);
            if(map==null){
                renderJson(ApiResult.fail("订单异常"));
                return;
            }
            renderJson(ApiResult.success(map));
        } else if(4== type) {
            String ip = IpUtil.getIpAddr(getRequest());
            if (StrKit.isBlank(ip) || ("0:0:0:0:0:0:0:1").equals(ip)) {
                ip = "127.0.0.1";
            }
            Map<String, String> map = userPayService.getPrepayIdXCX(order, ip, true);
            if(map==null){
                renderJson(ApiResult.fail("订单异常"));
                return;
            }
            renderJson(ApiResult.success(map));
        }else if (2 == type) {  //支付宝
            Map<String, String> map = userPayService.aliPayOrder(order, true);
            renderJson(ApiResult.success(map));
        } else {
           if(order.getAmountPaid()==BigDecimal.ZERO||order.getAmountPaid().compareTo(order.getAmount())!=0){
               renderJson(ApiResult.fail("订单异常"));
               return;
           }




            ApiResult result = orderService.paySuccess(order.getSn(), money, null, null);
            if (result.resultSuccess()) {
                renderJson(ApiResult.success());
            } else {
                renderJson(ApiResult.fail());
            }
        }
    }

    /**
     * 微信支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
     */
    public void paySuccess() {
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        String xmlObject = HttpKit.readData(getRequest());
        _logger.info("微信支付回调 {}", xmlObject);
        try {
            Map<String, String> map = XMLUtil.doXMLParse(xmlObject);
            boolean apiKey = PaymentKit.verifyNotify(map, prop.get("API_KEY"));
            if (apiKey) {
                _logger.info("***********************开始处理");
                if ("SUCCESS".equalsIgnoreCase(map.get("result_code"))) {

                    try {
                        //订单支付成功,更新订单
                        String id = map.get("out_trade_no");
                        String amount = map.get("total_fee");
                        String orderNo = map.get("transaction_id");
                        ApiResult result = null;
                        if (id.startsWith("RXM")) {
                            _logger.info("===============> 微信 订单 回调 <=============== \n " + map);
                            id = id.substring(3);
                            result = orderService.paySuccess(id, amount, orderNo, null);
                        } else if (id.startsWith("DP")) {
                            _logger.info("===============> 微信 倒拍 回调 <=============== \n " + map);
                            result = reverseAuctionService.handleWechatPayCallback(map);
                        } else {
                            _logger.info("===============> 微信 充值 回调 <=============== \n " + map);
                            result = orderService.CZpaySuccess(id, amount, "0");
                        }
                        if (result.resultSuccess()) {
                            getResponse().getWriter().write(XMLUtil.setXML("SUCCESS", ""));   //告诉微信服务器，我收到信息了，不要在调用回调action了
                            renderText("支付成功");
                            return;
                        } else {
                            getResponse().getWriter().write(XMLUtil.setXML("FAIL", ""));
                            renderText("支付失败");
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if ("FAIL".equalsIgnoreCase(map.get("result_code"))) {
                    getResponse().getWriter().write(XMLUtil.setXML("FAIL", ""));
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderText("");
    }

    /**
     * 支付宝 回调地址
     */
    public void alipaySuccess() {
        Map<String, String> params = new HashMap<String, String>();
        try {
            Map requestParams = getRequest().getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            _logger.info("支付宝支付回调 {}", params);
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            boolean flag = AlipaySignature.rsaCheckV1(params, AliPayUtil.getAppPublicKey(), AlipayConstants.CHARSET_UTF8, "RSA2");
            if (flag) {
                String id = params.get("out_trade_no");
                String amount = params.get("total_amount");
                String orderNo = params.get("trade_no");
                ApiResult result = null;
                if (id.startsWith("RXM")) {
                    _logger.info("===============> 支付宝 订单 回调 <=============== \n " + params);
                    id = id.substring(3);
                    result = orderService.paySuccess(id, amount, null, orderNo);
                } else if (id.startsWith("DP")) {
                    _logger.info("===============> 支付宝 倒拍 回调 <=============== \n " + params);
                    result = reverseAuctionService.handleAlipayPayCallback(params);
                } else {
                    _logger.info("===============> 支付宝 充值 回调 <=============== \n " + params);
                    result = orderService.CZpaySuccess(id, amount, "1");
                }
                if (result.resultSuccess()) {
                    renderText("success");
                    return;
                } else {
                    renderText("fail");
                    return;
                }
            } else {
                renderText("fail");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        renderText("");
    }


}
