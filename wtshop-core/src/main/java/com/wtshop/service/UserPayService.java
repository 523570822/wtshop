package com.wtshop.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.wtshop.CommonAttributes;
import com.wtshop.constants.Code;
import com.wtshop.model.ExchangeLog;
import com.wtshop.model.Order;
import com.wtshop.util.AliPayUtil;
import com.wtshop.util.RandomUtils;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.UUIDUtils;

import java.util.HashMap;
import java.util.Map;

import static com.jfinal.aop.Enhancer.enhance;

/**
 * Created by jobfo on 2017/7/30.
 */
public class UserPayService {

    private ExchangeLogService exchangeLogService = enhance(ExchangeLogService.class);
    /**
     * 微信端 获取支付信息
     * @return
     */
    public Map<String, String> getPrepayId (Order order , String ip, Boolean price){

        Logger logger = Logger.getLogger("getPrepayId");
        Double money = order.getAmountPayable().doubleValue();
        if (Code.isDevMode){
            money = 0.01;
        }

        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("appid", prop.get("AppID")); // 公众账号ID
        parameterMap.put("mch_id", prop.get("MCH_ID")); // 商户号
        parameterMap.put("nonce_str", RandomUtils.randomUpperWords(32)); // 随机字符串
        parameterMap.put("body", "任性猫微信支付"); // 商品描述
        parameterMap.put("out_trade_no", "RXM" + order.getSn()); // 商户订单号
        parameterMap.put("total_fee", String.format("%.0f", money * 100));// 订单总金额
        parameterMap.put("spbill_create_ip", ip); // 终端IP
        parameterMap.put("notify_url", prop.get("notify_url")); // 通知地址
        parameterMap.put("trade_type", PaymentApi.TradeType.APP.name()); // 交易类型

        Map<String, String> params = convertAttributes(parameterMap);
        String sign = PaymentKit.createSign(params, prop.get("API_KEY"));

        params.put("sign", sign);
        RedisUtil.setString("SIGN",sign);
        // 统一下单
        String xmlResult = PaymentApi.pushOrder(params);

        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

        String prepay_id = result.get("prepay_id");

        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", prop.get("AppID"));
        packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("noncestr", System.currentTimeMillis() + "");
        packageParams.put("package", "Sign=WXPay");
        packageParams.put("prepayid", prepay_id);
        packageParams.put("partnerid", prop.get("MCH_ID"));
        String packageSign = PaymentKit.createSign(packageParams, prop.get("API_KEY"));
        packageParams.put("paySign", packageSign);

        return packageParams;
    }

    /**
     * 微信端 获取支付信息 倒拍
     * @return
     */
    public Map<String, String> getPrepayIdForDp (Double money, String ip, String orderSn){

        if (Code.isDevMode){
            money = 0.01;
        }

        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("appid", prop.get("AppID")); // 公众账号ID
        parameterMap.put("mch_id", prop.get("MCH_ID")); // 商户号
        parameterMap.put("nonce_str", RandomUtils.randomUpperWords(32)); // 随机字符串
        parameterMap.put("body", "任性猫微信支付"); // 商品描述
        parameterMap.put("out_trade_no", orderSn ); // 商户订单号
        parameterMap.put("total_fee", String.format("%.0f", money * 100));// 订单总金额
        parameterMap.put("spbill_create_ip", ip); // 终端IP
        parameterMap.put("notify_url", prop.get("notify_url")); // 通知地址
        parameterMap.put("trade_type", PaymentApi.TradeType.APP.name()); // 交易类型

        Map<String, String> params = convertAttributes(parameterMap);
        String sign = PaymentKit.createSign(params, prop.get("API_KEY"));

        params.put("sign", sign);
        RedisUtil.setString("SIGN",sign);
        // 统一下单
        String xmlResult = PaymentApi.pushOrder(params);

        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

        String prepay_id = result.get("prepay_id");

        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", prop.get("AppID"));
        packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("noncestr", System.currentTimeMillis() + "");
        packageParams.put("package", "Sign=WXPay");
        packageParams.put("prepayid", prepay_id);
        packageParams.put("partnerid", prop.get("MCH_ID"));
        String packageSign = PaymentKit.createSign(packageParams, prop.get("API_KEY"));
        packageParams.put("paySign", packageSign);

        return packageParams;
    }

    /**
     * 小程序微信端 获取支付信息 倒拍
     * @return
     */
    public Map<String, String> getPrepayIdXCX (Order order , String ip, Boolean price){

        Logger logger = Logger.getLogger("getPrepayId");
        Double money = order.getAmountPayable().doubleValue();
        if (Code.isDevMode){
            money = 0.01;
        }

        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("appid", prop.get("XCX_APPID")); // 公众账号ID
        parameterMap.put("mch_id", prop.get("MCH_ID")); // 商户号
        parameterMap.put("nonce_str", RandomUtils.randomUpperWords(32)); // 随机字符串
        parameterMap.put("body", "小程序支付"); // 商品描述
        parameterMap.put("out_trade_no", "RXM" + order.getSn()); // 商户订单号
        parameterMap.put("total_fee", String.format("%.0f", money * 100));// 订单总金额
        parameterMap.put("spbill_create_ip", ip); // 终端IP
        parameterMap.put("notify_url", prop.get("notify_url")); // 通知地址
        parameterMap.put("trade_type", PaymentApi.TradeType.JSAPI.name()); // 交易类型

        Map<String, String> params = convertAttributes(parameterMap);
     //   String sign = PaymentKit.createSign(params, prop.get("API_KEY"));

        String sign=qianMing(params);
        params.put("sign", sign);
        // 统一下单
        String xmlResult = PaymentApi.pushOrder(params);

        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

        String prepay_id = result.get("prepay_id");

        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appId", prop.get("XCX_APPID"));
        packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("nonceStr", System.currentTimeMillis() + "");
        packageParams.put("package", "prepay_id="+prepay_id);
      //  packageParams.put("prepayid", prepay_id);
    //    packageParams.put("partnerid", prop.get("MCH_ID"));
   //    String packageSign = PaymentKit.createSign(packageParams, prop.get("API_KEY"));
        String packageSign=qianMing(packageParams);

        packageParams.put("signType", "MD5");
        packageParams.put("paySign", packageSign);

        return packageParams;
    }

    /**
     * 支付宝 支付下单
     */

    public  Map<String, String> aliPayOrder(Order order ,Boolean price) {

        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        Double money = order.getAmountPayable().doubleValue();
        if (Code.isDevMode){
            money = 0.01;
        }
        AlipayClient alipayClient = AliPayUtil.getAlipayClient();
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("任性猫支付宝付款");
        model.setSubject("任性猫支付宝付款");
        model.setOutTradeNo("RXM"+order.getSn());
        model.setTotalAmount(String.format("%.2f", money));
        model.setTimeoutExpress("30m");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(prop.get("Ali_Notify_url"));//回调地址
        String orderInfo = null;
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //response.getBody()就是orderString 可以直接给客户端请求，无需再做处理。
            orderInfo = response.getBody();
//            orderInfo = URLDecoder.decode(orderInfo, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("info", orderInfo);

        return resultMap;
    }

    /**
     * 支付宝 支付下单 倒拍
     */
    public  Map<String, String> aliPayOrderForDp(Double money, String orderSn) {

        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);

        if (Code.isDevMode){
            money = 0.01;
        }

        AlipayClient alipayClient = AliPayUtil.getAlipayClient();
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("任性猫支付宝付款");
        model.setSubject("任性猫支付宝付款");
        model.setOutTradeNo(orderSn);
        model.setTotalAmount(String.format("%.2f", money));
        model.setTimeoutExpress("30m");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(prop.get("Ali_Notify_url"));//回调地址
        String orderInfo = null;
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //response.getBody()就是orderString 可以直接给客户端请求，无需再做处理。
            orderInfo = response.getBody();
            //            orderInfo = URLDecoder.decode(orderInfo, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("info", orderInfo);

        return resultMap;
    }

    /**
     * 支付宝 充值
     */

    public  Map<String, String> aliRecharge(double price, Long exchangeId) {

        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        if (Code.isDevMode){
            price = 0.01;
        }

        AlipayClient alipayClient = AliPayUtil.getAlipayClient();
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("任性猫支付宝充值");
        model.setSubject("任性猫支付宝充值");
        model.setOutTradeNo(UUIDUtils.getLongUUID());
        model.setTotalAmount(String.format("%.2f", price));
        model.setTimeoutExpress("30m");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(prop.get("Ali_Notify_url"));//回调地址

        ExchangeLog exchangeLog = exchangeLogService.find(exchangeId);
        exchangeLog.setOrderNo(model.getOutTradeNo());
        exchangeLogService.update(exchangeLog);

        String orderInfo = null;
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //response.getBody()就是orderString 可以直接给客户端请求，无需再做处理。
            orderInfo = response.getBody();
            //            orderInfo = URLDecoder.decode(orderInfo, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("info", orderInfo);

        return resultMap;

    }


    /**
     * 微信充值
     * @return
     */
    public Map<String, String> getRechargePrepayId (double price, String ip, Long exchangeId){

        if (Code.isDevMode){
            price = 0.01;
        }

        //充值订单号
        Prop prop = PropKit.use(CommonAttributes.wtshop_PROPERTIES_PATH);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("appid", prop.get("AppID")); // 公众账号ID
        parameterMap.put("mch_id", prop.get("MCH_ID")); // 商户号
        parameterMap.put("nonce_str", RandomUtils.randomUpperWords(32)); // 随机字符串
        parameterMap.put("body", "任性猫余额充值"); // 商品描述
        parameterMap.put("out_trade_no",  UUIDUtils.getLongUUID()); // 商户订单号
        parameterMap.put("total_fee", String.format("%.0f", price * 100));// 订单总金额
        parameterMap.put("spbill_create_ip", ip); // 终端IP
        parameterMap.put("notify_url", prop.get("notify_url")); // 通知地址
        parameterMap.put("trade_type", PaymentApi.TradeType.APP.name()); // 交易类型
        Map<String, String> params = convertAttributes(parameterMap);
        String sign = PaymentKit.createSign(params, prop.get("API_KEY"));
        params.put("sign", sign);

        ExchangeLog exchangeLog = exchangeLogService.find(exchangeId);
        exchangeLog.setOrderNo(parameterMap.get("out_trade_no").toString());
        exchangeLogService.update(exchangeLog);
        // 统一下单
        String xmlResult = PaymentApi.pushOrder(params);

        Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

        String prepay_id = result.get("prepay_id");
        Map<String, String> packageParams = new HashMap<>();
        packageParams.put("appid", prop.get("AppID"));
        packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("noncestr", System.currentTimeMillis() + "");
        packageParams.put("package", "Sign=WXPay");
        packageParams.put("prepayid", prepay_id);
        packageParams.put("partnerid", prop.get("MCH_ID"));
        String packageSign = PaymentKit.createSign(packageParams, prop.get("API_KEY"));
        packageParams.put("paySign", packageSign);

        return packageParams;
    }

    /**
     * Object convert String
     *
     * @param parameterMap
     *            参数
     * @return 签名
     */
    private Map<String, String> convertAttributes(Map<String, Object> parameterMap) {
        Map<String, String> parameterNewMap = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
            if (entry.getValue() instanceof String) {
                parameterNewMap.put(entry.getKey(), (String) entry.getValue());
            }
        }
        return parameterNewMap;
    }
    private String qianMing(Map<String, String> parameterMap){
        parameterMap.remove("sign");
        String stringA = PaymentKit.packageSign(parameterMap, false);
        String stringSignTemp = stringA ;
        return HashKit.md5(stringSignTemp).toUpperCase();
    }

}
